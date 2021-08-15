package com.lucid.OAE;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;

import static java.lang.System.arraycopy;

public class OAETestProvider {
    private static final String TAG = "ER-36 OAE";
    static final int LUCID_BRAND_ID = 0xA0EABA70;
    public static final int CMD_OBTAIN_VERSION = 9;
    public static final int CMD_OBTAIN_BRAND_ID = 53;
    public static final int CMD_OBTAIN_POINTERS = 32;
    public static final int CMD_WRITE_CMOS = 29;
    public static final int CMD_POWER_OFF = 37;
    public static final int CMD_OBTAIN_TEST_SECTOR = 45;
    private static int DSPFirmwareVersion;
    private static final int [] SectorBuffer = new int [512 / 3 + 1];
    IOAETestReceiver mCaller;
    OAEProviderState oaeProviderState;
    ArrayList<OAETest> tests = new ArrayList<OAETest>();
    OAETestProvider.Pointers pointers = new OAETestProvider.Pointers();

    enum BLECommand {
        GET_VERSIONS,
        BEGIN_AUTOSTART,
        BEGIN_CALIBRATION,
        BEGIN_DP_FREQ_TEST,
        CANCEL,
        FLAG_FOR_ERASE,
        POWER_OFF
    }

    private final byte[] command = new byte[5];
    // shortbuff is used for temporary storage of small incoming buffers, accumulates
    // up to two bytes
    private byte[] shortbuff = null;

    public OAETestProvider(IOAETestReceiver caller) {
        mCaller = caller;
        oaeProviderState = new OAEProviderState();
    }

    private static int Reverse3Bytes(byte[] in, int index)  {
        int b1 = in[index] & 0xff;
        int b2 = (in[index + 1]  & 0xff ) << 8;
        int b3 = (in[index +2]  & 0xff) << 16;
        return b1 + b2 + b3;    // reverse byte order
    }
    private static String Reverse3ByteString(byte[] in, int index)  {
        char c1 = (char) in[index];
        char c2 = (char) in[index + 1];
        char c3 = (char) in[index + 2];
        return Character.toString(c3) + c2 + c1;    // reverse byte order
    }

