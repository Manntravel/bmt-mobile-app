package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class CashCardValidationResponse implements Serializable
{
    private  CashCard cashCard;

    public CashCard getCashCard() {
        return cashCard;
    }

    public void setCashCard(CashCard cashCard) {
        this.cashCard = cashCard;
    }
}
