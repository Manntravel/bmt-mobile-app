package com.rezofy.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rezofy.models.request_models.ModelFlightSearch;

import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linchpin11192 on 29-Sep-2015.
 */
public class DatabaseController extends SQLiteOpenHelper {

    /**
     * DATABASE CONSTANTS
     */
    private static final String DATABASE_NAME = "rezofy.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";
    private static volatile DatabaseController databaseController;

    private DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseController getInstance(Context context) {
        if (databaseController == null)
            synchronized (DatabaseController.class) {
                if (databaseController == null)
                    databaseController = new DatabaseController(context);
            }
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset(context);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return databaseController;
    }

    private static final String TABLE_SERVICES = "SERVICES";
    private static final String SERVCIES_COLUMN_ID = "_ID";
    private static final String VEHICLE_ID = "VEHICLE_ID";
    private static final String ADDED_SERVICES = "ADDED_SERVICES";
    private static final String SERVICE_SCHEDULE = "SERVICE_SCHEDULE";
    private static final String TOWING = "TOWING";
    private static final String ADDRESS = "ADDRESS";
    private static final String IMAGE_PATHS = "IMAGE_PATHS";
    private static final String SERVICE_COMMENTS = "SERVICE_COMMENTS";
    private static final String QUERY_CREATE_TABLE_SERVICES =

