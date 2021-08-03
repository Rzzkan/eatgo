package tech.mlsn.eatgo.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import tech.mlsn.eatgo.model.CartModel;


public class SQLiteDBHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME="eatgo_db";

    public static final String TABLE_NAME="cart";

    public static final String COL_ID ="id";
    public static final String COl_ID_RESTAURANT ="id_resto";
    public static final String COl_ID_MENU ="id_menu";
    public static final String COl_NAME ="name";
    public static final String COL_QTY ="qty";
    public static final String COL_PRICE ="price";
    public static final String COL_TOTAL ="total";
    public static final String COL_IMG ="img";
    public static final String COL_DESC ="des";


    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                ""+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+COl_ID_RESTAURANT+" VARCHAR, " +
                ""+COl_ID_MENU+" VARCHAR, " +
                ""+COl_NAME+" VARCHAR, " +
                ""+COL_QTY+" INTEGER, " +
                ""+COL_PRICE+" INTEGER, " +
                ""+COL_TOTAL+" INTEGER, " +
                ""+COL_IMG+" VARCHAR, " +
                ""+COL_DESC+" VARCHAR " +
                ")";
        database.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(CartModel cart){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_ID, Integer.valueOf(cart.getId_cart()));
        contentValues.put(COl_ID_RESTAURANT, cart.getId_restaurant());
        contentValues.put(COl_ID_MENU, cart.getId_menu());
        contentValues.put(COl_NAME, cart.getName());
        contentValues.put(COL_PRICE, Integer.valueOf(cart.getPrice()));
        contentValues.put(COL_QTY, Integer.valueOf(cart.getQty()));
        contentValues.put(COL_TOTAL, Integer.valueOf(cart.getTotal()));
        contentValues.put(COL_IMG, cart.getImg());
        contentValues.put(COL_DESC, cart.getDesc());
        Log.d("Insert", "addDATA : ADDING" + cart.getId_cart() + "\n" + cart.getName() + "\nto " + TABLE_NAME);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else
            return true;
    }

    public ArrayList<CartModel> getCartData() {
        ArrayList<CartModel> items = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                CartModel cart = new CartModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6),
                        cursor.getString(7),
                        cursor.getString(8)
                );
                items.add(cart);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public int updateCart(CartModel cart){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_ID, Integer.valueOf(cart.getId_cart()));
        contentValues.put(COl_ID_RESTAURANT, cart.getId_restaurant());
        contentValues.put(COl_ID_MENU, cart.getId_menu());
        contentValues.put(COl_NAME, cart.getName());
        contentValues.put(COL_PRICE, Integer.valueOf(cart.getPrice()));
        contentValues.put(COL_QTY, Integer.valueOf(cart.getQty()));
        contentValues.put(COL_TOTAL, Integer.valueOf(cart.getTotal()));
        contentValues.put(COL_IMG, cart.getImg());
        contentValues.put(COL_DESC, cart.getDesc());
        return db.update(TABLE_NAME,contentValues,COl_ID_MENU+"=?", new String[] {String.valueOf(cart.getId_menu())});
    }

    public void deleteCart(CartModel cart){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID+ " = ?",
                new String[]{String.valueOf(cart.getId_cart())});
        db.close();
    }

    public int countTotal(){
        int total = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_TOTAL  + ") as Total FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
           total  = cursor.getInt(cursor.getColumnIndex("Total"));// get final total
        }
        return total;
    }

    public void deleteDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
