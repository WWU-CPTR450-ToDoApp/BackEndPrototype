package edu.wallawalla.dailytodolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.wallawalla.dailytodolist.db.DBHandler;
import edu.wallawalla.dailytodolist.db.TaskContract;
import edu.wallawalla.dailytodolist.db.ToDoTask;

public class MainActivity extends AppCompatActivity {
    TextView idView;
    EditText taskBox;

    private DBHandler dbHandler;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idView = (TextView) findViewById(R.id.taskID);
        taskBox = (EditText) findViewById(R.id.taskName);
        dbHandler = new DBHandler(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        updateUI();
    }

    public void newTask (View view) {
        ToDoTask task = new ToDoTask(taskBox.getText().toString());
        dbHandler.addTask(task);
        taskBox.setText("");
        updateUI();
    }
    public void lookupTask (View view) {
        Cursor c = dbHandler.findTask(taskBox.getText().toString(), taskBox.getText().toString());
        if(c != null) {
            idView.setText("Found " + c.getCount() + " entries matching the query.");
        } else {
            idView.setText("No Match(es) Found");
        }
        // update the UI with the returned array
        updateUI(c);
    }

    public void removeTask (View view) {
        boolean result = dbHandler.deleteTask(taskBox.getText().toString());
        if (result) {
            idView.setText("Record Deleted");
            taskBox.setText("");
        } else {
            idView.setText("No Match Found");
        }
        updateUI();
    }

    public void deleteSingleTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_col1);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COLUMN_NAME_COL1 + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COLUMN_NAME_COL1},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_COL1);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_col1,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    private void updateUI(Cursor c) {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        while(c.moveToNext()) {
            int idx = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_COL1);
            taskList.add(c.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_col1,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        c.close();
        db.close();
    }
}
