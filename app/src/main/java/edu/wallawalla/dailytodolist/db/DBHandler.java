package edu.wallawalla.dailytodolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context context) {
        super(context, TaskContract.DATABASE_NAME, null, TaskContract.DATABASE_VERSION);
        Log.d("DB", "super");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "CREATE");
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + "("
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COLUMN_NAME_COL1 + " TEXT NOT NULL" + ");";
        db.execSQL(CREATE_TASKS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "OVER");
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }

    public void addTask(ToDoTask task) {
        Log.d("DB", "ADD");
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COL1, task.getCol1Data());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ToDoTask findTask(String s) {
        String query = "Select * FROM " + TaskContract.TaskEntry.TABLE + " WHERE " + TaskContract.TaskEntry.COLUMN_NAME_COL1 + " = \"" + s + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ToDoTask task = new ToDoTask();
        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            task.setID(Integer.parseInt(cursor.getString(0)));
            task.setCol1Data(cursor.getString(1));
            cursor.close();
        } else {
            task = null;
        }
        db.close();
        return task;
    }

    public boolean deleteTask(String s) {
        boolean result = false;
        String query = "Select * FROM " + TaskContract.TaskEntry.TABLE + " WHERE " + TaskContract.TaskEntry.COLUMN_NAME_COL1 + " = \"" + s + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ToDoTask task = new ToDoTask();
        if(cursor.moveToFirst()) {
            task.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TaskContract.TaskEntry.TABLE, TaskContract.COLUMN_ID + " = ?",
                    new String[]{ String.valueOf(task.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
