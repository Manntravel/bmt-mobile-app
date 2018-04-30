package com.rezofy.models.response_models;

import com.rezofy.models.MyDocument;

import java.util.ArrayList;

/**
 * Created by anuj on 23/2/17.
 */

public class UserDocumentList {

    private ArrayList<MyDocument> userDocumentList;

    public ArrayList<MyDocument> getUserDocumentList() {
        return userDocumentList;
    }

    public void setUserDocumentList(ArrayList<MyDocument> userDocumentList) {
        this.userDocumentList = userDocumentList;
    }
}
