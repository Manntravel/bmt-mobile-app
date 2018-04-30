package com.rezofy.models;

import java.io.Serializable;

/**
 * Created by linchpin on 7/2/17.
 */

public class DocumentTypes implements Serializable {

    String type;
    int icon;
    boolean checked;
    int count;

    public DocumentTypes(String type, int icon, boolean checked) {
        this.type = type;
        this.icon = icon;
        this.checked = checked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
