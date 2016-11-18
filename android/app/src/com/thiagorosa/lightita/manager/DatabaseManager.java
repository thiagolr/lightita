/*
  Copyright (c) 2016 Thiago Lopes Rosa

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.thiagorosa.lightita.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thiagorosa.lightita.common.Logger;

public class DatabaseManager {

    private static final DatabaseManager INSTANCE = new DatabaseManager();

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper mDbHelper = null;
    private static SQLiteDatabase mDb = null;
    private static final int MAX_RETRIES = 5;
    private static int numRetries = 0;

    /*******************************************************************************************
     *******************************************************************************************/

    private static final String SEQUENCE_TABLE = "sequences";
    public static final String SEQUENCE_ID = "_id";
    public static final String SEQUENCE_NAME = "name";
    public static final String SEQUENCE_VALUE = "value";

    private static final String QUERY_CREATE_SEQUENCE = "CREATE TABLE " + SEQUENCE_TABLE + " (" + SEQUENCE_ID + " integer primary key, " + SEQUENCE_NAME + " text not null, " + SEQUENCE_VALUE + " text not null)";
    private static final String QUERY_SELECT_SEQUENCE_ALL = "SELECT * FROM " + SEQUENCE_TABLE;
    private static final String QUERY_SELECT_SEQUENCE_ID = "SELECT * FROM " + SEQUENCE_TABLE + " WHERE " + SEQUENCE_ID + " = ?";

    /*******************************************************************************************
     *******************************************************************************************/

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mDbHelper = new DatabaseHelper(context);
    }

    /*******************************************************************************************
     *******************************************************************************************/

    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(QUERY_CREATE_SEQUENCE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SEQUENCE_TABLE);
            db.execSQL(QUERY_CREATE_SEQUENCE);
        }

    }

    /*******************************************************************************************
     *******************************************************************************************/

    private boolean openDatabase() {
        if (mDb == null || !mDb.isOpen()) {
            while (numRetries < MAX_RETRIES) {
                try {
                    mDb = mDbHelper.getWritableDatabase();
                } catch (Exception ignored) {
                    mDb = null;
                }
                if (mDb != null) {
                    return true;
                }
                numRetries++;
            }
            return false;
        }
        return true;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public Cursor fetchSequences() {
        if (openDatabase()) {
            Cursor cursor = mDb.rawQuery(QUERY_SELECT_SEQUENCE_ALL, null);
            if (cursor != null) {
                Logger.DB("fetchSequences: " + cursor.getCount());
            } else {
                Logger.DB("fetchSequences: empty");
            }
            return cursor;
        }
        return null;
    }

    public void updateSequence(long id, String name, String value) {
        Logger.DB("updateSequence: " + value);

        if (openDatabase()) {
            Cursor cursor = mDb.rawQuery(QUERY_SELECT_SEQUENCE_ID, new String[]{"" + id});

            ContentValues values = new ContentValues();
            values.put(SEQUENCE_NAME, name);
            values.put(SEQUENCE_VALUE, value);

            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    long result = mDb.update(SEQUENCE_TABLE, values, SEQUENCE_ID + " = " + id, null);
                    Logger.DB("updateSequence: update result " + result);
                } else {
                    long result = mDb.insert(SEQUENCE_TABLE, null, values);
                    Logger.DB("updateSequence: create result " + result);
                }
                cursor.close();
            }
        }
    }

    public void deleteSequence(long id) {
        Logger.DB("deleteSequence: " + id);

        if (openDatabase()) {
            long result = mDb.delete(SEQUENCE_TABLE, SEQUENCE_ID + " = " + id, null);
            Logger.DB("updateSequence: delete result " + result);
        }
    }

}
