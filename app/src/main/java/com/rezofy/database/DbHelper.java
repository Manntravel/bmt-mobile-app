package com.rezofy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rezofy.models.GstDBModel;
import com.rezofy.models.MyDocument;
import com.rezofy.models.response_models.CountryTours;
import com.rezofy.models.response_models.SelectedCountryTours;
import com.rezofy.models.util_models.TravellerDBModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linchpin on 12/2/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper dbHelper;
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String SEARCH_TABLE_NAME = "search_table";
    public static final String _id = "_id";
    public static final String originCityName = "originCityName";
    public static final String destCityName = "destCityName";
    public static final String originCityCode = "originCityCode";
    public static final String destCityCode = "destCityCode";
    public static final String departDate = "departDate";
    public static final String retDate = "retDate";
    public static final String noOfAdults = "noOfAdults";
    public static final String noOfChildren = "noOfChildren";
    public static final String noOfInfants = "noOfInfants";
    public static final String originAirportName = "originAirportName";
    public static final String destAirportName = "destAirportName";
    public static final String originCountryName = "originCountryName";
    public static final String destCountryName = "destCountryName";
    public static final String searchType = "searchType";
    public static final String timeStamp = "timeStamp";

    public static final String CREATE_TABLE = "create table search_table ( _id INTEGER PRIMARY KEY   AUTOINCREMENT,"
            + "originCityName TEXT,destCityName TEXT,originCityCode TEXT, destCityCode TEXT,departDate TEXT,retDate TEXT,noOfAdults INTEGER,"
            + "noOfChildren INTEGER,noOfInfants INTEGER,originAirportName TEXT,destAirportName TEXT,originCountryName TEXT,destCountryName TEXT,searchType TEXT,timeStamp TEXT)";


    public static final String GST_DETAILS_TABLE = "gst_details_table";
    public static final String gstId = "_id";
    public static final String gstCompanyName = "companyName";
    public static final String gstNo = "gstNo";
    public static final String gstBillingPhone = "billingPhone";
    public static final String gstEmail = "email";
    public static final String gstCountry = "country";
    public static final String gstState = "state";
    public static final String gstCity = "city";
    public static final String gstAddress = "address";
    public static final String gstPincode = "pincode";
    public static final String gstTimeStamp = "timeStamp";

    public static final String CREATE_GST_DETAILS_TABLE = "create table "+GST_DETAILS_TABLE+" ( "+gstId+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            gstCompanyName+ " TEXT, "+gstNo+" TEXT, "+gstBillingPhone+" TEXT, "+gstEmail+" TEXT, "+gstCountry+" TEXT, "+gstState+" TEXT, " +
            gstCity+" TEXT, "+gstAddress+" TEXT, "+gstPincode+" TEXT, "+gstTimeStamp+" TEXT)";

    //for Passenger Table
    public static final String PASSENGER_TABLE_NAME = "passenger_table";
    public static final String pas_id = "_id";
    public static final String pas_first_name = "first_name";
    public static final String pas_last_name = "last_name";
    public static final String pas_gender = "gender";
    public static final String pas_meal = "meal";
    public static final String pas_dob = "dob";
    public static final String pas_resident_country = "country";
    public static final String pas_passport_no = "passport_no";
    public static final String pas_passport_issued_by = "passport_issued_by";
    public static final String pas_passport_expiration_date = "passport_expiration_date";
    public static final String pas_passenger_type = "passenger_type";
    public static final String pas_timeStamp = "timeStamp";


    public static final String CREATE_PASSENGER_TABLE =
            "CREATE TABLE " + PASSENGER_TABLE_NAME
                    + " ( "
                    + pas_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + pas_first_name + " TEXT, "
                    + pas_last_name + " TEXT, "
                    + pas_gender + " TEXT, "
                    + pas_meal + " TEXT, "
                    + pas_dob + " TEXT, "
                    + pas_resident_country + " TEXT, "
                    + pas_passport_no + " TEXT, "
                    + pas_passport_issued_by + " TEXT, "
                    + pas_passport_expiration_date + " TEXT, "
                    + pas_passenger_type + " TEXT, "
                    + pas_timeStamp + " TEXT"
                    + ")";

    //MyDocument Table Fields
    public static final String DOCUMENT_TABLE_NAME = "document_table";
    public static final String DOCUMENT_NAME = "document_name";
    public static final String UPLOAD_DATE = "upload_date";
    public static final String DOCUMENT_TYPE = "document_type";
    public static final String DOCUMENT_SERVER_ID = "document_server_id";
    public static final String DOCUMENT_PATH = "document_path";

    public static final String CREATE_DOCUMENT_TABLE =
            "CREATE TABLE " + DOCUMENT_TABLE_NAME
                    + " ( "
                    + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DOCUMENT_NAME + " TEXT, "
                    + UPLOAD_DATE + " TEXT, "
                    + DOCUMENT_TYPE + " TEXT, "
                    + DOCUMENT_SERVER_ID + " TEXT, "
                    + DOCUMENT_PATH + " TEXT "
                    + ")";


    //for favourite table
    public static final String FAVORITE_TABLE_NAME = "favorite_table";
    public static final String FAV_ID = "fav_id";
    public static final String FAV_NAME = "fav_name";
    public static final String FAV_DURATION = "fav_duration";
    public static final String FAV_PRICE = "fav_price";
    public static final String FAV_CITY = "fav_city";
    public static final String FAV_THUMBNAIL = "fav_thumbnail";
    public static final String FAV_PHONE = "fav_phone";

    public static final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + FAVORITE_TABLE_NAME
            + " ( "
            + FAV_ID + " INTEGER PRIMARY KEY, "
            + FAV_NAME + " TEXT, "
            + FAV_DURATION + " TEXT, "
            + FAV_PRICE + " TEXT, "
            + FAV_CITY + " TEXT, "
            + FAV_THUMBNAIL + " TEXT, "
            + FAV_PHONE + " TEXT"
            + ")";

            ;// =
