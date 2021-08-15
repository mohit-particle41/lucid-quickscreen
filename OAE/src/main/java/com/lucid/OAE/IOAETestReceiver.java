package com.lucid.OAE;

import java.io.IOException;
import java.util.ArrayList;

public interface IOAETestReceiver {
    void send(byte[] data) throws IOException;               // send data to instrument
    void OnBulkTestResultsReady(ArrayList<OAETest> testResults); // receive an ArrayList of test results (from flash memory)
    void OnRealTestDisplayInit(TestDisplayData testDisplayData);  // receive TestDisplayData (in real time)
    void OnRealTimeTestResultReady(DPTestFrequencyDisplay dpTestFrequencyDisplay);  // receive DPTestFrequencyDisplay (in real time)
    void DisplayMessage(String message);           // pass a message to the user using the UI thread
    void OnPollDetected(boolean PollDetected);
    boolean isBLE();                                // BLE devices use different interface to the instrument
}
