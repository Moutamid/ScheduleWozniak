package com.example.schedulewozniak;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemClickListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.schedulewozniak.databinding.ActivityMainBinding;


public class SecondActivity extends AppCompatActivity {

    // on below line we are creating variables.
    private ListView empLV;
    private Button addBtn;
    private Button changebutton;
    private EditText itemEdt;
    //private ArrayList<String> lngList;
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private Intent myIntnet = null;
    private Intent myIntent2 = null;
    EmployeeDB employeeDB = DatabaseSingleton.getInstance(this).getEmployeeDatabase();
    EmployeeAvailability employeeAvailability;
    private List<EmployeeAvailability> aList = new ArrayList<>();

    private void refreshListView() {
        empLV = findViewById(R.id.idLVLanguages);
        ArrayAdapter<EmployeeModel> adapter = new ArrayAdapter<>(SecondActivity.this, android.R.layout.simple_list_item_1, employeeDB.getAll());
        empLV.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        // on below line we are initializing our variables.
        addBtn = findViewById(R.id.idBtnAdd);
        itemEdt = findViewById(R.id.idEdtItemName);
        //lngList = new ArrayList<>();
        changebutton = findViewById(R.id.button2);

        // on below line we are adding items to our list
        //lngList.add("C++");
        //lngList.add("Python");

        // on the below line we are initializing the adapter for our list view.


        // on below line we are setting adapter for our list view.
        refreshListView();
        // on below line we are adding click listener for our button.

        changebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myIntnet = new Intent(getApplicationContext(), MainActivity.class);

                myIntnet.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntnet);

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent2  = new Intent(SecondActivity.this, EmployeeActivity.class);
                startActivity(myIntent2);
            }

        });

        empLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmployeeModel emp = (EmployeeModel) empLV.getItemAtPosition(position);
                /*Snackbar.make(view, "You Selected: " + emp, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.idLVLanguages)
                        .setAction("Action", null).show();*/
                //employeeDB.deleteEmployee(emp);
                myIntent2  = new Intent(SecondActivity.this, EmployeeActivity.class);
                myIntent2.putExtra("EDIT_THIS_EMPLOYEE", emp);
                startActivity(myIntent2);
                refreshListView();

            }
        });


    }

    protected void onResume() {
        super.onResume();

        // Update the ListView by getting fresh data from EmployeeDB
        refreshListView();
    }

}