     OAETest ParseSector12Freq(byte[] CommandResponse, ArrayList<OAETest> tests) throws Exception {
        OAETest test;
        Frequency f;

        Log.d(TAG, "ParseSector12Freq \n");

        int j = 0;
        // skip over the echoed command, which is the first 5 bytes of data
        for (int i = 5; i < (510 + 5); i += 3) {
            SectorBuffer[j] = (CommandResponse[i + 2]  & 0xff) * 256 * 256 +
                    (CommandResponse[i + 1] & 0xff) * 256 +
                    (CommandResponse[i] & 0xff);
            j++;
        }

        if (SectorBuffer[0] == 0xffffff)
            return null;

        if (DSPFirmwareVersion < 10800 || DSPFirmwareVersion > 10899) {
            String m = ("Unsupported version of firmware: " + DSPFirmwareVersion);
            Log.d(TAG, m +"\n");
              throw new Exception(m);
        }

        int TestType = SectorBuffer[0x88];

        if ((TestType & 0x800000) != 0) {
            // adjustment for 3-byte negative DSP numbers
            TestType = TestType | 0xFF000000;
        }

        if (TestType < -1 || TestType == 0) {
            String m = ("Invalid test type " + TestType);
            Log.d(TAG, m +"\n");
            throw new Exception(m);
        }
        if (TestType < 0) {
            test = new TETest();
            ((TETest) (test)).Frequencies = new ArrayList<TETestFrequency>();
        } else {
            test = new DPTest();
            ((DPTest) (test)).Frequencies = new ArrayList<DPTestFrequency>();
        }
         tests.add(test);

         test.RecordVersion = SectorBuffer[0x9A];
        if (test.getRecordVersion() != 210)
            throw new Exception("Invalid record version " + test.getRecordVersion());

        test.ParmChangeFlags = SectorBuffer[0x92];
        test.MinPassSetting = SectorBuffer[0x9b];
        if ((test.getMinPassSetting() & 0x80000) != 0)
            test.MinPassSetting = test.getMinPassSetting() | 0xff000000;  // extend sign bit for negative numbers

        test.NumberFrequencies = SectorBuffer[0x8E];
        test.TestType = TestType;
        test.AutostopFlag = SectorBuffer[0x9c];
        test.ReverseFrequencyFlag = SectorBuffer[0x9d];

        String MinorVersion;
        String MajorVersion = String.valueOf(DSPFirmwareVersion / 100);
        int MinorVersionNumeric = DSPFirmwareVersion % 100;

        if (MinorVersionNumeric > 9 )
            MinorVersion = String.valueOf(MinorVersionNumeric);
        else
            MinorVersion = "0" + MinorVersionNumeric;

        test.FWVersion = MajorVersion + "." + MinorVersion;

        test.Ear = (char) ((SectorBuffer[0x87] & 0xff00) >> 8);

        int ind = 0;
        char Result;

        for (int k = 0; k < test.getNumberFrequencies(); k++) {

            char upper = (char) ((SectorBuffer[k + 0x7a] >> 16) & 0xFF);
            char middle = (char) ((SectorBuffer[k + 0x7a] >> 8) & 0xFF);
            char lower = (char) ((SectorBuffer[k + 0x7a]) & 0xFF);

            if (TestType >= 0) {
                // DP
                f = new DPTestFrequency();
                ((DPTest) (test)).getFrequencies().add((DPTestFrequency) f);
                ((DPTestFrequency) f).L1 = convert1(SectorBuffer[ind + 1]);
                ((DPTestFrequency) f).L2 = convert1(SectorBuffer[ind + 2]);
            } else {
                f = new TETestFrequency();
                ((TETest) (test)).Frequencies.add((TETestFrequency) f);
                if (ind == 5) {
                    //  actual stimulus peak value (float dB)
                    ((TETest) test).TEPeak = convert1(SectorBuffer[ind + 2]);
                    ((TETestFrequency) f).TEPeak = ((TETest) test).TEPeak;
                } else if (ind == 7) {
//                    actual test time in seconds
                    ((TETest) test).TETestTime = convert1(SectorBuffer[ind + 2]);
                }
            }
            f.FrequencyLabel = String.valueOf(upper) + middle + lower;
            f.FrequencyValue = Float.valueOf(f.FrequencyLabel);
            f.DPTEEmissions = convert1(SectorBuffer[ind + 3]);
            f.PercentBlockAccept = SectorBuffer[ind + 4] / 0x8000;
            f.SNR = convert1(SectorBuffer[ind + 5]);
            f.NF = convert1(SectorBuffer[ind]);
            Result = (char) (SectorBuffer[ind + 6] & 0xFF);
            switch (Result) {
                case ' ':
                case 'F':
                case 'P':
                case '*':
                case 'X':
                    f.Result = Result;
                    break;
                default:
                    f.Result = 'E';
                    break;    // no result
            }

            ind += 7;   // point to the next array
        }

        int ts_sec, ts_min, ts_hour_24, ts_day, ts_month, ts_year;
        ts_sec = (SectorBuffer[0x54] & 0xF) + (SectorBuffer[0x54] & 0xF00) / 256 * 10;
        ts_min = (SectorBuffer[0x55] & 0xF) + (SectorBuffer[0x55] & 0xF00) / 256 * 10;
        ts_hour_24 = (SectorBuffer[0x56] & 0xF) + (SectorBuffer[0x56] & 0xF00) / 256 * 10;
        char AMPMIndicator = (char) ((SectorBuffer[0x5b] & 0xFF00) / 256);
        if (AMPMIndicator == 'P') {
            ts_hour_24 += 12;
            if (ts_hour_24 == 24) ts_hour_24 = 12; // 12 PM is noon
        } else if (AMPMIndicator == 'A') {
            if (ts_hour_24 + 12 == 24) ts_hour_24 = 0; // 12 AM is midnight
        }

        ts_day = (SectorBuffer[0x58] & 0xF00) / 256 +
                (SectorBuffer[0x58] & 0xF0000) / 256 * 10 / 256;
        ts_month = SectorBuffer[0x94] - 1;
        ts_year = 2000 + (SectorBuffer[0x5a] & 0xF) +
                (SectorBuffer[0x5a] & 0xF00) / 256 * 10;

         GregorianCalendar cal = new GregorianCalendar();
        cal.set(ts_year, ts_month, ts_day, ts_hour_24, ts_min, ts_sec);
        test.TestDate = cal.getTime();

        int ProbeSerialIndex = 0x95;
        char upper, middle, lower;
        StringBuilder sb = new StringBuilder();

        if (SectorBuffer[ProbeSerialIndex] != 0) { // ignore if serial was never written (all one bits)
           for (int i = ProbeSerialIndex; i < ProbeSerialIndex + 3; i++) {
               upper = (char) ((SectorBuffer[i] >> 16) & 0xff);
               middle = (char) ((SectorBuffer[i] >> 8) & 0xff);
               lower = (char) (SectorBuffer[i]  & 0xff);
               sb.append(lower).append(middle).append(upper);
           }
           test.ProbeSerial = sb.toString().trim();
        } else {
            throw new Exception("probe serial is missing");
        }

        int InstrumentSerialIndex = 0x99;
        int InstrumentSerialPrefix = 0x98;

//        obtain instrument serial (3 ASCII character prefix and a binary number)
        if (SectorBuffer[InstrumentSerialIndex] != 0) { // ignore if serial was never written (all one bits)
            char prefix1, prefix2, prefix3;

            prefix1 = (char) ((SectorBuffer[InstrumentSerialPrefix] >> 16) & 0xff);
            prefix2 = (char) ((SectorBuffer[InstrumentSerialPrefix] >> 8) & 0xff);
            prefix3 = (char) (SectorBuffer[InstrumentSerialPrefix]  & 0xff);

            if (! Character.isLetter(prefix1)) prefix1 = ' ';
            if (! Character.isLetter(prefix2)) prefix2 = ' ';
            if (! Character.isLetter(prefix3)) prefix3 = ' ';

            sb = new StringBuilder();
            sb.append(prefix1).append(prefix2).append(prefix3);
            sb.append(SectorBuffer[InstrumentSerialIndex]);
            test.InstrumentSerial = String.format("%7s", sb.toString().trim()).replace(' ', '0');
        } else {
            throw new Exception("probe serial is missing");
        }

        test.TestNumber = SectorBuffer[0x86];

        StringBuilder builder = new StringBuilder();

        byte bupper, bmiddle, blower;

        for (int i = 0x76; i < 0x7A; i++) {
            bupper = (byte) ((SectorBuffer[i] >> 16) & 0xff);
            bmiddle = (byte) ((SectorBuffer[i] >> 8) & 0xff);
            blower = (byte) (SectorBuffer[i] & 0xff);
            builder.append((char) (bupper)).append((char) bmiddle).append((char) blower);
        }
        builder.setLength(10);
        test.ProtocolName = builder.toString();

        return test;
    }

