package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class TravellerFare implements Serializable
{
   private  AdtInfChd     ADT;
   private  AdtInfChd     INF;
   private  AdtInfChd     CHD;

   public AdtInfChd getADT() {
      return ADT;
   }

   public void setADT(AdtInfChd ADT) {
      this.ADT = ADT;
   }

   public AdtInfChd getINF() {
      return INF;
   }

   public void setINF(AdtInfChd INF) {
      this.INF = INF;
   }

   public AdtInfChd getCHD() {
      return CHD;
   }

   public void setCHD(AdtInfChd CHD) {
      this.CHD = CHD;
   }
}
