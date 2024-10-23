package core.delta.ticketsystem.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TicketDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteOpenHelper";

    // database schema (constants)
    public static final String TICKETS_TABLE    = "tickets_table";

    public static final String COL_ID           = "id";
    public static final String COL_DATE         = "date";

    public static final String COL_STATE        = "state";
    public static final String COL_PRIORITY     = "priority";
    public static final String COL_DEPARTMENT   = "department";
    public static final String COL_MANDATE      = "mandate";
    public static final String COL_TARGET_DATE  = "target_date";

    public static final String COL_SUBJECT      = "subject";
    public static final String COL_DESCRIPTION  = "description";

    public static final String COL_USER_ID      = "user_id";
    public static final String COL_USER_NAME    = "user_name";

    public TicketDBHelper(@Nullable Context context) {
        // set some default parameters for name, factory, version
        super(context, "ticket_system.db", null, 1);
    }

    // called first time a database is accessed, needs code to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDatabaseStatement = "CREATE TABLE " + TICKETS_TABLE + " ("
                + COL_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE          + " TEXT, "
                + COL_STATE         + " INTEGER, "
                + COL_PRIORITY      + " INTEGER, "
                + COL_DEPARTMENT    + " TEXT, "
                + COL_MANDATE       + " TEXT, "
                + COL_TARGET_DATE   + " TEXT, "
                + COL_SUBJECT       + " TEXT, "
                + COL_DESCRIPTION   + " TEXT, "
                + COL_USER_ID       + " INTEGER, "
                + COL_USER_NAME     + " TEXT)";

        db.execSQL(createDatabaseStatement);

        // add some sample data to newly installed app
        initSampleData(db);
    }

    // called if the database version # changes. it prevents previous users app from breaking you
    // change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(TicketModel ticket) {

        SQLiteDatabase db = this.getWritableDatabase();
        return addOne(db, ticket);
    }

    public boolean addOne(SQLiteDatabase db, TicketModel ticket) {

        // like a hashmap: holding key+value pairs
        ContentValues cv = getContentValues(ticket);

        long insert = db.insert(TICKETS_TABLE, null,cv);
        if (insert == -1) {
            return false;
        } else {
            // set id to response of inserted new row above
            ticket.setId((int) insert);
            return true;
        }
    }


    public void updateOne(TicketModel ticket) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = getContentValues(ticket);

        // update our database and passing our values.
        // and we are comparing it with ID of our stored in original ID variable.
        db.update(TICKETS_TABLE, cv, COL_ID + "=?", new String[]{String.valueOf(ticket.getId())});
        db.close();
    }

    @NonNull
    private static ContentValues getContentValues(TicketModel ticket) {
        // like a hashmap: holding key+value pairs
        ContentValues cv = new ContentValues();

        // passing all values along with its key and value pair.
        cv.put(COL_DATE, ticket.getDate());
        cv.put(COL_STATE, ticket.getState());
        cv.put(COL_PRIORITY, ticket.getPriority());
        cv.put(COL_DEPARTMENT, ticket.getDepartment());
        cv.put(COL_MANDATE, ticket.getMandate());
        cv.put(COL_TARGET_DATE, ticket.getTargetDate());
        cv.put(COL_SUBJECT, ticket.getSubject());
        cv.put(COL_DESCRIPTION, ticket.getDescription());
        cv.put(COL_USER_ID, ticket.getUserId());
        cv.put(COL_USER_NAME, ticket.getUserName());
        return cv;
    }

    public boolean deleteOne(TicketModel ticket) {
        // find TicketModel in the database. if found, delete it and return true, else return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TICKETS_TABLE + " WHERE " + COL_ID + " = " + ticket.getId();

        try (Cursor cursor = db.rawQuery(queryString, null)) {
            return cursor.moveToFirst();
        }
    }

    // unused in this project
    public void updateAllPositions(ArrayList<TicketModel> dataSet) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // update dataSet + DB by setting new positions in relation to the ArrayList
        for (int i = 0; i < dataSet.size(); i++) {
            TicketModel ticket = dataSet.get(i);
            // update dataSet first
//            ticket.setPosition(i);

            // passing all values along with its key and value pair.
            values.put(COL_STATE, i);
            // update database by comparing it with ID
            db.update(TICKETS_TABLE, values, COL_ID + "=?", new String[]{String.valueOf(ticket.getId())});
        }
        db.close();
    }

    public ArrayList<TicketModel> getAll() {
        ArrayList<TicketModel> ticketList = new ArrayList<>();

        // get data from database
//        String queryString = "SELECT * FROM " + TICKETS_TABLE + " ORDER BY " + COL_STATE;
        String queryString = "SELECT * FROM " + TICKETS_TABLE;

        // read only access doesn't block database
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new custom objects
            // put them into returnList
            do {
                int id              = cursor.getInt(0);
                String date         = cursor.getString(1);
                int state           = cursor.getInt(2);
                int priority        = cursor.getInt(3);
                String department   = cursor.getString(4);
                String mandate      = cursor.getString(5);
                String targetDate   = cursor.getString(6);
                String subject      = cursor.getString(7);
                String description  = cursor.getString(8);
                int userId          = cursor.getInt(9);
                String userName     = cursor.getString(10);

                //boolean ticketState = cursor.getInt(3) == 1 ? true : false;

                TicketModel newTicket = new TicketModel(id, date, state, priority, department,
                        mandate, targetDate, subject, description, userId, userName);
                ticketList.add(newTicket);

            } while (cursor.moveToNext());

        } else {
            // failure do not add anything to the list
        }

        // close database connections
        cursor.close();
        db.close();

        return ticketList;
    }

    private void initSampleData(SQLiteDatabase db) {
        addOne(db, new TicketModel(
                20,
                "01.07.2024",
                2,
                0,
                "Abteilung 01",
                "IT Support",
                "tDate",
                "Update Fehler",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr,\n\nsed diam nonumy eirmod " +
                        "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                        "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                        "no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor " +
                        "sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod",
                -1,
                "Superuser"));

        addOne(db, new TicketModel(
                42,
                "01.07.2024",
                3,
                2,
                "Abteilung 02",
                "IT Support",
                "tDate",
                "Hardware",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy 🤔\n\n" +
                        "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                        "At vero eos et accusam et\n\njusto duo dolores et ea rebum. Stet clita kasd gubergren, " +
                        "no sea takimata. ",
                -1,
                "Poweruser"));

        addOne(db, new TicketModel(
                43,
                "03.07.2024",
                0,
                2,
                "Abteilung 03",
                "IT Devs",
                "tDate",
                "Mobile Darstellung",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy " +
                        "eirmod tempor invidunt",
                -1,
                "Secretuser"));

        addOne(db, new TicketModel(
                44,
                "01.07.2024",
                1,
                3,
                "SitAmet 04",
                "IT Support",
                "tDate",
                "Backup fehlerhaft",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam \nnonumy eirmod tempor " +
                        "invidunt ut \nlabore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                        "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata",
                -1,
                "Simpleuser"));

        addOne(db, new TicketModel(
                45,
                "09.07.2024",
                4,
                3,
                "Consetetur 05",
                "IT Devs",
                "tDate",
                "Kontakformular sendet nicht",
                "Lorem ipsum, \ndolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                        "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam " +
                        "et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem " +
                        "ipsum dolor sit \n\namet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam " +
                        "nonumy eirmod",
                -1,
                "Stealthuser"));

        addOne(db, new TicketModel(
                60,
                "09.07.2024",
                2,
                2,
                "Abteilung 01",
                "Consulting",
                "tDate",
                "IT Consulting",
                "Lorem ipsum dolor sit amet, \nconsetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                        "invidunt ut labore et dolore 👍",
                -1,
                "Newuser"));

        addOne(db, new TicketModel(
                62,
                "09.07.2024",
                3,
                1,
                "Abteilung 02",
                "IT Support",
                "tDate",
                "Sytem überlastet ",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                        "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam " +
                        "et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est " +
                        "Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed " +
                        "diam nonumy eirmod 🤔",
                -1,
                "Heavyuser"));

        addOne(db, new TicketModel(
                63,
                "17.07.2024",
                2,
                1,
                "Abteilung 44",
                "mandate",
                "tDate",
                "test",
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam " +
                        "et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est ",
                -1,
                "user"));

        addOne(db, new TicketModel(
                64,
                "17.07.2024",
                1,
                1,
                "Abteilung 55",
                "mandate",
                "tDate",
                "test 4",
                "user missing ",
                -1,
                "trialuser"));
    }
/*
    private void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
*/
}
