//package tech.mlsn.eatgo.tools;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.paketnasi.agensembako.model.CartModel;
//
//import java.util.ArrayList;
//
//
//public class SQLiteDBHelper extends SQLiteOpenHelper {
//    static String DATABASE_NAME="agen_sembako_db";
//
//    public static final String TABLE_NAME="cart";
//
//    public static final String COL_ID ="id";
//    public static final String COL_SLUG ="slug";
//    public static final String COL_USER_ID ="user_id";
//    public static final String COl_NAME ="name";
//    public static final String COL_WEIGHT ="weight";
//    public static final String COL_WHOLESALEPRICE ="whole_sale_price";
//    public static final String COL_PRICE ="price";
//    public static final String COL_DESCRIPTION ="decription";
//    public static final String COL_PHOTO ="photo";
//    public static final String COL_STOCK ="stock";
//    public static final String COL_CREATED ="created_at";
//    public static final String COL_UPDATED ="updated_at";
//    public static final String COL_ACTIVE ="active";
//    public static final String COL_TOTAL ="total";
//
//
//    public SQLiteDBHelper(Context context) {
//        super(context, DATABASE_NAME, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase database) {
//
//        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
//                ""+ COL_ID +" INTEGER PRIMARY KEY, " +
//                ""+ COL_SLUG +" VARCHAR, " +
//                ""+ COL_USER_ID +" INTEGER, " +
//                ""+COl_NAME+" VARCHAR, " +
//                ""+COL_WEIGHT+" VARCHAR, " +
//                ""+COL_WHOLESALEPRICE+" INTEGER, " +
//                ""+COL_PRICE+" INTEGER, " +
//                ""+COL_DESCRIPTION+" VARCHAR, " +
//                ""+COL_PHOTO+" VARCHAR, " +
//                ""+COL_STOCK+" INTEGER, " +
//                ""+COL_CREATED+" VARCHAR, " +
//                ""+COL_UPDATED+" VARCHAR, " +
//                ""+COL_ACTIVE+" INTEGER, " +
//                ""+COL_TOTAL+" INTEGER " +
//                ")";
//        database.execSQL(CREATE_TABLE);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
//        onCreate(db);
//
//    }
//
//    public boolean addData(int id, String slug, int user_id, String name, String weight, int whole, int price, String description, String photo, int stock, String created, String updated, int active, int total){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_ID, id);
//        contentValues.put(COL_SLUG, slug);
//        contentValues.put(COL_USER_ID, user_id);
//        contentValues.put(COl_NAME, name);
//        contentValues.put(COL_WEIGHT, weight);
//        contentValues.put(COL_WHOLESALEPRICE, whole);
//        contentValues.put(COL_PRICE, price);
//        contentValues.put(COL_DESCRIPTION, description);
//        contentValues.put(COL_PHOTO, photo);
//        contentValues.put(COL_STOCK, stock);
//        contentValues.put(COL_CREATED, created);
//        contentValues.put(COL_UPDATED, updated);
//        contentValues.put(COL_ACTIVE, active);
//        contentValues.put(COL_TOTAL, total);
//        Log.d("Insert", "addDATA : ADDING" + user_id + "\n" + name + "\nto " + TABLE_NAME);
//        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
//        if (result == -1) {
//            return false;
//        } else
//            return true;
//    }
//
//    public ArrayList<CartModel> getCartData() {
//        ArrayList<CartModel> items = new ArrayList<>();
//        String selectQuery = "SELECT * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()){
//            do {
//                CartModel cart = new CartModel(
//                        cursor.getInt(0),
//                        cursor.getString(1),
//                        cursor.getInt(2),
//                        cursor.getString(3),
//                        cursor.getString(4),
//                        cursor.getInt(5),
//                        cursor.getInt(6),
//                        cursor.getString(7),
//                        cursor.getString(8),
//                        cursor.getInt(9),
//                        cursor.getString(10),
//                        cursor.getString(11),
//                        cursor.getInt(12),
//                        cursor.getInt(13)
//                );
//                items.add(cart);
//            } while (cursor.moveToNext());
//        }
//        return items;
//    }
//
//    public int updateCart(CartModel cart){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_ID, cart.getId());
//        contentValues.put(COL_SLUG, cart.getSlug());
//        contentValues.put(COL_USER_ID, cart.getUserId());
//        contentValues.put(COl_NAME, cart.getName());
//        contentValues.put(COL_WEIGHT, cart.getWeight());
//        contentValues.put(COL_WHOLESALEPRICE, cart.getWholesalePrice());
//        contentValues.put(COL_PRICE, cart.getPrice());
//        contentValues.put(COL_DESCRIPTION, cart.getDescription());
//        contentValues.put(COL_PHOTO, cart.getPhoto());
//        contentValues.put(COL_STOCK, cart.getStock());
//        contentValues.put(COL_CREATED, cart.getCreatedAt());
//        contentValues.put(COL_UPDATED, cart.getUpdatedAt());
//        contentValues.put(COL_ACTIVE, cart.getActive());
//        contentValues.put(COL_TOTAL, cart.getTotal());
//        return db.update(TABLE_NAME,contentValues,COL_ID+"=?", new String[] {String.valueOf(cart.getId())});
//    }
//
//    public void deleteCart(CartModel cart){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME, COL_ID+ " = ?",
//                new String[]{String.valueOf(cart.getId())});
//        db.close();
//    }
//
//    public int countTotal(){
//        int total = 0;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_WHOLESALEPRICE +"*"+ COL_TOTAL  + ") as Total FROM " + TABLE_NAME, null);
//
//        if (cursor.moveToFirst()) {
//           total  = cursor.getInt(cursor.getColumnIndex("Total"));// get final total
//        }
//        return total;
//    }
//
//    public void deleteDatabase(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME, null, null);
//    }
//}
