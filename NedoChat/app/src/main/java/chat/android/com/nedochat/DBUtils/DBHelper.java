package chat.android.com.nedochat.DBUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chatbase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "chatmsgtb";
    public static final String ID_COLUMN = "_id";
    public static final String USER_NAME_COLUMN = "user_name";
    public static final String MESSAGE_COLUMN = "message";
    public static final String SESSION_NAME_COLUMN = "session";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + ID_COLUMN
            + " integer primary key autoincrement, " + USER_NAME_COLUMN
            + " text not null, " + MESSAGE_COLUMN + " text not null, "
            + SESSION_NAME_COLUMN + " text not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXIST " + DATABASE_TABLE);
        onCreate(db);
    }
}
