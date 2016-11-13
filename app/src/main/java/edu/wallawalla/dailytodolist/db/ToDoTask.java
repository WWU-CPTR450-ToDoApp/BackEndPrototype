package edu.wallawalla.dailytodolist.db;

public class ToDoTask {
    private int _id;
    private String _data_col1;

    public ToDoTask() {}
    public ToDoTask(int id, String data_col1) {
        this._id = id;
        this._data_col1 = data_col1;
    }
    public ToDoTask(String data_col1) {
        this._data_col1 = data_col1;
    }
    public void setID(int id) {
        this._id = id;
    }
    public int getID() {
        return this._id;
    }
    public void setCol1Data(String data_col1) {
        this._data_col1 = data_col1;
    }
    public String getCol1Data() {
        return this._data_col1;
    }


}