    private static int convert1(int value) {
        // shift so units are 1/256
        if (value > (1 <<  23) )
            return (value - (1 <<  24)) / 128;
        else
            return  (value / 128 );
    }

    public void receive(byte[] inbuff) throws IOException {

        // make our copy that we can manipulate (inbuff is final)
        byte[] data = new byte[inbuff.length];
        System.arraycopy(inbuff, 0, data, 0, inbuff.length);

        try {
            Log.d(TAG, "\n received " + data.length + " bytes" +
                    " \n" + HexDump.dumpHexString(data, 0, data.length)
                    + " \nOAECommand  " +
                    oaeProviderState.OAECommand.code + " ExpectedPacketLength " +
                    + oaeProviderState.OAECommand.ExpectedPacketLength +
                    " accumulated " +
                    (oaeProviderState.OAECommand.AccumulatedLength + data.length) + "\n");

            // we are looking for a poll from the instrument
            if (data.length >= 2 & data[0] == 27 && data[1] == 'v') {
                Log.d(TAG, "poll detected\n");
                shortbuff = null;       // no need for this data
                
                mCaller.OnPollDetected(true);
                byte[] ER_PCRESPONSE = {(byte) 0xf2};

                try {
                    mCaller.send(ER_PCRESPONSE);
                    oaeProviderState.state = OAEProviderState.RequestStates.CONNECTED;
                    mCaller.DisplayMessage("poll detected ** ");
                } catch (IOException e) {
                    if (e.getMessage() == null)
                        Log.d(TAG, e.toString());
                    else
                        Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                    throw e;
                }

                Log.d(TAG, "sent PC response\n");
                mCaller.DisplayMessage(" requesting connection \n");
                return;
            }

            // we did not detect a poll
            if (oaeProviderState.OAECommand.ExpectedPacketLength != 0)
                shortbuff = null;   // no need for this buffer, we already know packet length
            else {
                // still do not know what kind of packet is coming in
                if (shortbuff != null) {
                    // we already have some accumulated data
                    if ((shortbuff.length + data.length) >= 3) {
                        // we have enough for parsing, concatenate the buffers and keep going
                        byte[] temp = new byte[shortbuff.length + data.length];
                        System.arraycopy(shortbuff, 0, temp, 0, shortbuff.length);
                        System.arraycopy(data, 0, temp, shortbuff.length, data.length);
                        data = new byte[temp.length];
                        System.arraycopy(temp, 0, data, 0, temp.length);
                        shortbuff = null;
                        Log.d(TAG, "\n short chunk: concatenated buffers, go parse");
                    } else {
                        // shortbuff already has 1 byte, append the newly arrived one
                        byte sbyte = shortbuff[0];
                        byte databyte = data[0];
                        shortbuff = new byte[2];
                        shortbuff[0] = sbyte;
                        shortbuff[1] = databyte;
                        Log.d(TAG, "\n short chunk: added 1 byte, wait for more");
                        return;     // wait for more data
                    }
                } else {
                    // nothing in shortbuff
                    if (data.length < 3) {
                        // a short chunk right at the beginning - set it aside and keep going
                        shortbuff = new byte[data.length];
                        Log.d(TAG, "\n short chunk: added " + data.length + " bytes to new shortbuff, wait for more\n");
                        System.arraycopy(data, 0, shortbuff, 0, data.length);
                        return;     // remember, wait for more data
                    }
                }
            }

//            if (data.length >= 3 & data[0] == 4 && data[1] == 0x35 && data[2] == 0x3) {
                if (data.length >= 3 & data[0] == 4 && data[1] == 0x36 && data[2] == 0x3) {
                // real time axes display data

                Log.d(TAG, "axes display data\n");
                mCaller.DisplayMessage("axes display data detected ** ");
                oaeProviderState.state = OAEProviderState.RequestStates.RECEIVING_AXES_DISPLAY_DATA;
                oaeProviderState.OAECommand.code = 0;
                oaeProviderState.OAECommand.ExpectedPacketLength = 56;
                oaeProviderState.OAECommand.AccumulatedLength = 0;
            }

            else if (data.length >= 3 & data[0] == 4 && data[1] == 0x1f && data[2] == 0x3) {
                // real time frequency display data
                Log.d(TAG, "frequency display data\n");
                oaeProviderState.state = OAEProviderState.RequestStates.RECEIVING_DP_FREQ_DISPLAY_DATA;
                oaeProviderState.OAECommand.code = 0;
                oaeProviderState.OAECommand.ExpectedPacketLength = 33;
                oaeProviderState.OAECommand.AccumulatedLength = 0;
            }

            if (oaeProviderState.OAECommand.AccumulatedLength + data.length > oaeProviderState.OAECommand.ExpectedPacketLength) {
//                throw new RuntimeException("Data overflow. data length " + data.length + " CommandInfo: " + oaeProviderState.OAECommand.toString());
                Log.d(TAG,"Data overflow, proceeding anyway -- data length " + data.length + " CommandInfo: " + oaeProviderState.OAECommand.toString());
                ParseCommandResponse(oaeProviderState.OAECommand, OAETest.CommandResponse);
            }
            arraycopy(data, 0, OAETest.CommandResponse, oaeProviderState.OAECommand.AccumulatedLength , data.length);
            oaeProviderState.OAECommand.AccumulatedLength += data.length;

            if (oaeProviderState.OAECommand.AccumulatedLength == oaeProviderState.OAECommand.ExpectedPacketLength) {
                // all of the packet arrived, reset lenghts
                oaeProviderState.OAECommand.AccumulatedLength = 0;
                oaeProviderState.OAECommand.ExpectedPacketLength = 0;
                ParseCommandResponse(oaeProviderState.OAECommand, OAETest.CommandResponse);

            } else {
                Log.d(TAG, "Received " + oaeProviderState.OAECommand.AccumulatedLength + " / "
                        + oaeProviderState.OAECommand.ExpectedPacketLength + "\n");
            }
        } catch (Exception e) {
            if (e.getMessage() == null) Log.d(TAG, e.toString());
            else Log.d(TAG, e.getMessage());
            oaeProviderState.OAECommand.AccumulatedLength = 0;
        }
    }

