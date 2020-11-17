package tech.demura.detmaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

@SuppressLint("DefaultLocale")
public class DataFromBase {
    private static int
            ID_INDEX,
            GROUP1_INDEX,
            GROUP_ID_INDEX,
            IF_NOT_INDEX,
            NEXT_ID_INDEX,
            ORDER_INDEX,
            FAMILY_INDEX,
            GENUS_INDEX,
            SPECIES_INDEX,
            SIGN_INDEX,
            IMAGE_PATH_INDEX;

    // todo static?
    private static int
            id = 1,
            group1,
            group_id = 1,
            if_not = 0,
            next_id = 0,
            order = 0,
            family = 0,
            genus = 0,
            species = 0;
    private Context context;
    private static Cursor cursor;
    private static SQLiteDatabase db;
    private DBHelper dbHelper;

    DataFromBase(Context current) {
        context = current;
        dbHelper = new DBHelper(context);
        // todo Прописать создание в class Application через наследника
        dbHelper.createDB();
        db = dbHelper.openBase();

        cursor = db.query(DBHelper.TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();

        // todo почему не перенести присвоение начальных значений выше?
        id = 1;
        group_id = 1;
        if_not = 0;
        next_id = 0;
        order = 0;
        family = 0;
        genus = 0;
        species = 0;

        //todo получение самого первого значения?
        ID_INDEX = cursor.getColumnIndex(DBHelper.ID);
        GROUP1_INDEX = cursor.getColumnIndex(DBHelper.GROUP1);
        GROUP_ID_INDEX = cursor.getColumnIndex(DBHelper.GROUP_ID);
        IF_NOT_INDEX = cursor.getColumnIndex(DBHelper.IF_NOT);
        NEXT_ID_INDEX = cursor.getColumnIndex(DBHelper.NEXT_ID);
        ORDER_INDEX = cursor.getColumnIndex(DBHelper.ORDER1);
        FAMILY_INDEX = cursor.getColumnIndex(DBHelper.FAMILY);
        GENUS_INDEX = cursor.getColumnIndex(DBHelper.GENUS);
        SPECIES_INDEX = cursor.getColumnIndex(DBHelper.SPECIES);
        SIGN_INDEX = cursor.getColumnIndex(DBHelper.SIGN);
        IMAGE_PATH_INDEX = cursor.getColumnIndex(DBHelper.IMAGE_PATH);
    }

    //todo group1 = group2? при этом метод называется setGroup1
    public void setGroup1(int group2) {
        Log.d("log", "setGroup1 starting");
        group1 = group2;
    }

    public void clickedYes() {
        Log.d("log", "clickedYes starting");
        group_id = cursor.getInt(NEXT_ID_INDEX);
        order = cursor.getInt(ORDER_INDEX);
        family = cursor.getInt(FAMILY_INDEX);
        genus = cursor.getInt(GENUS_INDEX);
        species = cursor.getInt(SPECIES_INDEX);
    }

    public void clickedNo() {
        Log.d("log", "clickedNo starting " + cursor.getInt(IF_NOT_INDEX));
        group_id = cursor.getInt(IF_NOT_INDEX);
    }

    public String getSign(String query) {
        Log.d("log", "getSignQuery starting");
        cursor = db.query(DBHelper.TABLE, null, query, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(SIGN_INDEX);
    }

    public String getSign(String query, int INDEX) {
        Log.d("log", "getSignIndex starting");
        cursor = db.query(DBHelper.TABLE, null, query, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(INDEX);
    }

    public int getGroupId() {
        Log.d("log", "getGroupId starting ");
        return group_id;
    }

    public int getImageId() {
        Log.d("log", "getImageId starting");
        String query = "";
        query = DBHelper.GROUP1 + "=" + group1
                + " AND " + DBHelper.GROUP_ID + "=" + -1
                + " AND " + DBHelper.NEXT_ID + "=" + -1;

        if (order > 0) {
            query += " AND " + DBHelper.ORDER1 + "=" + order;
        }
        if (family > 0) {
            query += " AND " + DBHelper.FAMILY + "=" + family;
        }
        if (genus > 0) {
            query += " AND " + DBHelper.GENUS + "=" + genus;
        }
        if (species > 0) {
            query += " AND " + DBHelper.SPECIES + "=" + species;
        }
        int resId = context.getResources().getIdentifier(getSign(query, IMAGE_PATH_INDEX), "drawable", context.getPackageName());
        return resId;
    }

    public String progressText() {
        Log.d("log", "ProgressText starting");
        String progress = "-> ", preText = "Определение отряда:\n";
        String query;
        query = String.format("%s=%d AND %s=%d AND %s=%d", DBHelper.GROUP1, group1, DBHelper.GROUP_ID, -1, DBHelper.NEXT_ID, -1);

        if (order > 0) {
            query += String.format(" AND %s=%d", DBHelper.ORDER1, order);
            progress += String.format("%s\n-> ", getSign(query));
            preText = "Определение семейства:\n";
        }
        if (family > 0) {
            query += String.format(" AND %s=%d", DBHelper.FAMILY, family);
            progress += String.format("%s\n-> ", getSign(query));
            preText = "Определение рода:\n";
        }
        if (genus > 0) {
            query += String.format(" AND %s=%d", DBHelper.GENUS, genus);
            progress += String.format("%s\n-> ", getSign(query));
            preText = "Определение вида:\n";
        }
        if (species > 0) {
            query += String.format(" AND %s=%d", DBHelper.SPECIES, species);
            progress += String.format("%s", getSign(query));
        }
        if(group_id < 0){
            preText = "Результат:\n";
        }
        return preText + progress;
    }


    public String previewText() {
        Log.d("log", "previewText starting");
        String result = "", query = "";
        query = String.format("%s=%d AND %s=%d", DBHelper.GROUP1, group1, DBHelper.GROUP_ID, group_id);
        if (order > 0) {
            query += String.format(" AND %s=%d", DBHelper.ORDER1, order);
        }
        if (family > 0) {
            query += String.format(" AND %s=%d", DBHelper.FAMILY, family);
        }
        if (genus > 0) {
            query += String.format(" AND %s=%d", DBHelper.GENUS, genus);
        }
        result = group_id + ".(" + getSign(query, IF_NOT_INDEX) + ")-" + getSign(query);

        return result;
    }

    @SuppressLint("DefaultLocale")
    public ArrayList<String> getStringList() {
        Log.d("log", "getStringList starting");

        String cmQuery = String.format("%s=%d AND %s=-1", DBHelper.GROUP1, group1, DBHelper.GROUP_ID);

        if (order == 0) {
            cmQuery += String.format(" AND %s>0 AND %s=0", DBHelper.ORDER1, DBHelper.FAMILY);
        } else if (family == 0) {
            cmQuery += String.format(" AND %s=%d AND %s>0 AND %s=0", DBHelper.ORDER1, order, DBHelper.FAMILY, DBHelper.GENUS);
        } else if (genus == 0) {
            cmQuery += String.format(" AND %s=%d AND %s=%d AND %s>0 AND %s=0", DBHelper.ORDER1, order, DBHelper.FAMILY, family, DBHelper.GENUS, DBHelper.SPECIES);
        } else if (species == 0) {
            cmQuery += String.format(" AND %s=%d AND %s=%d AND %s=%d AND %s>0", DBHelper.ORDER1, order, DBHelper.FAMILY, family, DBHelper.GENUS, genus, DBHelper.SPECIES);
        }
        ArrayList<String> result = new ArrayList<>();
        Cursor cursor1;
        cursor1 = db.query(DBHelper.TABLE, null, cmQuery, null, null, null, null);
        if (cursor1.getCount() == 0) {
            return result;
        }
        cursor1.moveToFirst();
        for (int i = 0; i < cursor1.getCount(); i++) {
            result.add(cursor1.getString(SIGN_INDEX));
            cursor1.moveToNext();
        }
        return result;
    }

    public void Assignment(int itemId) {
        Log.d("log", "Assignment starting");
        String cmQuery = String.format("%s=%d AND %s>0", DBHelper.GROUP1, group1, DBHelper.GROUP_ID);
        if (order == 0) {
            order = itemId;
            cmQuery += String.format(" AND %s=%d", DBHelper.ORDER1, order);
        } else if (family == 0) {
            family = itemId;
            cmQuery += String.format(" AND %s=%d AND %s=%d", DBHelper.ORDER1, order, DBHelper.FAMILY, family);
        } else if (genus == 0) {
            genus = itemId;
            cmQuery += String.format(" AND %s=%d AND %s=%d AND %s=%d", DBHelper.ORDER1, order, DBHelper.FAMILY, family, DBHelper.GENUS, genus);
        } else if (species == 0) {
            species = itemId;
            cmQuery += String.format(" AND %s=%d AND %s=%d AND %s=%d AND %s=%d", DBHelper.ORDER1, order, DBHelper.FAMILY, family, DBHelper.GENUS, genus, DBHelper.SPECIES, species);
        }
        cursor = db.query(DBHelper.TABLE, null, cmQuery, null, null, null, null);
        cursor.moveToFirst();
    }
}
