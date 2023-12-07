package com.example.schedulewozniak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmployeeDB extends SQLiteOpenHelper {
    private static final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE Employee (" +
            "eid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "fName TEXT, lName TEXT, " +
            "nName TEXT," +
            "email TEXT, " +
            "phone TEXT," +
            "active INTEGER " +
            ");";

    private static final String CREATE_AVAILABILITY_TABLE = "CREATE TABLE Availability (" +
            "aid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "eid INTEGER," +
            "day TEXT," +
            "morning_available INTEGER," + // 0 for false, 1 for true
            "evening_available INTEGER, " +  // 0 for false, 1 for true
            "FOREIGN KEY (eid) REFERENCES Employee(eid)" +
            ");";

    private static final String CREATE_DAYS_OFF_TABLE = "CREATE TABLE DaysOff (" +
            "oid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "eid INTEGER," +
            "date TEXT, " +
            "FOREIGN KEY (eid) REFERENCES Employee(eid) " +
            ");";

    private static final String CREATE_TRAINING_TABLE = "CREATE TABLE Training(" +
            "eid INTEGER PRIMARY KEY, " +
            "open INTEGER, close INTEGER, " +
            "FOREIGN KEY (eid) REFERENCES Employee(eid));";

    private static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE Schedule (" +
            "sid INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "date TEXT NOT NULL, " +
            "eid INTEGER NOT NULL, " +
            "shiftTime TEXT NOT NULL, " +
            "dow TEXT, " +
            "empLevel INT, " +
            "FOREIGN KEY (eid) REFERENCES Employee(eid));";


    public EmployeeDB(@Nullable Context context) {
        super(context, "Employees.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEE_TABLE);
        db.execSQL(CREATE_AVAILABILITY_TABLE);
        db.execSQL(CREATE_DAYS_OFF_TABLE);
        db.execSQL(CREATE_TRAINING_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addEmployee(EmployeeModel employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues personal = new ContentValues();
        personal.put("fName", employee.getEmp_FName());
        personal.put("lName", employee.getEmp_LName());
        personal.put("nName", employee.getEmp_nName());
        personal.put("email", employee.getEmp_Email());
        personal.put("phone", employee.getEmp_Phone());
        personal.put("active", 1);

        long empID = db.insert("Employee", null, personal);
        if (empID < 0) {
            return false;
        }

        for (EmployeeAvailability availability : employee.getWeeklyAvailability()) {
            ContentValues avbl = new ContentValues();
            avbl.put("eid", empID);
            avbl.put("day", availability.getDay());
            avbl.put("morning_available", availability.isMorningAvailable());
            avbl.put("evening_available", availability.isEveningAvailable());
            long aid = db.insert("Availability", null, avbl);
            if (aid < 0) {
                return false;
            }
        }

        ContentValues training = new ContentValues();
        training.put("eid", empID);
        training.put("open", employee.isEmp_OpenTrained());
        training.put("close", employee.isEmp_CloseTrained());
        long eid = db.insert("Training", null, training);

        db.close();
        return true;
    }

    public List<EmployeeModel> getAll() {
        List<EmployeeModel> emp_List = new ArrayList<>();
        String query = "SELECT * FROM Employee ;";
        SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int eid = cursor.getInt(0);
                String fName = cursor.getString(1);
                String lName = cursor.getString(2);
                String nName = cursor.getString(3);
                boolean active = cursor.getInt(6) == 1? true: false;
                EmployeeModel employee = new EmployeeModel(eid, fName, lName,nName, active);

                String q2 = "SELECT * FROM Training WHERE eid = " + eid + ";";
                Cursor cursor2 = db.rawQuery(q2,null);
                if(cursor2.moveToFirst()){
                    boolean open = cursor2.getInt(1) == 1? true: false;
                    boolean close = cursor2.getInt(2) == 1? true: false;
                    employee.setEmp_OpenTrained(open);
                    employee.setEmp_CloseTrained(close);
                }
                cursor2.close();

                emp_List.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return emp_List;
    }

    public boolean deleteEmployee(EmployeeModel employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long eid = employee.getEmp_Id();
        ContentValues personal = new ContentValues();

        try {
            db.beginTransaction();
            db.delete("Availability", "eid= ?", new String[]{String.valueOf(eid)});
            db.delete("Availability", "eid= ?", new String[]{String.valueOf(eid)});
            for (EmployeeAvailability availability : employee.getWeeklyAvailability()) {
                ContentValues avbl = new ContentValues();
                avbl.put("eid", employee.getEmp_Id());
                avbl.put("day", availability.getDay());
                avbl.put("morning_available", 0);
                avbl.put("evening_available", 0);
                db.insert("Availability", null, avbl);
            }

            db.delete("DaysOff", "eid= ?", new String[]{String.valueOf(eid)});

            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String where = "eid = ? AND date > ?";
            String[] args = new String[]{String.valueOf(eid), currentDate};
            db.delete("Schedule", where, args);

            personal.put("fName", employee.getEmp_FName());
            personal.put("lName", employee.getEmp_LName());
            personal.put("nName", employee.getEmp_nName());
            personal.put("email", employee.getEmp_Email());
            personal.put("phone", employee.getEmp_Phone());
            personal.put("active", 0);

            db.update("Employee", personal, "eid=?", new String[]{String.valueOf(eid)});

            db.setTransactionSuccessful();
            return true;
        }catch (SQLException e){
            return false;
        }finally{
            db.endTransaction();
            db.close();
        }
    }
    public Boolean updateEmployee(EmployeeModel employee){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues personal = new ContentValues();
        long eid = employee.getEmp_Id();

        db.beginTransaction();

        personal.put("fName", employee.getEmp_FName());
        personal.put("lName", employee.getEmp_LName());
        personal.put("nName", employee.getEmp_nName());
        personal.put("email", employee.getEmp_Email());
        personal.put("phone", employee.getEmp_Phone());

        db.update("Employee", personal, "eid=?", new String[]{String.valueOf(eid)});

        db.delete("Availability", "eid= ?", new String[]{String.valueOf(eid)});
        for (EmployeeAvailability availability : employee.getWeeklyAvailability()) {
            ContentValues avbl = new ContentValues();
            avbl.put("eid", employee.getEmp_Id());
            avbl.put("day", availability.getDay());
            avbl.put("morning_available", availability.isMorningAvailable());
            avbl.put("evening_available", availability.isEveningAvailable());
            db.insert("Availability", null, avbl);
        }

        fixSched(employee);

        ContentValues training = new ContentValues();
        training.put("eid", employee.getEmp_Id());
        training.put("open", employee.isEmp_OpenTrained());
        training.put("close", employee.isEmp_CloseTrained());
        db.update("Training", training, "eid=?", new String[]{String.valueOf(eid)});
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return true;

    }

    public EmployeeModel  getEmpDetails(EmployeeModel employee){
        EmployeeModel moreEmployee;
        long eid = employee.getEmp_Id();
        EmployeeAvailability ea;
        List<EmployeeAvailability> aList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String q1 = "SELECT * FROM Employee WHERE eid = " + eid;
        String q2 = "SELECT * FROM Training WHERE eid = " + eid;
        String q3 = "SELECT * FROM Availability WHERE eid = " + eid;

        Cursor cursor = db.rawQuery(q1, null);
        if(cursor.moveToFirst()){
            String fName = cursor.getString(1);
            String lName = cursor.getString(2);
            String nName = cursor.getString(3);
            String email = cursor.getString(4);
            String phone = cursor.getString(5);
            boolean active = cursor.getInt(6) == 1? true: false;

            moreEmployee = new EmployeeModel(fName, lName, nName, email, phone, active);
            moreEmployee.setEmp_Id((int) eid);
        }else{
            return null;
        }
        cursor.close();

        cursor = db.rawQuery(q2,null);
        if(cursor.moveToFirst()){
            boolean open = cursor.getInt(1) == 1? true: false;
            boolean close = cursor.getInt(2) == 1? true: false;
            moreEmployee.setEmp_OpenTrained(open);
            moreEmployee.setEmp_CloseTrained(close);
        }else{return null;}
        cursor.close();

        cursor = db.rawQuery(q3,null);
        if(cursor.moveToFirst()){
            do{
                String day = cursor.getString(2) ;
                boolean morning = cursor.getInt(3) == 1? true: false;
                boolean afternoon = cursor.getInt(4) == 1? true: false;
                ea = new EmployeeAvailability(day, morning, afternoon);
                aList.add(ea);
            } while(cursor.moveToNext());
        }else{return null;}
        moreEmployee.setEmployeeAvailabilityList(aList);

        cursor.close();;
        db.close();;

        return moreEmployee;
    }

    public List<EmployeeModel> getAvailableEmployees(Date date, String shift, int level){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        String dow = getDOW(date);

        String query = "SELECT * FROM Employee E "; ;

        if(shift.equals("Morning")){
            query += "WHERE E.eid IN (SELECT A.eid FROM Availability A WHERE A.morning_available = 1 AND A.day = '" +
            dow + "' ) AND  E.eid NOT IN (SELECT D.eid FROM DaysOff D WHERE date = '" +
            dateString + "' ) " +
            "AND E.eid NOT IN (SELECT S.eid FROM Schedule S WHERE S.date = '" + dateString + "' AND " +
            "S.shiftTime = 'Morning' AND S.empLevel = " + level + " );";
        } else{
            query += "WHERE E.eid IN (SELECT A.eid FROM Availability A WHERE A.evening_available = 1 AND A.day = '" +
                    dow + "' ) AND  E.eid NOT IN (SELECT D.eid FROM DaysOff D WHERE date = '" +
                    dateString + "' ) "+
                    "AND E.eid NOT IN (SELECT S.eid FROM Schedule S WHERE S.date = '" + dateString + "' AND " +
                    "S.shiftTime = 'Afternoon' AND S.empLevel = " + level + " );";
        }

        List<EmployeeModel> emp_List = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int eid = cursor.getInt(0);
                String fName = cursor.getString(1);
                String lName = cursor.getString(2);
                String nName = cursor.getString(3);
                boolean active = cursor.getInt(6) == 1? true: false;
                EmployeeModel employee = new EmployeeModel(eid, fName, lName,nName, active);

                String q2 = "SELECT * FROM Training WHERE eid = " + eid + ";";
                Cursor cursor2 = db.rawQuery(q2,null);
                if(cursor2.moveToFirst()){
                    boolean open = cursor2.getInt(1) == 1? true: false;
                    boolean close = cursor2.getInt(2) == 1? true: false;
                    employee.setEmp_OpenTrained(open);
                    employee.setEmp_CloseTrained(close);
                }
                cursor2.close();

                emp_List.add(employee);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return emp_List;
        } else {
            cursor.close();
            db.close();
            return null;
        }


    }

    public boolean addSched (Date date, int eid, String shiftTime, int empLevel){
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        String dow = getDOW(date);

        ContentValues schedule = new ContentValues();
        schedule.put("date", dateString);
        schedule.put("eid", eid);
        schedule.put("shiftTime", shiftTime);
        schedule.put("dow", dow);
        schedule.put("empLevel" , empLevel);

        long sID = db.insert("Schedule", null, schedule);
        if (sID < 0) {
            return false;
        }

        return true;
    }

    public EmployeeModel getAssignedEmployee (Date date, String shift, int level){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);

        EmployeeModel employee = new EmployeeModel();

        String shift2 = "abc";
        String query = "SELECT eid FROM Schedule " +
                "WHERE date = '" + dateString  + "' AND shiftTime = '" + shift + "' AND " +
                " empLevel = " + level ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            int eid = cursor.getInt(0);
            employee.setEmp_Id(eid);
            employee = getEmpDetails(employee);
            return employee;
        }

        return null;
    }

    public boolean deleteSched(Date date, String shiftTime, int empLevel){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);

        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "date = ? AND shiftTime = ? AND empLevel = ?";
        String[] whereArgs = new String[] { dateString, shiftTime, String.valueOf(empLevel) };

        db.beginTransaction();
        db.delete("Schedule", whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return true;
    }

    public String getDOW(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Get the day of the week
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Since dayOfWeek is an int, you might want to convert it to a human-readable form
        String[] daysOfWeek = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String dayOfWeekString = daysOfWeek[dayOfWeek];

        return dayOfWeekString;
    }

    public int checkDuplicate (EmployeeModel employee){
        SQLiteDatabase db = this.getReadableDatabase();
        String fName = employee.getEmp_FName();
        String lName = employee.getEmp_LName();
        String nName = employee.getEmp_nName();
        String query;
        String[] args;
        Cursor cursor;
        int eid = employee.getEmp_Id();
        if(nName.isEmpty() ){
            query = "SELECT * FROM Employee WHERE fName COLLATE NOCASE =? AND lName COLLATE NOCASE =? AND nName = '' AND eid !=? ;";
            args = new String[]{fName, lName, String.valueOf(eid)};
            cursor = db.rawQuery(query, args);
            if(cursor.moveToFirst()){
                return 1;
            }
        } else {
            query = "SELECT * FROM Employee WHERE nName COLLATE NOCASE =? AND eid !=?  ;";
            args = new String[]{nName, String.valueOf(eid)};
            cursor = db.rawQuery(query, args);
            if(cursor.moveToFirst()){
                return 2;
            }
        }

        return 0;
    }

    public boolean fixSched (EmployeeModel employee){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args;
        String where = "eid =? AND date >? AND shiftTime =? AND dow =?";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String eid = String.valueOf(employee.getEmp_Id());

        for (EmployeeAvailability avail : employee.getWeeklyAvailability()){
            if(avail.isMorningAvailable() == false){
                args = new String[]{eid, currentDate, "Morning", avail.getDay()};
                db.delete("Schedule", where, args);
            }

            if(avail.isEveningAvailable() == false){
                args = new String[]{eid, currentDate, "Afternoon", avail.getDay()};
                db.delete("Schedule", where, args);
            }
        }

        return true;
    }

    public boolean isScheduled (Date date){
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Get the day of the week
        int dow = calendar.get(Calendar.DAY_OF_WEEK);

        String query = "SELECT * FROM Schedule WHERE date =? ;";
        String[] args = new String[]{dateString};
        int i = 0;

        Cursor cursor = db.rawQuery(query, args);
        if(cursor.moveToFirst()){
            do {
                i++;
            } while (cursor.moveToNext());

            cursor.close();
            db.close();;

            if(dow == 1 || dow == 7){
                if(i< 2){ return false; }
                else {return true;}
            } else{
                if(i < 4){return false;}
                else {return true;}
            }
        }
        return false;
    }
}


