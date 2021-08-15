package com.lucid.OAE;

public class DPTestFrequencyDisplay extends DPTestFrequency {
    int AcceptRatio;
    int Noise;
    byte index;
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(String.format("** DPDisplayData: Freq %s kHz, index %d, ", FrequencyLabel, index ));
        sb.append(String.format("L1 %.1f, L2 %.1f, DP %.1f, SNR %.1f, Computed NF %.1f, Measured NF %.1f",
                L1 /256., L2 /256., DPTEEmissions/256.,SNR/256.,NF/256., Noise/256.));
        sb.append(String.format(" result '%c', ear '%c'", TestIndicator, Ear));

        return sb.toString();
    }

    public int getAcceptRatio() {
        return AcceptRatio;
    }

    public int getNoise() {
        return Noise;
    }

    public byte getIndex() {
        return index;
    }
}

