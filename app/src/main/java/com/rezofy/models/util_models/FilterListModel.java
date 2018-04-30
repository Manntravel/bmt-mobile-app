package com.rezofy.models.util_models;

/**
 * Created by linchpin on 4/2/16.
 */
public class FilterListModel  {
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getAirlLineName() {
        return airlLineName;
    }

    public void setAirlLineName(String airlLineName) {
        this.airlLineName = airlLineName;
    }

    private String carrier;
    private String airlLineName;

    @Override
    public boolean equals(Object obj) {
        boolean flag;
        if (obj == this) {
            flag= true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            flag= false;
        }

        FilterListModel guest = (FilterListModel) obj;
        if(this.carrier.equals(guest.carrier))    //this.airlLineName.equals(guest.airlLineName) &&
        {
            flag= true;
        }
        else
        {
            flag=  false;
        }


        return flag;

    }

    @Override
    public int hashCode() {
        //Log.d("Trip","inside hashcode");
        return 1;
    }




}
