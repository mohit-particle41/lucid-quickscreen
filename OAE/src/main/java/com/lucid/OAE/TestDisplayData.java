package com.lucid.OAE;

public class TestDisplayData {
    byte    GraphType;              // 2 = DP Test, 3 = TE Test
    byte    NumberOfFrequencies;    // Muber of frequencies for this test
    boolean NormsMode;              // Instrument is set to draw Boys Town Norms if true
    byte    GraphMode;              // Instrument is set to draw:
                                    //  LSB = 0 SNR Graph, LSB = 1 Value Graph
                                    //  MSB = 1 Review both ears on the same graph
    String[] FrequencyLabels;       // Frequency Labels (kHz)
    int     P1;                     // Target Pressure for F1 (dB SPL in 1/256 units)
    int     P2;                     // Target Pressure for F2 (dB SPL in 1/256 units)
    protected char    Ear;
    // int     MinPassSettings;     // Not Implemented
    TestDisplayData() {
        FrequencyLabels = new String[12];
    }
    public String toString()  {
        StringBuilder sb = new StringBuilder(200);
        try {
            sb.append(String.format("*** TestDisplayData: %d frequencies: ", NumberOfFrequencies ));
            for (int i = 0; i < NumberOfFrequencies; i++) {
                sb.append(FrequencyLabels[i]).append(" ");
            }
            sb.append(" kHz, Norms ").append(NormsMode? "On, ": "Off, ");
            sb.append("Review Both Ears ").append((GraphMode & 0x80 ) != 0 ? "On, ": "Off, ");
            sb.append((GraphMode & 0x01) != 0 ? "Value Graph, ": "SNR Graph, ");
            sb.append(String.format("Target Pressures %.1f, %.1f dB SPL, Ear %c", P1/256., P2/256., Ear));
            return sb.toString();
        }
        catch (Exception e) {
          String m = e.getMessage();
        } finally {
            return sb.toString();
        }
    }

    public byte getGraphType() {
        return GraphType;
    }

    public byte getNumberOfFrequencies() {
        return NumberOfFrequencies;
    }

    public boolean isNormsMode() {
        return NormsMode;
    }

    public byte getGraphMode() {
        return GraphMode;
    }

    public String[] getFrequencyLabels() {
        return FrequencyLabels;
    }

    public int getP1() {
        return P1;
    }

    public int getP2() {
        return P2;
    }

    public char getEar() {
        return Ear;
    }
}
