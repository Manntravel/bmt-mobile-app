package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Total implements Serializable
{
   private  Price   price;
   private  String  conversionRate;

   public Price getPrice() {
      return price;
   }

   public void setPrice(Price price) {
      this.price = price;
   }

   public String getConversionRate() {
      return conversionRate;
   }

   public void setConversionRate(String conversionRate) {
      this.conversionRate = conversionRate;
   }
}
