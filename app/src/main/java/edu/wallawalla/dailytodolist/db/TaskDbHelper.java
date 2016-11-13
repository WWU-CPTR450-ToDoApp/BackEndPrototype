package edu.wallawalla.dailytodolist.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class TaskDbHelper extends SQLiteOpenHelper {
    public TaskDbHelper(Context context) {
        super(context, TaskContract.DATABASE_NAME, null, TaskContract.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + "("
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_NAME_COL2 + " INTEGER NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_NAME_COL3 + " INTEGER NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_NAME_COL4 + " INTEGER NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_NAME_COL5 + " INTEGER NOT NULL, "
                + TaskContract.TaskEntry.COL_TASK_DESC + " INTEGER"
                + ");";
        db.execSQL(CREATE_TASKS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }

    public void addTask(ToDoTask task) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task.getCol1Data());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COL2, task.getCol2Data());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COL3, task.getCol3Data());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COL4, task.getCol4Data());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COL5, task.getCol5Data());
        values.put(TaskContract.TaskEntry.COL_TASK_DESC, task.getCol6Data());


        SQLiteDatabase db = this.getWritableDatabase();

        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public Cursor findTask(String key, String col) {
        // Define a projection that specifies which columns from the database
        // we will actually use after this query
        String[] projection = {
                TaskContract.TaskEntry._ID, col
        };

        // Filter results WHERE col = key
        String selection = col + " = ?";
        String[] selectionArgs = {key};

        // How we want the results sorted in the resulting Cursor
        String sortOrder = null;
        //        TaskContract.TaskEntry.COLUMN_NAME_COL2;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(
                TaskContract.TaskEntry.TABLE,   // the table to query
                projection,                     // the columns to return
                selection,                      // the columns for the WHERE clause
                selectionArgs,                  // the values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                sortOrder                       // the sort order
        );
        return c;
    }

    public Cursor findTask(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(
                TaskContract.TaskEntry.TABLE,   // the table to query
                projection,                     // the columns to return
                selection,                      // the columns for the WHERE clause
                selectionArgs,                  // the values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                sortOrder                       // the sort order
        );
        return c;
    }

    public boolean deleteTask(String s) {
        boolean result = false;
        String query = "Select * FROM " + TaskContract.TaskEntry.TABLE + " WHERE " + TaskContract.TaskEntry.COL_TASK_TITLE + " = \"" + s + "\"";
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