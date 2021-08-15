package com.lucid.OAE;


public class DPTestFrequency extends Frequency {
    int L1;      // stimulus frequency 1 sound pressure (dB SPL / 256 units)
    int L2;      // stimulus frequency 2 sound pressure (dB SPL / 256 units)
    char    Ear;
    char TestIndicator;

    public int getL1() {
        return L1;
    }

    public int getL2() {
        return L2;
    }

    public char getEar() {
        return Ear;
    }

    public char getTestIndicator() {
        return TestIndicator;
    }
}

