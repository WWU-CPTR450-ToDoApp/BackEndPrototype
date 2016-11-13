package edu.wallawalla.dailytodolist.db;

import android.provider.BaseColumns;

public final class TaskContract {
    public static final int     DATABASE_VERSION    = 1;
    public static final String  DATABASE_NAME       = "todotaskDB.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASKNAME = "taskname";

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE            = "taskTable";
        public static final String COLUMN_NAME_COL1 = "title";
        public static final String COLUMN_NAME_COL2 = "date";
        public static final String COLUMN_NAME_COL3 = "note";
    }
}