//            "CREATE TABLE " + FAVORITE_TABLE_NAME
//                    + " ( "
//                    + FAV_ID + " INTEGER PRIMARY KEY, "
//                    + FAV_NAME + " TEXT, "
//                    + FAV_DURATION + " TEXT, "
//                    + FAV_PRICE + " TEXT, "
//                    + FAV_CITY + " TEXT, "
//                    + FAV_THUMBNAIL + " TEXT, "
//                    + FAV_PHONE + " TEXT"
//                    + ")";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //  this.context = context;

    }

    public void deleteDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public static DbHelper getInstance (Context context) {

        if(dbHelper == null) {
            synchronized (DbHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DbHelper(context);
                }
            }
        }
        return dbHelper;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Trip", "inside onCreate stmt is " + CREATE_TABLE);
        Log.d("Trip", "inside onCreate stmt is " + CREATE_PASSENGER_TABLE);
        Log.d("Trip", "inside onCreate stmt is " + CREATE_FAVORITE_TABLE);

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_PASSENGER_TABLE);
        db.execSQL(CREATE_DOCUMENT_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_GST_DETAILS_TABLE);

        //db.execSQL("CREATE TABLE favorite_table ( fav_id INTEGER PRIMARY KEY, fav_name TEXT, fav_duration TEXT, fav_price TEXT, fav_city TEXT, fav_thumbnail TEXT, fav_phone TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long insertDocument(MyDocument document)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DOCUMENT_NAME, document.getName());
        contentValues.put(DOCUMENT_TYPE, document.getType());
        contentValues.put(UPLOAD_DATE, document.getCreatedOn());
        contentValues.put(DOCUMENT_SERVER_ID, document.getId());
        contentValues.put(DOCUMENT_PATH, document.getPath());
        long insertRowId = db.insertWithOnConflict(DOCUMENT_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return insertRowId;
    }

    public List<MyDocument> getAllDocument(String type)
    {
        List<MyDocument> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DOCUMENT_TABLE_NAME, null,DOCUMENT_TYPE+"=?",new String[]{type},null, null, null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                   // public MyDocument(int _id, String name, String date, String type, String path, int icon)
                    MyDocument document = new MyDocument(cursor.getInt(cursor.getColumnIndex(_id)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_NAME)),
                            cursor.getString(cursor.getColumnIndex(UPLOAD_DATE)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_TYPE)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_PATH)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_SERVER_ID)));
                    list.add(document);

                }while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return list;
    }

    public List<MyDocument> getAllDocument()
    {
        List<MyDocument> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DOCUMENT_TABLE_NAME, null, null, null,null, null, null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    // public MyDocument(int _id, String name, String date, String type, String path, int icon)
                    MyDocument document = new MyDocument(cursor.getInt(cursor.getColumnIndex(_id)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_NAME)),
                            cursor.getString(cursor.getColumnIndex(UPLOAD_DATE)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_TYPE)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_PATH)),
                            cursor.getString(cursor.getColumnIndex(DOCUMENT_SERVER_ID)));
                    list.add(document);

                }while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return list;
    }

    public int getDocumentCount(String type)
    {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DOCUMENT_TABLE_NAME, null,DOCUMENT_TYPE+"=?",new String[]{type},null, null, null);
       int count = 0;
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {

                   count++;
                }while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return count;
    }


    public void insertOrUpdateData(String originCityName, String destCityName, String originCityCode, String destCityCode, String departDate,
                                   String retDate, int noOfAdults, int noOfChildren, int noOfInfants, String originAirportName, String destAirportName,
                                   String originCountryName, String destCountryName, String searchType, String timeStamp, SQLiteDatabase db) {

        if (retDate == null)
            retDate = "";

        String query = "Select * from " + SEARCH_TABLE_NAME + " where " + this.originCityCode + "='" + originCityCode + "' and " + this.destCityCode + "='" + destCityCode + "' and " + this.searchType + "='" + searchType + "' and " + this.departDate + "='" + departDate + "' and " + this.retDate + "='" + retDate + "'";

        Log.d("Trip", "where query is " + query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.originCityName, originCityName);
            contentValues.put(this.destCityName, destCityName);
            contentValues.put(this.originCityCode, originCityCode);
            contentValues.put(this.destCityCode, destCityCode);
            contentValues.put(this.departDate, departDate);
            contentValues.put(this.retDate, retDate);
            contentValues.put(this.noOfAdults, noOfAdults);
            contentValues.put(this.noOfChildren, noOfChildren);
            contentValues.put(this.noOfInfants, noOfInfants);
            contentValues.put(this.originAirportName, originAirportName);
            contentValues.put(this.destAirportName, destAirportName);
            contentValues.put(this.originCountryName, originCountryName);
            contentValues.put(this.destCountryName, destCountryName);
            contentValues.put(this.searchType, searchType);
            contentValues.put(this.timeStamp, timeStamp);
            db.insert(SEARCH_TABLE_NAME, null, contentValues);

        } else if (cursor != null && cursor.getCount() == 1) {
            String whereArg[] = new String[5];
            whereArg[0] = originCityCode;
            whereArg[1] = destCityCode;
            whereArg[2] = searchType;
            whereArg[3] = departDate;
            whereArg[4] = retDate;

            ContentValues contentValues = new ContentValues();
            contentValues.put(this.noOfAdults, noOfAdults);
            contentValues.put(this.noOfChildren, noOfChildren);
            contentValues.put(this.noOfInfants, noOfInfants);
            contentValues.put(this.timeStamp, timeStamp);
            int updatedrows = db.update(SEARCH_TABLE_NAME, contentValues, this.originCityCode + "=? and " + this.destCityCode + "=? and " + this.searchType + "=? and " + this.departDate + "=? and " + this.retDate + "=?", whereArg);
            Log.d("Trip", "updated rows are " + updatedrows);
        }
        cursor.close();
    }

    public Cursor getData(SQLiteDatabase db, String SELECT_QUERY) {
        Cursor res = null;
        try {


            res = db.rawQuery(SELECT_QUERY, null);


        } catch (Exception e) {
            Log.d("Trip", "Eror in getData" + e);
        } finally {
            //Log.d("Trip","size of cursor is "+res.getCount());
            return res;
        }
    }


    public List<TravellerDBModel> getTravellerData(SQLiteDatabase db, String SELECT_QUERY) {
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        List<TravellerDBModel> travellerDBModelList = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                TravellerDBModel travellerDBModel = new TravellerDBModel();
                travellerDBModel.setFirstName(cursor.getString(cursor.getColumnIndex(DbHelper.pas_first_name)));
                travellerDBModel.setLastName(cursor.getString(cursor.getColumnIndex(DbHelper.pas_last_name)));
                travellerDBModel.setGender(cursor.getString(cursor.getColumnIndex(DbHelper.pas_gender)));
                travellerDBModel.setMealPref(cursor.getString(cursor.getColumnIndex(DbHelper.pas_meal)));
                travellerDBModel.setDob(cursor.getString(cursor.getColumnIndex(DbHelper.pas_dob)));
                travellerDBModel.setPassportNo(cursor.getString(cursor.getColumnIndex(DbHelper.pas_passport_no)));
                travellerDBModel.setIssuedBy(cursor.getString(cursor.getColumnIndex(DbHelper.pas_passport_issued_by)));
                travellerDBModel.setCountry(cursor.getString(cursor.getColumnIndex(DbHelper.pas_passport_issued_by)));
                travellerDBModel.setExpDate(cursor.getString(cursor.getColumnIndex(DbHelper.pas_passport_expiration_date)));
                travellerDBModel.setPassengerType(cursor.getString(cursor.getColumnIndex(DbHelper.pas_passenger_type)));
                travellerDBModelList.add(travellerDBModel);
            }
            cursor.close();
        }
        return travellerDBModelList;
    }

    public void updateTimeStamp(String originCityName, String destCityName, String type, String timeStamp, SQLiteDatabase db) {
        try {
            String whereArg[] = new String[3];
            whereArg[0] = originCityName;
            whereArg[1] = destCityName;
            whereArg[2] = type;
            ContentValues values = new ContentValues();
            values.put(this.timeStamp, timeStamp);
            int rows = db.update(SEARCH_TABLE_NAME, values, this.originCityName + " =? and " + this.destCityName + " =? and " + this.searchType + " =?", whereArg);
            Log.d("Trip", "updated rows are " + rows);
//            String updateQuery = "Update "+SEARCH_TABLE_NAME+" set "+this.timeStamp+" = '"+timeStamp+"' where "+this.originCityName+"='"+originCityName+"' and "+this.destCityName+"='"+destCityName+"' and "+this.searchType+"='"+type+"'";
//            db.rawQuery(updateQuery,null);

        } catch (Exception e) {
            Log.d("Trip", "Eror in updateTimeStamp " + e);
        }
    }

    public void insertDatatoPassengerTable(String pas_first_name, String pas_last_name, String pas_gender, String pas_meal, String pas_dob,
                                           String pas_resident_country, String pas_passport_no, String pas_passport_issued_by, String pas_passport_expiration_date,
                                           String pas_passenger_type, String pas_timeStamp, SQLiteDatabase db) {
        try {
            Log.d("Trip", "inside insert " + pas_first_name + " ::: " + pas_gender + " :: " + pas_dob);
            String str = ifRecordExist(pas_first_name, pas_last_name, pas_gender, pas_meal, pas_dob,
                    pas_resident_country, pas_passport_no, pas_passport_issued_by, pas_passport_expiration_date,
                    pas_passenger_type, pas_timeStamp, db);

            if (str.equals("insert")) {
                ContentValues values = new ContentValues();
                values.put(this.pas_first_name, pas_first_name);
                values.put(this.pas_last_name, pas_last_name);
                values.put(this.pas_gender, pas_gender);
                values.put(this.pas_meal, pas_meal);
                values.put(this.pas_dob, pas_dob);
                values.put(this.pas_resident_country, pas_resident_country);
                values.put(this.pas_passport_no, pas_passport_no);
                values.put(this.pas_passport_issued_by, pas_passport_issued_by);
                values.put(this.pas_passport_expiration_date, pas_passport_expiration_date);
                values.put(this.pas_passenger_type, pas_passenger_type);
                values.put(this.pas_timeStamp, pas_timeStamp);
                db.insert(PASSENGER_TABLE_NAME, null, values);
            } else if (str.equals("updatedobandmeal")) {
                String whereclause[] = new String[3];
                whereclause[0] = pas_first_name;
                whereclause[1] = pas_last_name;
                whereclause[2] = pas_gender;
                ContentValues values = new ContentValues();
                values.put(this.pas_dob, pas_dob);
                values.put(this.pas_meal, pas_meal);
                values.put(this.pas_timeStamp, pas_timeStamp);
                db.update(PASSENGER_TABLE_NAME, values, this.pas_first_name + "=? and " + this.pas_last_name + "=? and " + this.pas_gender + "=?", whereclause);

            } else if (str.equals("updateall")) {
                String whereclause[] = new String[3];
                whereclause[0] = pas_first_name;
                whereclause[1] = pas_last_name;
                whereclause[2] = pas_gender;
                ContentValues values = new ContentValues();
                values.put(this.pas_first_name, pas_first_name);
                values.put(this.pas_last_name, pas_last_name);
                values.put(this.pas_gender, pas_gender);
                values.put(this.pas_meal, pas_meal);
                values.put(this.pas_dob, pas_dob);
                values.put(this.pas_resident_country, pas_resident_country);
                values.put(this.pas_passport_no, pas_passport_no);
                values.put(this.pas_passport_issued_by, pas_passport_issued_by);
                values.put(this.pas_passport_expiration_date, pas_passport_expiration_date);
                values.put(this.pas_passenger_type, pas_passenger_type);
                values.put(this.pas_timeStamp, pas_timeStamp);
                db.update(PASSENGER_TABLE_NAME, values, this.pas_first_name + "=? and " + this.pas_last_name + "=? and " + this.pas_gender + "=?", whereclause);

            }


        } catch (Exception e) {
            Log.d("Trip", "Eror in insertDatatoPassengerTable " + e);
        }

    }

    private String ifRecordExist(String pas_first_name, String pas_last_name, String pas_gender, String pas_meal, String pas_dob,
                                 String pas_resident_country, String pas_passport_no, String pas_passport_issued_by, String pas_passport_expiration_date,
                                 String pas_passenger_type, String pas_timeStamp, SQLiteDatabase db) {
        String str = "";
        try {
            String query = "Select * from " + PASSENGER_TABLE_NAME + " where " + this.pas_first_name + "='" + pas_first_name + "' and " + this.pas_last_name + "='" + pas_last_name + "' and " + this.pas_gender + "='" + pas_gender + "'";
//                    +"' and "+this.pas_meal+"='"+pas_meal+"' and "+this.pas_dob+"='"+pas_dob+"' and "+this.pas_resident_country+"='"+pas_resident_country+"' and "+this.pas_passport_no+"='"+pas_passport_no
//            +"' and "+this.pas_passport_issued_by+"='"+pas_passport_issued_by+"' and "+this.pas_passport_expiration_date+"='"+pas_passport_expiration_date+"'";

            Log.d("Trip", "where query is " + query);
            Cursor csr = db.rawQuery(query, null);
            if (csr != null && csr.getCount() > 0) {
                csr.moveToFirst();
                String db_dob = csr.getString(csr.getColumnIndex(this.pas_dob));
                if (db_dob.equals(pas_dob)) {
                    str = "updateall";

                } else {
                    if (db_dob.equals("")) {
                        str = "updateall";
                    } else if (pas_dob.equals("")) {
                        str = "updatedobandmeal";
                    }
                }

            } else {
                str = "insert";
            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in ifRecordExist " + e);
        } finally {
            return str;
        }
    }

    public void insertDatatoFavoriteTable(String fav_id, String fav_name, String fav_duration, String fav_price, String fav_city,
                                           String fav_thumbnail, String fav_phone) {

        SQLiteDatabase db = getWritableDatabase();

        try {
                ContentValues values = new ContentValues();
                values.put(FAV_ID, fav_id);
                values.put(FAV_NAME, fav_name);
                values.put(FAV_DURATION, fav_duration);
                values.put(FAV_PRICE, fav_price);
                values.put(FAV_CITY, fav_city);
                values.put(FAV_THUMBNAIL, fav_thumbnail);
                values.put(FAV_PHONE, fav_phone);
                db.insert(FAVORITE_TABLE_NAME, null, values);

        } catch (Exception e) {
            Log.d("Trip", "Eror in insertDatatoPassengerTable " + e);
        }

    }

    public SelectedCountryTours getFavPackages() {
        SelectedCountryTours selectedCountryTours = new SelectedCountryTours();
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<CountryTours> countryToursArrayList = new ArrayList<CountryTours>();
        Cursor cursor = db.rawQuery("select * from " + FAVORITE_TABLE_NAME, null);
        if(cursor != null && cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                CountryTours favoritePackages = new CountryTours();
                favoritePackages.setID(cursor.getString(0));
                favoritePackages.setName(cursor.getString(1));
                favoritePackages.setDuration(cursor.getString(2));
                favoritePackages.setStarting_price(cursor.getString(3));
                favoritePackages.setCity_name(cursor.getString(4));
                favoritePackages.setThumbnail(cursor.getString(5));
                favoritePackages.setPhonenumber(cursor.getString(6));
                countryToursArrayList.add(favoritePackages);
            }
        selectedCountryTours.setCountryToursArrayList(countryToursArrayList);
        cursor.close();

        return selectedCountryTours;
    }

    public boolean isPackageFav(String id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<CountryTours> countryToursArrayList = new ArrayList<CountryTours>();
        Cursor cursor = db.rawQuery("select * from " + FAVORITE_TABLE_NAME + " where " + FAV_ID + " = " + id, null);
        if(cursor.getCount() > 0)
            return true;
        else return false;
    }

    public boolean deleteRow(String package_id)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(FAVORITE_TABLE_NAME, FAV_ID + "=" + package_id, null) > 0;
    }

    public void insertDataToGstTable(String companyName, String gstNo, String billingPhone, String email, String countryName, String stateName,
                                     String cityName, String address, String pincode, String timeStamp, SQLiteDatabase db) {
        try {
            Log.d("InsertToGstTable","CompanyName::"+companyName+"::GSTNo::"+gstNo);
            String str = checkIfGstRecordExist(companyName,gstNo,billingPhone,email,countryName,stateName,cityName,address,pincode,timeStamp,db);
            if(str.equalsIgnoreCase("insert")) {
                ContentValues values = new ContentValues();
                values.put(gstCompanyName, companyName);
                values.put(this.gstNo, gstNo);
                values.put(gstBillingPhone, billingPhone);
                values.put(gstEmail, email);
                values.put(gstCountry, countryName);
                values.put(gstState, stateName);
                values.put(gstCity, cityName);
                values.put(gstAddress, address);
                values.put(gstPincode, pincode);
                values.put(gstTimeStamp, timeStamp);
                db.insert(GST_DETAILS_TABLE, null, values);
            } else if(str.equalsIgnoreCase("update")) {
                String whereclause[] = new String[2];
                whereclause[0] = companyName;
                whereclause[1] = gstNo;
                ContentValues values = new ContentValues();
                values.put(gstCountry, countryName);
                values.put(gstState, stateName);
                values.put(gstCity, cityName);
                values.put(gstAddress, address);
                values.put(gstPincode, pincode);
                values.put(gstBillingPhone, billingPhone);
                values.put(gstTimeStamp, timeStamp);
                db.update(GST_DETAILS_TABLE, values, gstCompanyName + "=? and " + this.gstNo + "=?", whereclause);
            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in insertDataToGstTable " + e);
        }
    }

    public String checkIfGstRecordExist(String companyName, String gstNo, String billingPhone, String email, String countryName, String stateName,
                                        String cityName, String address, String pincode, String timeStamp, SQLiteDatabase db) {
        String str = "";
        try {
            String query = "Select * from "+GST_DETAILS_TABLE+" where "+gstCompanyName+" ='"+companyName+"' and gstNo = '"+gstNo+"';";
            Cursor csr = db.rawQuery(query, null);

            if (csr != null && csr.getCount() > 0) {
                csr.moveToFirst();
                String country = csr.getString(csr.getColumnIndex(gstCountry));
                if (country.equals(companyName)) {
                    str = "doNothing";
                } else {
                    str = "update";
                }

            } else {
                str = "insert";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public List<GstDBModel> getGstData(SQLiteDatabase db, String SELECT_QUERY) {
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        List<GstDBModel> gstDBModelList = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                GstDBModel gstDBModel = new GstDBModel();
                gstDBModel.setAddress(cursor.getString(cursor.getColumnIndex(gstAddress)));
                gstDBModel.setBillingPhone(cursor.getString(cursor.getColumnIndex(gstBillingPhone)));
                gstDBModel.setCity(cursor.getString(cursor.getColumnIndex(gstCity)));
                gstDBModel.setCompanyName(cursor.getString(cursor.getColumnIndex(gstCompanyName)));
                gstDBModel.setCountry(cursor.getString(cursor.getColumnIndex(gstCountry)));
                gstDBModel.setEmail(cursor.getString(cursor.getColumnIndex(gstEmail)));
                gstDBModel.setGstNo(cursor.getString(cursor.getColumnIndex(this.gstNo)));
                gstDBModel.setPincode(cursor.getString(cursor.getColumnIndex(gstPincode)));
                gstDBModel.setState(cursor.getString(cursor.getColumnIndex(gstState)));
                gstDBModelList.add(gstDBModel);
            }
            cursor.close();
        }
        return gstDBModelList;
    }
}
