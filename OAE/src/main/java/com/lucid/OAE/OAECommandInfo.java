package com.lucid.OAE;

import androidx.annotation.NonNull;

public class OAECommandInfo {
    public int code;
    public int ExpectedPacketLength;
    public int AccumulatedLength;

    @NonNull
    @Override
    public String toString() {
        return ("OAECommandInfo code " + code + " ExpectedPacketLength "
                + ExpectedPacketLength + "  AccumulatedLength " + AccumulatedLength);
    }

    public OAECommandInfo(int code, int ExpectedPacketLength) {
        this.code = code;
        this.ExpectedPacketLength = ExpectedPacketLength;
        AccumulatedLength = 0;
    }
}
