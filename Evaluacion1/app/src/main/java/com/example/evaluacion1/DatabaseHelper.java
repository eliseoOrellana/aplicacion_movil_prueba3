package com.example.evaluacion1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "incidence_database";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_INCIDENCE = "incidences";
    private static final String KEY_ID = "id";
    private static final String KEY_LABORATORY = "laboratory";
    private static final String KEY_RUT = "rut";
    private static final String KEY_NAME = "name";
    private static final String KEY_INCIDENCE_BODY = "incidence_body";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    private static final String CREATE_TABLE_INCIDENCES = "CREATE TABLE "
            + TABLE_INCIDENCE + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LABORATORY + " TEXT, " + KEY_RUT + " TEXT, "
            + KEY_NAME + " TEXT, " + KEY_INCIDENCE_BODY + " TEXT, " + KEY_DATE + " TEXT, " + KEY_TIME + " TEXT );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("table", CREATE_TABLE_INCIDENCES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INCIDENCES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCIDENCE);
        onCreate(db);
    }

    public long addIncidenceDetail(String laboratory, String rut, String name, String incidenceBody, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LABORATORY, laboratory);
        values.put(KEY_RUT, rut);
        values.put(KEY_NAME, name);
        values.put(KEY_INCIDENCE_BODY, incidenceBody);
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);
        return db.insert(TABLE_INCIDENCE, null, values);
    }

    @SuppressLint("Range")
    public ArrayList<IncidenceModel> getAllIncidences() {
        ArrayList<IncidenceModel> incidenceModelArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_INCIDENCE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                IncidenceModel incidenceModel = new IncidenceModel();
                incidenceModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                incidenceModel.setLaboratory(c.getString(c.getColumnIndex(KEY_LABORATORY)));
                incidenceModel.setRut(c.getString(c.getColumnIndex(KEY_RUT)));
                incidenceModel.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                incidenceModel.setIncidenceBody(c.getString(c.getColumnIndex(KEY_INCIDENCE_BODY)));
                incidenceModel.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                incidenceModel.setTime(c.getString(c.getColumnIndex(KEY_TIME)));
                incidenceModelArrayList.add(incidenceModel);
            } while (c.moveToNext());
        }
        return incidenceModelArrayList;
    }

    public int updateIncidence(int id, String laboratory, String rut, String name, String incidenceBody, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LABORATORY, laboratory);
        values.put(KEY_RUT, rut);
        values.put(KEY_NAME, name);
        values.put(KEY_INCIDENCE_BODY, incidenceBody);
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);
        return db.update(TABLE_INCIDENCE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteIncidence(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INCIDENCE, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
}