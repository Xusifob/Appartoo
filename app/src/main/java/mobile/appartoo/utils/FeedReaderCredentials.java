package mobile.appartoo.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by alexandre on 16-07-13.
 */
public class FeedReaderCredentials extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Appartoo.db";
    private static final String TEXT_TYPE = "TEXT";
    private static final String BOOLEAN_TYPE = "BOOLEAN";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedEntry.TABLE_CREDENTIALS + " (" +
            FeedEntry._ID + " INTEGER PRIMARY KEY," +
            FeedEntry.COLUMN_USER_MAIL + " " + TEXT_TYPE + COMMA_SEP +
            FeedEntry.COLUMN_USER_CREDENTIALS + " " + TEXT_TYPE + COMMA_SEP +
            FeedEntry.COLUMN_IS_LOGGED + " " + BOOLEAN_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_CREDENTIALS;
    public FeedReaderCredentials(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void setLastUserLoggedCredentials() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + FeedEntry.COLUMN_USER_CREDENTIALS + ", " + FeedEntry.COLUMN_USER_MAIL +
                " FROM " + FeedEntry.TABLE_CREDENTIALS +
                " WHERE " + FeedEntry.COLUMN_IS_LOGGED + " = ?";

        Cursor c = db.rawQuery(query, new String[] {"true"});

        try {
            c.moveToFirst();
            Appartoo.LOGGED_USER_MAIL = c.getString(c.getColumnIndex(FeedEntry.COLUMN_USER_MAIL));
            Appartoo.TOKEN = c.getString(c.getColumnIndex(FeedEntry.COLUMN_USER_CREDENTIALS));
        } catch (CursorIndexOutOfBoundsException e) {
            Appartoo.TOKEN = null;
            Appartoo.LOGGED_USER_MAIL = null;
        } catch (Exception e) {
            e.printStackTrace();
            Appartoo.TOKEN = null;
            Appartoo.LOGGED_USER_MAIL = null;
        }
    }

    public boolean userIsInDatabase(String mail){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + FeedEntry.COLUMN_USER_MAIL +
                " FROM " + FeedEntry.TABLE_CREDENTIALS +
                " WHERE " + FeedEntry.COLUMN_USER_MAIL + "=?";
        Cursor c = db.rawQuery(query, new String[] {mail});
        if (c.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void addUserToDatabase(String mail, String credentials){
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "INSERT INTO " + FeedEntry.TABLE_CREDENTIALS +
                    "(" + FeedEntry.COLUMN_USER_MAIL +
                    ", " + FeedEntry.COLUMN_USER_CREDENTIALS +
                    ", " + FeedEntry.COLUMN_IS_LOGGED + ")" +
                    " VALUES " + "('" + mail + "', '" + credentials + "', 'true')";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserLoggedState(String mail, boolean logged) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "UPDATE " + FeedEntry.TABLE_CREDENTIALS +
                    " SET " + FeedEntry.COLUMN_IS_LOGGED + " = '" + Boolean.toString(logged) + "'" +
                    " WHERE " + FeedEntry.COLUMN_USER_MAIL + " = '" + mail + "'";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_CREDENTIALS = "credentials";
        public static final String COLUMN_USER_MAIL = "mail";
        public static final String COLUMN_USER_CREDENTIALS = "user_credentials";
        public static final String COLUMN_IS_LOGGED = "is_logged";

    }
}