            "CREATE TABLE " + TABLE_SERVICES
                    + " ( "
                    + SERVCIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + VEHICLE_ID + " STRING, "
                    + ADDED_SERVICES + " STRING, "
                    + SERVICE_SCHEDULE + " LONG, "
                    + TOWING + " STRING, "
                    + ADDRESS + " STRING, "
                    + IMAGE_PATHS + " STRING, "
                    + SERVICE_COMMENTS + " STRING"
                    + ");";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_TABLE_SERVICES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);

        // Create table again
        onCreate(db);
    }

    private static void copyDataBaseFromAsset(Context context) throws IOException {

        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getDatabasePath(context);

        // if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private static String getDatabasePath(Context context) {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public List<String> getCountryList() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select COUNTRY_CODE,COUNTRY_NAME  from COMP_DATA group by COUNTRY_CODE  ORDER BY COUNTRY_NAME ASC", null);
        if (cursor != null && cursor.getCount() != 0) {
            List<String> searchList = new ArrayList<>();
            while (cursor.moveToNext()) {
                searchList.add(WordUtils.capitalizeFully(cursor.getString(cursor.getColumnIndex("COUNTRY_NAME"))));
            }
            cursor.close();
            return searchList;
        }
        return null;
    }

    public String getContryCode(String countryName) {
        String codeName = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNTRY_CODE FROM COMP_DATA where COUNTRY_NAME=" + "'" + countryName + "'", null);
        int cursorCount = 0;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            codeName = cursor.getString(cursor.getColumnIndex("COUNTRY_CODE"));
            cursor.close();
        }
        return codeName;
    }

    public String getContryName(String countryCode) {
        String codeName = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNTRY_NAME FROM COMP_DATA where COUNTRY_CODE='" + countryCode + "'", null);
        int cursorCount = 0;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            codeName = cursor.getString(cursor.getColumnIndex("COUNTRY_NAME"));
            cursor.close();
        }
        return codeName;
    }

    public String getStateName(String countryCode, String stateCode) {
        String codeName = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT STATE_NAME FROM state_master where STATE_CODE=" + "'" + stateCode + "' AND COUNTRY_CODE = '" + countryCode + "'", null);
        int cursorCount = 0;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            codeName = cursor.getString(cursor.getColumnIndex("STATE_NAME"));
            cursor.close();
        }
        return codeName;
    }

    public List<String> getStateList(String countryCode) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT STATE_CODE,STATE_NAME FROM state_master where COUNTRY_CODE = '" + countryCode + "' ORDER BY STATE_NAME ASC", null);
        if (cursor != null && cursor.getCount() != 0) {
            List<String> searchList = new ArrayList<>();
            while (cursor.moveToNext()) {
                searchList.add(WordUtils.capitalizeFully(cursor.getString(cursor.getColumnIndex("STATE_NAME"))));
            }
            cursor.close();
            return searchList;
        }
        return new ArrayList<String>();
    }

    public String getStateCode(String stateName) {

        String codeName = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT STATE_CODE FROM state_master where STATE_NAME=" + "'" + stateName + "'", null);
        int cursorCount = 0;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            codeName = cursor.getString(cursor.getColumnIndex("STATE_CODE"));
            cursor.close();
        }
        return codeName;
    }

    public List<ModelFlightSearch> getAirportNames(String inputString) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ModelFlightSearch> mainSearchList = new ArrayList<>();

        cursor = db.rawQuery("SELECT * FROM `COMP_DATA` as COMP where COMP.AIRPORT_CODE = '" + inputString + "'", null);
        String airportCode = "";
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ModelFlightSearch modelFlightSearch = new ModelFlightSearch();
                modelFlightSearch.setCityName(cursor.getString(cursor.getColumnIndex("CITY_NAME_1")));
                modelFlightSearch.setAirportName(cursor.getString(cursor.getColumnIndex("AIRPORT_NAME_1")));
                airportCode = cursor.getString(cursor.getColumnIndex("AIRPORT_CODE"));
                modelFlightSearch.setAirportCode(airportCode);
                modelFlightSearch.setCountryName(cursor.getString(cursor.getColumnIndex("COUNTRY_NAME")));
                modelFlightSearch.setCountryCode(cursor.getString(cursor.getColumnIndex("COUNTRY_CODE")));
                mainSearchList.add(modelFlightSearch);
            }
            cursor.close();
        }

        cursor = db.rawQuery("SELECT * FROM `COMP_DATA` as COMP where COMP.AIRPORT_NAME_1 like '" + inputString + "%' OR COMP.CITY_NAME_1 like '" + inputString + "%' OR COMP.COUNTRY_NAME like '" + inputString + "%' OR COMP.ALIAS_NAME_1 like '" + inputString + "%' ORDER BY GLOBAL_PRIORITY DESC", null);
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ModelFlightSearch modelFlightSearch = new ModelFlightSearch();
                modelFlightSearch.setCityName(cursor.getString(cursor.getColumnIndex("CITY_NAME_1")));
                modelFlightSearch.setAirportName(cursor.getString(cursor.getColumnIndex("AIRPORT_NAME_1")));
                String airportCodeCity = cursor.getString(cursor.getColumnIndex("AIRPORT_CODE"));
                modelFlightSearch.setAirportCode(airportCodeCity);
                modelFlightSearch.setCountryName(cursor.getString(cursor.getColumnIndex("COUNTRY_NAME")));
                modelFlightSearch.setCountryCode(cursor.getString(cursor.getColumnIndex("COUNTRY_CODE")));
                if (!airportCode.equals(airportCodeCity))
                    mainSearchList.add(modelFlightSearch);
            }
            cursor.close();
        }
        return mainSearchList;
    }

    public List<ModelFlightSearch> getCountryAirportNames(String inputString, String countryName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ModelFlightSearch> mainSearchList = new ArrayList<>();

        cursor = db.rawQuery("SELECT * FROM `COMP_DATA` as COMP where COMP.COUNTRY_NAME = '" + countryName + "' AND COMP.AIRPORT_CODE = '" + inputString + "'", null);
        String airportCode = "";
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ModelFlightSearch modelFlightSearch = new ModelFlightSearch();
                modelFlightSearch.setCityName(cursor.getString(cursor.getColumnIndex("CITY_NAME_1")));
                modelFlightSearch.setAirportName(cursor.getString(cursor.getColumnIndex("AIRPORT_NAME_1")));
                airportCode = cursor.getString(cursor.getColumnIndex("AIRPORT_CODE"));
                modelFlightSearch.setAirportCode(airportCode);
                modelFlightSearch.setCountryName(cursor.getString(cursor.getColumnIndex("COUNTRY_NAME")));
                modelFlightSearch.setCountryCode(cursor.getString(cursor.getColumnIndex("COUNTRY_CODE")));
                mainSearchList.add(modelFlightSearch);
            }
            cursor.close();
        }

        //SELECT * FROM COMP_DATA where COUNTRY_NAME = 'Australia' AND (CITY_NAME_1 like  'MEL%' OR AIRPORT_NAME_1 like 'MEL%' OR COUNTRY_NAME like 'MEL%' OR AIRPORT_CODE like 'MEL%') ORDER BY PRIORITY DESC
        cursor = db.rawQuery("SELECT * FROM `COMP_DATA` as COMP where COMP.COUNTRY_NAME = '" + countryName + "' AND (COMP.AIRPORT_NAME_1 like '" + inputString + "%' OR COMP.CITY_NAME_1 like '" + inputString + "%' OR COMP.COUNTRY_NAME like '" + inputString + "%' OR COMP.ALIAS_NAME_1 like '" + inputString + "%') ORDER BY GLOBAL_PRIORITY DESC", null);
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ModelFlightSearch modelFlightSearch = new ModelFlightSearch();
                modelFlightSearch.setCityName(cursor.getString(cursor.getColumnIndex("CITY_NAME_1")));
                modelFlightSearch.setAirportName(cursor.getString(cursor.getColumnIndex("AIRPORT_NAME_1")));
                String airportCodeCity = cursor.getString(cursor.getColumnIndex("AIRPORT_CODE"));
                modelFlightSearch.setAirportCode(airportCodeCity);
                modelFlightSearch.setCountryName(cursor.getString(cursor.getColumnIndex("COUNTRY_NAME")));
                modelFlightSearch.setCountryCode(cursor.getString(cursor.getColumnIndex("COUNTRY_CODE")));
                if (!airportCode.equals(airportCodeCity))
                    mainSearchList.add(modelFlightSearch);
            }
            cursor.close();
        }
        return mainSearchList;
    }

    public List<ModelFlightSearch> getAirportNamesDefaultList() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        cursor = db.rawQuery("SELECT * FROM `COMP_DATA` WHERE PRIORITY <> 0 ORDER BY GLOBAL_PRIORITY DESC", null);
        if (cursor != null && cursor.getCount() != 0) {
            List<ModelFlightSearch> searchList = new ArrayList<>();
            while (cursor.moveToNext()) {
                ModelFlightSearch modelFlightSearch = new ModelFlightSearch();
                modelFlightSearch.setCityName(cursor.getString(cursor.getColumnIndex("CITY_NAME_1")));
                modelFlightSearch.setAirportName(cursor.getString(cursor.getColumnIndex("AIRPORT_NAME_1")));
                modelFlightSearch.setAirportCode(cursor.getString(cursor.getColumnIndex("AIRPORT_CODE")));
                modelFlightSearch.setCountryName(cursor.getString(cursor.getColumnIndex("COUNTRY_NAME")));
                modelFlightSearch.setCountryCode(cursor.getString(cursor.getColumnIndex("COUNTRY_CODE")));
                searchList.add(modelFlightSearch);
            }
            cursor.close();
            return searchList;
        }
        return null;
    }

    public List<ModelFlightSearch> getAirportNamesCountryList(String countryName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ModelFlightSearch> searchList = new ArrayList<>();

        //cursor = db.rawQuery("SELECT * FROM `COMP_DATA` where COUNTRY_NAME = 'Australia'", null);
        cursor = db.rawQuery("SELECT * FROM `COMP_DATA` where COUNTRY_NAME = " + "'" + countryName + "' AND PRIORITY <> 0 ORDER BY GLOBAL_PRIORITY DESC", null);
        if (cursor != null && cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                ModelFlightSearch modelFlightSearch = new ModelFlightSearch();
                modelFlightSearch.setCityName(cursor.getString(cursor.getColumnIndex("CITY_NAME_1")));
                modelFlightSearch.setAirportName(cursor.getString(cursor.getColumnIndex("AIRPORT_NAME_1")));
                modelFlightSearch.setAirportCode(cursor.getString(cursor.getColumnIndex("AIRPORT_CODE")));
                modelFlightSearch.setCountryName(cursor.getString(cursor.getColumnIndex("COUNTRY_NAME")));
                modelFlightSearch.setCountryCode(cursor.getString(cursor.getColumnIndex("COUNTRY_CODE")));
                searchList.add(modelFlightSearch);
            }
            cursor.close();

        }
        return searchList;
    }


}
