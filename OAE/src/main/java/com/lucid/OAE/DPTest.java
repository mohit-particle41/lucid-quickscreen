   package com.lucid.OAE;

import java.util.ArrayList;

public class DPTest extends OAETest {
    private int TargetP1;               // feature not supported at this time
    private int TargetP2;               // feature not supported at this time
    private int DPBlocks;               // feature not supported at this time
    private int DPFlags;                // feature not supported at this time
    private int AutoStopActualFreqsRun; // feature not supported at this time
    protected ArrayList<DPTestFrequency> Frequencies;

    public int getTargetP1() {
        return TargetP1;
    }

    public int getTargetP2() {
        return TargetP2;
    }

    public int getDPBlocks() {
        return DPBlocks;
    }

    public int getDPFlags() {
        return DPFlags;
    }

    public int getAutoStopActualFreqsRun() {
        return AutoStopActualFreqsRun;
    }

    public ArrayList<DPTestFrequency> getFrequencies() {
        return Frequencies;
    }
}

