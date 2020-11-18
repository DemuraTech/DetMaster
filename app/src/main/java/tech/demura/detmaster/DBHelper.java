package tech.demura.detmaster;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DBHelper extends SQLiteOpenHelper {

    // todo naming
    private Context context;
    static String DB_PATH;

    public DBHelper(Context context) {
        super(context, BaseInfo.DB_NAME, null, BaseInfo.SCHEMA);
        this.context = context;
        DB_PATH = context.getFilesDir().getPath() + BaseInfo.DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void createDB() {

        //todo myInput и myOutput по умолчанию будут null, явно к null приравнивать не нужно
        // myInput myOutput - плохой нейминг
        // https://carlalexander.ca/importance-naming-programming/
        // https://dmitripavlutin.com/coding-like-shakespeare-practical-function-naming-conventions/
        // и т.д.

        InputStream assetsBase;
        OutputStream resBase;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();
                //todo Use try-with-resources or close this "InputStream" in a "finally" clause.
                assetsBase = context.getAssets().open(BaseInfo.DB_NAME);
                String outFileName = DB_PATH;
                // todo Use try-with-resources or close this "OutputStream" in a "finally" clause.
                resBase = new FileOutputStream(outFileName);
                // todo вынести чтение с файла в массив байт / outputstream
                //  как метод в отдельный класс типа FileUtil
                //  Util-классы это классы расширяющие и дополняющие функционал которого не хватает в Java
                //  пример FileUtil.readBytesFromFile(File file) или
                //  FileUtil.readBytesFromFile(InputStream file)
                byte[] buffer = new byte[1024];
                int length;
                while ((length = assetsBase.read(buffer)) > 0) {
                    resBase.write(buffer, 0, length);
                }
                resBase.flush();
                resBase.close();
                assetsBase.close();
            }
        } catch (IOException ex) {
            Log.d("BASE", ex.getMessage());
        }
    }

    // todo нэйминг
    public SQLiteDatabase open() throws SQLException {
        Log.d("BASE", "Open DetBase");
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
