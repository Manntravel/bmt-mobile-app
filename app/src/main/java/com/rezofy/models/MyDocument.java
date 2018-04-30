package com.rezofy.models;

import java.io.Serializable;

/**
 * Created by linchpin on 6/2/17.
 */
public class MyDocument implements Serializable{

   private int _id;
   private String id;
   private String userId;
   private String documentFileName;
   private String fileType;
   private String name;
   private String createdOn;
   private String type;
   private String path;

    public MyDocument(String name, String createdOn, String type, String path) {
        this.name = name;
        this.createdOn = createdOn;
        this.type = type;
        this.path = path;
    }

    public MyDocument(int _id, String name, String createdOn, String type, String path, String serverId) {
        this._id = _id;
        this.name = name;
        this.createdOn = createdOn;
        this.type = type;
        this.path = path;
        this.id = serverId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