    void ParseBLECommandResponse(OAECommandInfo oaeCommand, byte[] commandResponse) {
        try {
            Log.d(TAG, "ParseBLECommandResponse:  oaeProviderState: " + oaeProviderState);
            if (oaeCommand.code == BLECommand.GET_VERSIONS.ordinal()) {
                // FW Version
                String msg = String.format("FirmwareVersion Response: %d.%d    Record Version: %d\n",
                        commandResponse[1], commandResponse[2], commandResponse[3]);
                Log.d(TAG, msg);
                mCaller.DisplayMessage(msg);
                oaeProviderState.state = OAEProviderState.RequestStates.BLE_BEGIN_AUTOSTART;
                oaeProviderState.OAECommand = new OAECommandInfo(BLECommand.BEGIN_AUTOSTART.ordinal(),5);
                mCaller.send(new byte[] {(byte) BLECommand.BEGIN_AUTOSTART.ordinal()});

                return;
            }

            if (oaeCommand.code == BLECommand.BEGIN_AUTOSTART.ordinal()) {
                String msg = String.format("AUTOSTART Response: Return Code %d Noise %d Volume %d  Flags 0x%x\n",
                        commandResponse[1], commandResponse[2], commandResponse[3], commandResponse[4]);
                Log.d(TAG, msg);
                mCaller.DisplayMessage(msg);

                if ( commandResponse[1] != 0) {
                    return;
                }

                msg = "AUTOSTART successful: proceeding with test\n";
                Log.d(TAG, msg);
                mCaller.DisplayMessage(msg);
                oaeProviderState.state = OAEProviderState.RequestStates.BLE_BEGIN_CALIBRATION;
                oaeProviderState.OAECommand = new OAECommandInfo(BLECommand.BEGIN_CALIBRATION.ordinal(),2);
                mCaller.send(new byte[] {(byte) BLECommand.BEGIN_CALIBRATION.ordinal()});

                return;
            }

            if (oaeCommand.code == BLECommand.BEGIN_CALIBRATION.ordinal()) {
                String msg = String.format("Calibration Response: Return Code %d\n", commandResponse[1]);
                Log.d(TAG, msg);
                mCaller.DisplayMessage(msg);

                short F2=1200, dpV1 = 129, dpV2 = 110, n_blocks = 40;
                msg = String.format("Begin DP Test Request: F2=%d Hz, dpV1=%d,  dpV2=%d, n_blocks= %d\n",
                        F2, dpV1 , dpV2 , n_blocks );
                Log.d(TAG, msg);
                mCaller.DisplayMessage(msg);

                oaeProviderState.state = OAEProviderState.RequestStates.BLE_BEGIN_DP_FREQ_TEST;
                oaeProviderState.OAECommand = new OAECommandInfo(BLECommand.BEGIN_DP_FREQ_TEST.ordinal(),25);
                mCaller.send(new byte[] {(byte) BLECommand.BEGIN_DP_FREQ_TEST.ordinal(),
                        (byte) ((F2 >> 8 ) & 0xff), (byte) (F2 & 0xff),
                        (byte) ((dpV1 >> 8 ) & 0xff), (byte) (dpV1 & 0xff),
                        (byte) ((dpV2 >> 8 ) & 0xff), (byte) (dpV2 & 0xff),
                        (byte) ((n_blocks >> 8 ) & 0xff), (byte) (n_blocks & 0xff)  });

                return;
            }
            if (oaeCommand.code == BLECommand.BEGIN_DP_FREQ_TEST.ordinal()) {

                String msg = String.format("DP Test Response: L1=%d, L2=%d,  DP=%d, NF=%d, SNR=%d, Block Accepted=%d\n",
                        ConvertToInt(commandResponse,  1),
                        ConvertToInt(commandResponse,  5),
                        ConvertToInt(commandResponse,  9),
                        ConvertToInt(commandResponse,  13),
                        ConvertToInt(commandResponse,  17),
                        ConvertToInt(commandResponse,  21) );

                Log.d(TAG, msg);
                mCaller.DisplayMessage(msg);
            }
        }
        catch (Exception e) {
            mCaller.DisplayMessage(e.getMessage());
            Log.d(TAG, "oaeProviderState " + oaeProviderState + " Exception\n" +  e.getMessage() +"\n");
            Log.d(TAG, e.getMessage());
        }
    }

