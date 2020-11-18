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
            group1 = 1,
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
        group1 = 1;
        id = 1;
        group_id = 1;
        if_not = 0;
        next_id = 0;
        order = 0;
        family = 0;
        genus = 0;
        species = 0;

        context = current;
        dbHelper = new DBHelper(context);
        db = dbHelper.openBase();

        setGroup1(1);

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

    public void setGroup1(int group) {
        group1 = group;
        String query = String.format("%s=%d AND %s=%d", DBHelper.GROUP1, group1, DBHelper.GROUP_ID, group_id);
        cursor = db.query(DBHelper.TABLE, null, query, null, null, null, null);
        cursor.moveToFirst();
    }

    public void clickedYes() {
        group_id = cursor.getInt(NEXT_ID_INDEX);
        order = cursor.getInt(ORDER_INDEX);
        family = cursor.getInt(FAMILY_INDEX);
        genus = cursor.getInt(GENUS_INDEX);
        species = cursor.getInt(SPECIES_INDEX);


        String query = String.format("%s=%d AND %s=%d", DBHelper.GROUP1, group1, DBHelper.GROUP_ID, group_id);
        if (group_id == 1 || group_id == -1) {
            if (order > 0) {
                query += String.format(" AND %s=%d", DBHelper.ORDER1, order);
            }
            if (family > 0) {
                query += String.format(" AND %s=%d", DBHelper.FAMILY, family);
            }
            if (genus > 0) {
                query += String.format(" AND %s=%d", DBHelper.GENUS, genus);
            }
            if (species > 0) {
                query += String.format(" AND %s=%d", DBHelper.SPECIES, species);
            }
        } else {
            int id = cursor.getInt(ID_INDEX);
            id++;
            query += String.format(" AND %s=%d", DBHelper.ID, id);
        }
        cursor = db.query(DBHelper.TABLE, null, query, null, null, null, null);
        cursor.moveToFirst();
    }

    public void clickedNo() {
        int oldGroup_id = group_id;
        int id = cursor.getInt(ID_INDEX);
        int newId;
        group_id = cursor.getInt(IF_NOT_INDEX);
        newId = id + (group_id - oldGroup_id);
        String query = String.format("%s=%d AND %s=%d AND %s=%d", DBHelper.GROUP1, group1, DBHelper.GROUP_ID, group_id, DBHelper.ID, newId);
        cursor = db.query(DBHelper.TABLE, null, query, null, null, null, null);
        cursor.moveToFirst();
    }

    public String getSign(String query) {
        Cursor supCursor;
        supCursor = db.query(DBHelper.TABLE, null, query, null, null, null, null);
        supCursor.moveToFirst();
        return supCursor.getString(SIGN_INDEX);
    }

    public String getSign(String query, int INDEX) {
        Cursor supCursor;
        supCursor = db.query(DBHelper.TABLE, null, query, null, null, null, null);
        supCursor.moveToFirst();
        return supCursor.getString(INDEX);
    }

    public String getSign(Cursor supCursor) {
        return supCursor.getString(SIGN_INDEX);
    }

    public String getSign(Cursor supCursor, int INDEX) {
        return supCursor.getString(INDEX);
    }

    public int getGroupId() {
        return group_id;
    }

    public int getImageId() {
        Log.d("log", "getImageId starting");
        String query = "";
        query = String.format("%s=%d AND %s=%d AND %s=%d", DBHelper.GROUP1, group1, DBHelper.GROUP_ID, -1, DBHelper.NEXT_ID, -1);

        if (order > 0) {
            query += String.format(" AND %s=%d", DBHelper.ORDER1, order);
        }
        if (family > 0) {
            query += String.format(" AND %s=%d", DBHelper.FAMILY, family);
        }
        if (genus > 0) {
            query += String.format(" AND %s=%d", DBHelper.GENUS, genus);
        }
        if (species > 0) {
            query += String.format(" AND %s=%d", DBHelper.SPECIES, species);
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
        if (group_id < 0) {
            preText = "Результат:\n";
        }
        return preText + progress;
    }

    public String previewText() {
        return group_id + ".(" + getSign(cursor, IF_NOT_INDEX) + ")-" + getSign(cursor);
    }

    @SuppressLint("DefaultLocale")
    public ArrayList<String> getStringList() {
        ArrayList<String> result = new ArrayList<>();
        if (order == 0) {
            Cursor supCursor;
            String cmQuery = String.format("%s=%d AND %s=-1", DBHelper.GROUP1, group1, DBHelper.GROUP_ID);
            cmQuery += String.format(" AND %s>0 AND %s=0", DBHelper.ORDER1, DBHelper.FAMILY);
            supCursor = db.query(DBHelper.TABLE, null, cmQuery, null, null, null, null);
            if (supCursor.getCount() == 0) {
                return result;
            }
            supCursor.moveToFirst();
            for (int i = 0; i < supCursor.getCount(); i++) {
                result.add(supCursor.getString(SIGN_INDEX));
                supCursor.moveToNext();
            }
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
