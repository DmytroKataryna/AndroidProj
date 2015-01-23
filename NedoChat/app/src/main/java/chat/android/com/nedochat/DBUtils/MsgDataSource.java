package chat.android.com.nedochat.DBUtils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import chat.android.com.nedochat.POJO.Message;

public class MsgDataSource {

    private static MsgDataSource sDataSource;
    public DBHelper dbHelper;
    public SQLiteDatabase sdb;
    private Utils utils;
    public static final String[] COLUMNS = {DBHelper.ID_COLUMN, DBHelper.USER_NAME_COLUMN, DBHelper.MESSAGE_COLUMN, DBHelper.SESSION_NAME_COLUMN};

    public MsgDataSource(Context context) {
        dbHelper = new DBHelper(context);
        utils = Utils.get(context);
    }

    public static MsgDataSource get(Context c) {
        if (sDataSource == null) {
            sDataSource = new MsgDataSource(c.getApplicationContext());
        }
        return sDataSource;
    }

    public void open() throws SQLException {
        sdb = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Message createMSG(String name, String msg) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.USER_NAME_COLUMN, name);
        values.put(DBHelper.MESSAGE_COLUMN, msg);
        values.put(DBHelper.SESSION_NAME_COLUMN, utils.getSessionUser());
        long insertId = sdb.insert(DBHelper.DATABASE_TABLE, null,
                values);
        Cursor cursor = sdb.query(DBHelper.DATABASE_TABLE,
                COLUMNS, DBHelper.ID_COLUMN + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Message newMSG = cursorToMessage(cursor);
        cursor.close();
        return newMSG;
    }


    public void delete() {
        System.out.println("MSG deleted");
        sdb.delete(DBHelper.DATABASE_TABLE, null, null);
    }

    public void deleteSessionMsg(String session) {
        System.out.println("MSG deleted");
        sdb.delete(DBHelper.DATABASE_TABLE, DBHelper.SESSION_NAME_COLUMN + " = ?", new String[]{session});
    }

    public List<Message> getAllSessionMessage(String session) {
        List<Message> allMSG = new ArrayList<>();

        Cursor cursor = sdb.query(DBHelper.DATABASE_TABLE,
                COLUMNS, DBHelper.SESSION_NAME_COLUMN + " = ?", new String[]{session}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message msg = cursorToMessage(cursor);
            allMSG.add(msg);
            cursor.moveToNext();
        }
        cursor.close();
        return allMSG;
    }
//   Method never used , but it may be necessary in future))
//    public List<Message> getAllMessage() {
//        List<Message> allMSG = new ArrayList<>();
//
//        Cursor cursor = sdb.query(DBHelper.DATABASE_TABLE,
//                COLUMNS, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Message msg = cursorToMessage(cursor);
//            allMSG.add(msg);
//            cursor.moveToNext();
//        }
//        // make sure to close the cursor
//        cursor.close();
//        return allMSG;
//    }

    private Message cursorToMessage(Cursor cursor) {
        Message msg = new Message();

        msg.setUser(cursor.getString(1));
        msg.setMessage(cursor.getString(2));
        msg.setSelf(utils.getSessionUser().equalsIgnoreCase(msg.getUser()));

        return msg;
    }
}