    int ConvertToInt (byte[] data, int offset) {
        return (data[offset] >> 24) + (data[offset + 1] >> 16) + (data[offset + 2] >> 8) + data[offset + 3];
    }
    void ParseER36CommandResponse(OAECommandInfo oaeCommand, byte[] commandResponse) {
        try {
            Log.d(TAG, "ParseER36CommandResponse:  oaeProviderState: " + oaeProviderState);
            try {
                if (oaeProviderState.state == OAEProviderState.RequestStates.RECEIVING_AXES_DISPLAY_DATA) {
                    Log.d(TAG, " Parsing RECEIVING_AXES_DISPLAY_DATA\n");
                    int i = 0;
                    byte packet_type = commandResponse[i++];
                    byte length = commandResponse[i++];
                    byte command = commandResponse[i++];
                    byte content_type = commandResponse[i++];
                    byte content_length = commandResponse[i++];
                    byte graph_type = commandResponse[i++];
                    byte number_of_freqs = commandResponse[i++];
                    byte norms_mode = commandResponse[i++];
                    byte graph_mode = commandResponse[i++];

                    TestDisplayData testDisplayData = new TestDisplayData();
                    testDisplayData.GraphType = graph_type;
                    testDisplayData.NumberOfFrequencies = number_of_freqs;
                    testDisplayData.NormsMode = norms_mode != 0;
                    testDisplayData.GraphMode = graph_mode;

                    i = i + 3;  // pass threshold todo
                    String [] FreqLabels = new String[12];
                    for (int j = 0; j < 12; j++) {
                        FreqLabels [j] = Reverse3ByteString(commandResponse, i);
                        i = i + 3;
                        testDisplayData.FrequencyLabels[j] = FreqLabels [j];
                    }

                    testDisplayData.P1 = convert2(commandResponse, i);
                    i = i + 3;

                    testDisplayData.P2 = convert2(commandResponse, i);
                    i = i + 3;
                    Log.d(TAG,"Ear commandResponse[i]: " + commandResponse[i] + "\n ");
                    testDisplayData.Ear = (char) (commandResponse[i++]);
                    short command1 = commandResponse[i];

                    Log.d(TAG,"Calling OnGraphAxesReady " + testDisplayData.toString() + "\n ");
                    mCaller.OnRealTestDisplayInit(testDisplayData);
                    return;
                }

                if (oaeProviderState.state == OAEProviderState.RequestStates.RECEIVING_DP_FREQ_DISPLAY_DATA) {
                    Log.d(TAG, " Parsing RECEIVING_DP_FREQ_DISPLAY_DATA\n");
                    int i = 0;
                    byte packet_type = commandResponse[i++];
                    byte length = commandResponse[i++];
                    byte command = commandResponse[i++];
                    byte content_type = commandResponse[i++];
                    byte l = commandResponse[i++];
                    byte graph_type = commandResponse[i++];
                    byte number_of_freqs = commandResponse[i++];
                    byte index = commandResponse[i++];
                    String  label = Reverse3ByteString(commandResponse, i);
                    i = i + 3;
                    int accept_ratio = Reverse3Bytes(commandResponse, i);
                    i = i + 3;
                    int Pnoise = convert2(commandResponse, i);
                    i = i + 3;
                    int P1 = convert2(commandResponse, i);
                    i = i + 3;
                    int P2 = convert2(commandResponse, i);
                    i = i + 3;
                    int PDPE = convert2(commandResponse, i);
                    i = i + 3;
                    int SNR = convert2(commandResponse, i);
                    i = i + 3;
                    char TestIndicator = (char) commandResponse[i++];
                    char ear = (char) commandResponse[i++];
                    short graph_mode = commandResponse[i++];
                    short command1 = commandResponse[i];
                    String msg = String.format("packet_type %d length %d, command %d, content_type %d\n",
                            packet_type,
                            length,
                            command,
                            content_type);
                    msg = msg + String.format("content_length % d, graph_type %d, number_of_freqs %d," +
                                    "freq index %d, graph_mode %d\n",
                            l,
                            graph_type ,
                            number_of_freqs,
                            index ,
                            graph_mode );

                    msg  = msg + String.format("label %s, ratio %d, Noise %d , P1 % d, P2 %d, PDPE %d, SNR %d, command1 %d",
                            label, accept_ratio, Pnoise,
                            P1, P2, PDPE, SNR, command1);
                    Log.d(TAG, msg);

                    DPTestFrequencyDisplay dptfe  = new DPTestFrequencyDisplay();
                    dptfe.L1 = P1;
                    dptfe.L2 = P2;
                    dptfe.DPTEEmissions = PDPE;
                    dptfe.SNR = SNR;
                    dptfe.NF = dptfe.DPTEEmissions - dptfe.SNR;
                    dptfe.AcceptRatio = accept_ratio;
                    dptfe.Noise = Pnoise;
                    dptfe.Ear = ear;
                    dptfe.index = index;
                    dptfe.TestIndicator = TestIndicator;
                    dptfe.FrequencyLabel = label;

                    Log.d(TAG,"Calling OnTestResultReady " + dptfe.toString() + "\n ");
                    mCaller.OnRealTimeTestResultReady(dptfe);

                    return;
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            if (oaeCommand.code == CMD_OBTAIN_VERSION) {
                // FW Version
                DSPFirmwareVersion = Reverse3Bytes(commandResponse, 2);
                Log.d(TAG, "DSPFirmwareVersion " + DSPFirmwareVersion);

                oaeProviderState.state = OAEProviderState.RequestStates.OBTAINING_BRAND_ID;
                oaeProviderState.OAECommand = new OAECommandInfo(53,11);
                mCaller.send(new byte[] {0, CMD_OBTAIN_BRAND_ID});

                return;
            }

            if (oaeCommand.code == CMD_OBTAIN_BRAND_ID) {
                // brand id
                int  i1,i2, BrandID, Prefix;
                i1 = Reverse3Bytes(commandResponse, 2);
                i2 = Reverse3Bytes(commandResponse, 5);
                BrandID = i1 + (i2 << 24);
                Prefix = Reverse3Bytes(commandResponse, 8);
                if (BrandID != LUCID_BRAND_ID) {
                    throw new Exception("Unsupported device brand");
                }
                oaeProviderState.OAECommand = new OAECommandInfo(32,20);
                oaeProviderState.state = OAEProviderState.RequestStates.OBTAINING_VERSION;

                mCaller.send(new byte[] {0, CMD_OBTAIN_POINTERS});

                return;
            }
            int testSectorAddress;
            int EXPRESS_TEST_START_SECTOR_12FREQ = 0xA0000;
            if (oaeCommand.code == CMD_OBTAIN_POINTERS) {
                // CMOS

                pointers.PointerWrite = Reverse3Bytes(commandResponse, 2);
                pointers.PointerRead = Reverse3Bytes(commandResponse, 5);
                pointers.PointerLeft = Reverse3Bytes(commandResponse, 8);
                pointers.PointerRight = Reverse3Bytes(commandResponse, 11);
                pointers.PrintSaveMode = Reverse3Bytes(commandResponse, 14);
                pointers.NamesPresent =  commandResponse[17] == 0;
                Log.d(TAG, "Pointers " + pointers.toString());

                tests = new ArrayList<OAETest>(); // clear

                if (pointers.PrintSaveMode == 1) {
                    mCaller.DisplayMessage("\nPrintSaveMode is not currently supported\n");
                    oaeProviderState.state = OAEProviderState.RequestStates.CONNECTED;
                    return;
                } else {
                    int Totalresults = 0;
                    // pointer with value NO_RESULTS means "no results"
                    if (pointers.PointerLeft != 0xfff)
                        Totalresults +=  1;
                    if  (pointers.PointerRight != 0xfff)
                        Totalresults +=  1;

                    if (Totalresults > 0) {
                        oaeProviderState.testSectors = new int[Totalresults];
                        int i = 0;
                        if (pointers.PointerLeft != 0xfff)
                            oaeProviderState.testSectors[i++] = pointers.PointerLeft;
                        if (pointers.PointerRight!= 0xfff)
                            oaeProviderState.testSectors[i] = pointers.PointerRight;
                        oaeProviderState.currentTestSectorIndex = 0;
                        String msg = "\n\nStarting transfer of " + Totalresults + " results\n";
                        mCaller.DisplayMessage(msg);
                    } else {
                        mCaller.DisplayMessage("No test results found on instrument");
                        return;
                    }

                    command[1]=45;  // fun45

                    if  (pointers.PointerLeft != 0xfff) {
                        oaeProviderState.OAECommand = new OAECommandInfo(45,515);
                        oaeProviderState.state = OAEProviderState.RequestStates.OBTAINING_TEST_SECTORS;
                        testSectorAddress = EXPRESS_TEST_START_SECTOR_12FREQ + 512 * pointers.PointerLeft;
                        command[2] = (byte) (testSectorAddress & 0xff);
                        command[3] = (byte) ((testSectorAddress & 0xff00) >> 8);
                        command[4] = (byte) ((testSectorAddress & 0xff0000) >> 16);
                        mCaller.send(command);
                        return;
                    }
                    if  (pointers.PointerRight != 0xfff) {
                        oaeProviderState.OAECommand = new OAECommandInfo(45,515);
                        oaeProviderState.state = OAEProviderState.RequestStates.OBTAINING_TEST_SECTORS;
                        testSectorAddress = EXPRESS_TEST_START_SECTOR_12FREQ + 512 * pointers.PointerRight;
                        command[2] = (byte) (testSectorAddress & 0xff);
                        command[3] = (byte) ((testSectorAddress & 0xff00) >> 8);
                        command[4] = (byte) ((testSectorAddress & 0xff0000) >> 16);
                        mCaller.send(command);
                        return;
                    }
                }

                return;

            } else if (oaeCommand.code == CMD_OBTAIN_TEST_SECTOR) {

                // parse this sector

                OAETest test;
                test = ParseSector12Freq(OAETest.CommandResponse, tests);
                if (test != null) {
                    Log.d(TAG,"Parsed sector " + test.getTestNumber() + " outcome " + test.getTestOutcome() + "\n");
                } else {
                    Log.d(TAG,"test not parsed\n");
                }

                // more tests to come?
                oaeProviderState.currentTestSectorIndex++;
                if (oaeProviderState.currentTestSectorIndex < oaeProviderState.testSectors.length) {
                    Log.d(TAG,"Processing currentTestSectorIndex " + oaeProviderState.currentTestSectorIndex );
                    oaeProviderState.OAECommand = new OAECommandInfo(CMD_OBTAIN_TEST_SECTOR,515);
                    oaeProviderState.state = OAEProviderState.RequestStates.OBTAINING_TEST_SECTORS;
                    testSectorAddress = EXPRESS_TEST_START_SECTOR_12FREQ +
                            512 * oaeProviderState.testSectors[oaeProviderState.currentTestSectorIndex];
                    command[2] = (byte) (testSectorAddress & 0xff);
                    command[3] = (byte) ((testSectorAddress & 0xff00) >> 8);
                    command[4] = (byte) ((testSectorAddress & 0xff0000) >> 16);
                    mCaller.send(command);
                    return;

                } else {
                    Log.d(TAG,"Calling OnBulkTestResultsReady with " + tests.size() + " tests\n ");
                    mCaller.OnBulkTestResultsReady(tests);
                }

                return;
            }
        }
        catch (Exception e) {
            mCaller.DisplayMessage(e.getMessage());
            Log.d(TAG, "oaeProviderState " + oaeProviderState + " Exception\n" +  e.getMessage() +"\n");
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

    }

    private int convert2(byte[] commandResponse, int i) {
         int result = convert1((commandResponse[i + 2]  & 0xff) * 256 * 256 +
                (commandResponse[i + 1] & 0xff) * 256 +
                (commandResponse[i] & 0xff));
        return  result;
    }

    void ParseCommandResponse(OAECommandInfo oaeCommand, byte[] commandResponse) {
        if (mCaller.isBLE())
            ParseBLECommandResponse(oaeCommand, commandResponse);
        else
            ParseER36CommandResponse(oaeCommand, commandResponse);
    }

    public void PowerOff() throws IOException {
        // power off the instrument
        if (mCaller.isBLE()) {
            byte command =  (byte) BLECommand.POWER_OFF.ordinal();
            oaeProviderState.OAECommand = new OAECommandInfo(command,0);
            mCaller.send(new byte[] {command});
        }
        else {
            oaeProviderState.OAECommand = new OAECommandInfo(37,0);
            mCaller.send(new byte[] {0, CMD_POWER_OFF});
        }
        oaeProviderState.state = OAEProviderState.RequestStates.POWERED_OFF ;
    }

    public void GetTestResults() throws IOException {

        // fist step: obtain firmware version
        if (mCaller.isBLE()) {
            byte command =  (byte) BLECommand.GET_VERSIONS.ordinal();
            oaeProviderState.OAECommand = new OAECommandInfo(command,4);
            mCaller.send(new byte[] {command});
        }
        else {
            oaeProviderState.OAECommand = new OAECommandInfo(CMD_OBTAIN_VERSION,5);
            mCaller.send(new byte[] {0, CMD_OBTAIN_VERSION});
        }
        oaeProviderState.state = OAEProviderState.RequestStates.OBTAINING_VERSION;
    }

    public void SetFlag() throws IOException {

        // tell the instrument that recording a new test should erase all the old, stored tests
        if (mCaller.isBLE()) {
            byte command =  (byte) BLECommand.FLAG_FOR_ERASE.ordinal();
            oaeProviderState.OAECommand = new OAECommandInfo(command,0);
            mCaller.send(new byte[] {command});
        }
        else {
            oaeProviderState.OAECommand = new OAECommandInfo(29,0);
            mCaller.send(new byte[] {0, CMD_WRITE_CMOS, 11, 0, 0, 1,0,0});
        }

        oaeProviderState.state = OAEProviderState.RequestStates.CONNECTED;
    }

     class Pointers {
        public  int     PointerWrite;
        public  int     PointerRead;
        public  int     PointerLeft;
        public  int     PointerRight;
        public  int     PrintSaveMode;
        public  boolean     NamesPresent;

        public String toString() {
            return "PointerWrite " + PointerWrite + "   PointerRead " + PointerRead +
                    "   PointerLeft " + PointerLeft +  "   PointerRight " + PointerRight +
                    "   PrintSaveMode " + PrintSaveMode + "   NamesPresent " + NamesPresent;
        }
    }
}
