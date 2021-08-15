package com.lucid.OAE;

import java.util.Date;

public class OAETest {
    private static final String TAG = OAETest.class.getSimpleName();
    protected char Ear;               // R or L
    protected int TestType;    // -1=TE, 1=DP, >=2 DP High Frequency
    private int PassSNR;            // feature not supported at this time
    private int PassFreq;           // feature not supported at this time
    private int TestOutcome;        // with Lucid Screener, not to be reported to user
                            // -4 can not obtain pressure
                            // -3 seal error
                            // 0 REFER
                            // 1 PASS
    int FrequencySet;       // feature not supported at this time
    protected Date TestDate;
    protected String ProtocolName;
    int ParmChangeFlags;    // feature not supported at this time
    protected int MinPassSetting;     // feature not supported at this time
    private int     Flags;          // feature not supported at this time
    protected int     ReverseFrequencyFlag;    // feature not supported at this time
    protected int     AutostopFlag;            // feature not supported at this time
    protected int     TestNumber;              // sequential number, starts from 0 (shown as 1 in UI)
    private int     TAvg;                   // feature not supported at this time
    protected String     FWVersion;    // Major Version*100 + Minor Version
    protected String     InstrumentSerial;
    protected String     ProbeSerial;
    protected int     NumberFrequencies;       // count of frequencies within protocol
    protected int     RecordVersion;           // only 210 supported

    private byte[] SectorBytes = new byte[517];                 // image of DSP sector, in 24 bit words
    protected static byte[] CommandResponse = new byte[515];    // response buffer

    public String ToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nTest # ").append(TestNumber + 1).append("\nDate ").append(TestDate).append(" \nFirmware ").append(FWVersion);

        String  TestResult = "";
        switch (TestOutcome) {
            case (1): TestResult = "PASS"; break;
            case (0): TestResult = "REFER"; break;
            case (-4): TestResult = "No Pressure"; break;
            case (-3): TestResult = "No Seal"; break;
            case (-2): TestResult = "Error"; break;
        }

        sb.append("\nProtocol ").append(ProtocolName).append("\nResult (* do not display in UI *): ").append(TestResult).append("\n");
        sb.append("Ear ").append(Ear).append("\nInstrument Serial ").append(InstrumentSerial).append("  Probe Serial ").append(ProbeSerial).append("\n\n");

        if (this instanceof DPTest) {
            sb.append(String.format("%-6.6s%-6.6s%-6.6s%-6.6s%-6.6s%-6.6s\n\n" , "F2" , "L1" , "L2" , "DP" , "NF" ,"SNR"));
            for (DPTestFrequency dpTestFrequency : (((DPTest) this).getFrequencies())) {
                double dp = Double.parseDouble(String.format("%-6.1f", dpTestFrequency.DPTEEmissions / 256.));
                double snr = Double.parseDouble(String.format("%6.1f", dpTestFrequency.SNR / 256.));
                String nftoprint = String.format("%6.1f", dp - snr);

                sb.append(dpTestFrequency.FrequencyLabel).append(String.format("%6.0f", dpTestFrequency.L1 / 256.)).append(String.format("%6.0f", dpTestFrequency.L2 / 256.)).append(String.format("%6.1f", dpTestFrequency.DPTEEmissions / 256.)).append(nftoprint).append(String.format("%6.1f", dpTestFrequency.SNR / 256.)).append("    ").append(dpTestFrequency.Result).append("\n");

            }
        } else { // TE is not used with Lucid screener
            sb.append("  F       L    TE   NF   SNR \n");
            for (TETestFrequency teTestFrequency : (((TETest) this).Frequencies)) {
                sb.append(teTestFrequency.FrequencyLabel).append(" ")
                        .append(teTestFrequency.TEPeak).append(" ")
                        .append(teTestFrequency.DPTEEmissions / 256.)
                        .append(" ").append(teTestFrequency.NF / 256.)
                        .append(" ").append(teTestFrequency.SNR / 256.)
                        .append("    ").append(teTestFrequency.Result)
                        .append("\n");
            }
        }

        sb.append("\n");
        return sb.toString();
    }

    public char getEar() {
        return Ear;
    }

    public int getTestType() {
        return TestType;
    }

    public int getPassSNR() {
        return PassSNR;
    }

    public int getPassFreq() {
        return PassFreq;
    }

    public int getTestOutcome() {
        return TestOutcome;
    }

    public Date getTestDate() {
        return TestDate;
    }

    public String getProtocolName() {
        return ProtocolName;
    }

    public int getMinPassSetting() {
        return MinPassSetting;
    }

    public int getFlags() {
        return Flags;
    }

    public int getReverseFrequencyFlag() {
        return ReverseFrequencyFlag;
    }

    public int getAutostopFlag() {
        return AutostopFlag;
    }

    public int getTestNumber() {
        return TestNumber;
    }

    public int getTAvg() {
        return TAvg;
    }

    public String getFWVersion() {
        return FWVersion;
    }

    public String getInstrumentSerial() {
        return InstrumentSerial;
    }

    public String getProbeSerial() {
        return ProbeSerial;
    }

    public int getNumberFrequencies() {
        return NumberFrequencies;
    }

    public int getRecordVersion() {
        return RecordVersion;
    }
}

