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

//todo Сущности твоих таблиц лучше выносить в отдельные классы
// и там как раз будет инкапсулирована вся информация о них (имя колонок, имя таблицы, сами значения)
class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    // todo final field?
    private static final String DB_NAME = "DetBase.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "DetBase";

    static final String ID = "_id";
    static final String GROUP1 = "GROUP1";
    static final String GROUP_ID = "GROUP_ID";
    static final String IF_NOT = "IF_NOT";
    static final String NEXT_ID = "NEXT_ID";
    static final String ORDER1 = "ORDER1";
    static final String FAMILY = "FAMILY";
    static final String GENUS = "GENUS";
    static final String SPECIES = "SPECIES";
    static final String SIGN = "SIGN";
    static final String IMAGE_PATH = "IMAGE_PATH";


    // todo naming
    private Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
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

        Log.d("BASE", "Создание");
        InputStream myInput;
        OutputStream myOutput;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();
                //todo Use try-with-resources or close this "InputStream" in a "finally" clause.
                myInput = myContext.getAssets().open(DB_NAME);
                String outFileName = DB_PATH;
                // todo Use try-with-resources or close this "OutputStream" in a "finally" clause.
                myOutput = new FileOutputStream(outFileName);
                // todo вынести чтение с файла в массив байт / outputstream
                //  как метод в отдельный класс типа FileUtil
                //  Util-классы это классы расширяющие и дополняющие функционал которого не хватает в Java
                //  пример FileUtil.readBytesFromFile(File file) или
                //  FileUtil.readBytesFromFile(InputStream file)
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        } catch (IOException ex) {
            Log.d("BASE", ex.getMessage());
        }
    }

    // todo нэйминг
    public SQLiteDatabase openBase() throws SQLException {
        Log.d("BASE", "Open DetBase");
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
