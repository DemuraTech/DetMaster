package tech.demura.detmaster;

import android.app.Application;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.createDB();
    }
}
