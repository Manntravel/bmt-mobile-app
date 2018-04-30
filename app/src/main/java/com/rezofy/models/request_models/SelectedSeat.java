package com.rezofy.models.request_models;

/**
 * Created by linchpin11192 on 24-Dec-2015.
 */
public class SelectedSeat {
    private int CabinType;
    private boolean exist;
    private boolean overWing;
    private boolean handicapSeat;

    public int getCabinType() {
        return CabinType;
    }

    public void setCabinType(int cabinType) {
        CabinType = cabinType;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean isOverWing() {
        return overWing;
    }

    public void setOverWing(boolean overWing) {
        this.overWing = overWing;
    }

    public boolean isHandicapSeat() {
        return handicapSeat;
    }

    public void setHandicapSeat(boolean handicapSeat) {
        this.handicapSeat = handicapSeat;
    }
}
