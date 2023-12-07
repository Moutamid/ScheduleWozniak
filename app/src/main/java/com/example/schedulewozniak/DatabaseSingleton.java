package com.example.schedulewozniak;

import android.content.Context;

public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private EmployeeDB  employeeDB;

    private DatabaseSingleton(Context context) {
        employeeDB = new EmployeeDB(context);
    }

    public static synchronized DatabaseSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseSingleton(context.getApplicationContext());
        }
        return instance;
    }

    public EmployeeDB getEmployeeDatabase() {
        return employeeDB;
    }
}

