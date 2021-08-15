package com.lucid.OAE;

class OAEProviderState {
    public enum RequestStates {
        POLLING,
        CONNECTED,
        OBTAINING_VERSION,
        OBTAINING_BRAND_ID,
        OBTAINING_POINTERS,
        OBTAINING_TEST_SECTORS,
        POWERED_OFF,
        BLE_GET_VERSIONS,
        BLE_BEGIN_AUTOSTART,
        BLE_BEGIN_CALIBRATION,
        BLE_BEGIN_DP_FREQ_TEST,
        BLE_CANCEL,
        BLE_FLAG_FOR_ERASE,
        BLE_POWER_OFF,
        RECEIVING_AXES_DISPLAY_DATA,
        RECEIVING_DP_FREQ_DISPLAY_DATA

    }

    RequestStates state;

    int[] testSectors = new int[0];

    int currentTestSectorIndex;
    int FirmwareVersion = 0;
    OAECommandInfo OAECommand;

    public OAEProviderState() {
        state = RequestStates.POWERED_OFF;
        OAECommand = new OAECommandInfo(0, 0);
    }

    @Override
    public String toString() {
        return "OAEProviderState{" +
                "state=" + state +
                ", testSectors count" + testSectors.length +
                ", currentTestSectorIndex=" + currentTestSectorIndex +
                ", FirmwareVersion=" + FirmwareVersion +
                ", OAECommand=" + OAECommand +
                '}';
    }
}
