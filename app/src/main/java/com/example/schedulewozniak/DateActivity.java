package com.example.schedulewozniak;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


public class DateActivity extends AppCompatActivity {

    EmployeeDB employeeDB = DatabaseSingleton.getInstance(this).getEmployeeDatabase();
    EmployeeModel employee , employee2, employee3, employee4, employee5;
    List<EmployeeModel> empList = new ArrayList<>();
    List<EmployeeModel> empList2 = new ArrayList<>();
    List<EmployeeModel> empList3 = new ArrayList<>();
    List<EmployeeModel> empList4 = new ArrayList<>();
    List<EmployeeModel> empListX1;
    List<EmployeeModel> empListX2;
    List<EmployeeModel> empListX3;
    List<EmployeeModel> empListX4;
    Button svButton, cancelButton;
    TextView tvMorning, tvAfternoon;

    TextView tvDate;


    View.OnTouchListener doNothingTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true; // Consume the touch event, do nothing
        }
    };


    // Create a View.OnClickListener that does nothing
    View.OnClickListener doNothingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Do nothing
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        // Spinners by their IDs
        Spinner morningSpinner = findViewById(R.id.spMorning);
        Spinner morningSpinner2 = findViewById(R.id.spMorning2);
        Spinner afternoonSpinner = findViewById(R.id.spAfternoon);
        Spinner afternoonSpinner2 = findViewById(R.id.spAfternoon2);

        svButton = findViewById(R.id.svButton);
        cancelButton = findViewById(R.id.cancelButton);

        tvDate = findViewById(R.id.tvDate);
        tvMorning = findViewById(R.id.tvMorning);
        tvAfternoon = findViewById(R.id.tvAfternoon);

        Intent intent = getIntent();

        employee = new EmployeeModel();
        employee.setEmp_FName("Not");
        employee.setEmp_LName("Assigned");
        employee.setEmp_Id(-1);

        empList.add(employee);
        empList2.add(employee);
        empList3.add(employee);
        empList4.add(employee);

        Calendar selectedCalendar = (Calendar) intent.getSerializableExtra("selectedCalendar");
        Date date = selectedCalendar.getTime();
        int dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        String[] daysOfWeek = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String dayOfWeekString = daysOfWeek[dayOfWeek];
        String findalDate = dayOfWeekString + ", " + dateString;

        Date d1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date currentDate = cal.getTime();

        tvDate.setText(findalDate);

        if ((dayOfWeek == 1) || (dayOfWeek == 7)){
            tvMorning.setText("All Day");
            tvAfternoon.setVisibility(View.GONE); // Set it to View.GONE
            afternoonSpinner.setVisibility(View.GONE);
            afternoonSpinner2.setVisibility(View.GONE);

            employee2 = employeeDB.getAssignedEmployee(date, "Afternoon", 1);
            if (employee2 != null) {
                empList.add(employee2);
            }

            employee3 = employeeDB.getAssignedEmployee(date, "Afternoon", 2);
            if (employee3 != null) {
                empList2.add(employee3);
            }

            if( (empListX1 = employeeDB.getAvailableEmployees(date,"Afternoon",1)) != null) {
                for (EmployeeModel emp : empListX1) {
                    empList.add(emp);
                }

               if ( (empListX2 = employeeDB.getAvailableEmployees(date, "Afternoon", 2)) != null) {
                   for (EmployeeModel emp : empListX2) {
                       empList2.add(emp);
                   }
               }
            }

                ArrayAdapter<EmployeeModel> m1 = new ArrayAdapter<>(DateActivity.this, android.R.layout.simple_spinner_item, empList);
                m1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                morningSpinner.setAdapter(m1);

                ArrayAdapter<EmployeeModel> m2 = new ArrayAdapter<>(DateActivity.this, android.R.layout.simple_spinner_item, empList2);
                m2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                morningSpinner2.setAdapter(m2);

                if (employee2 != null) {
                    morningSpinner.setSelection(1);
                }
                if (employee3 != null) {
                    morningSpinner2.setSelection(1);
                }

            if (currentDate.after(date)) {
                morningSpinner.setOnTouchListener(doNothingTouchListener);
                morningSpinner2.setOnTouchListener(doNothingTouchListener);

                // Disable Button
                svButton.setEnabled(false);
                svButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }

        }else {
            employee2 = employeeDB.getAssignedEmployee(date, "Morning", 1);
            if (employee2 != null) {
                empList.add(employee2);
            }
            employee3 = employeeDB.getAssignedEmployee(date, "Morning", 2);
            if (employee3 != null) {
                empList2.add(employee3);
            }

            employee4 = employeeDB.getAssignedEmployee(date, "Afternoon", 1);
            if (employee4 != null) {
                empList3.add(employee4);
            }

            employee5 = employeeDB.getAssignedEmployee(date, "Afternoon", 2);
            if (employee5 != null) {
                empList4.add(employee5);
            }


            if( (empListX1 = employeeDB.getAvailableEmployees(date,"Morning", 1)) != null) {
                for (EmployeeModel emp : empListX1) {
                    empList.add(emp);
                }
                empListX2 = employeeDB.getAvailableEmployees(date, "Morning", 2);
                empList2.addAll(empListX2);
            }

            if((empListX3 = employeeDB.getAvailableEmployees(date, "Afternoon", 1)) != null) {
                for (EmployeeModel emp : empListX3) {
                    empList3.add(emp);
                }

                empListX4 = employeeDB.getAvailableEmployees(date, "Afternoon", 2);
                empList4.addAll(empListX4);
            }

            ArrayAdapter<EmployeeModel> m1 = new ArrayAdapter<>(DateActivity.this, android.R.layout.simple_spinner_item, empList);
            m1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            morningSpinner.setAdapter(m1);

            ArrayAdapter<EmployeeModel> m2 = new ArrayAdapter<>(DateActivity.this, android.R.layout.simple_spinner_item, empList2);
            m2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            morningSpinner2.setAdapter(m2);

            ArrayAdapter<EmployeeModel> a1 = new ArrayAdapter<>(DateActivity.this, android.R.layout.simple_spinner_item, empList3);
            a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            afternoonSpinner.setAdapter(a1);

            ArrayAdapter<EmployeeModel> a2 = new ArrayAdapter<>(DateActivity.this, android.R.layout.simple_spinner_item, empList4);
            a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            afternoonSpinner2.setAdapter(a2);

            if (employee2 != null) {
                morningSpinner.setSelection(1);
            }
            if (employee3 != null) {
                morningSpinner2.setSelection(1);
            }
            if (employee4 != null) {
                afternoonSpinner.setSelection(1);
            }
            if (employee5 != null) {
                afternoonSpinner2.setSelection(1);
            }

            if (currentDate.after(date)) {
                morningSpinner.setOnTouchListener(doNothingTouchListener);
                morningSpinner2.setOnTouchListener(doNothingTouchListener);
                afternoonSpinner.setOnTouchListener(doNothingTouchListener);
                afternoonSpinner2.setOnTouchListener(doNothingTouchListener);

                // Disable Button
                svButton.setEnabled(false);
                svButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }

        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        svButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Calendar selectedCalendar = (Calendar) intent.getSerializableExtra("selectedCalendar");
                Date date = selectedCalendar.getTime();
                int dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

                EmployeeModel emp1, emp2, emp3, emp4;

                emp1 = (EmployeeModel) morningSpinner.getSelectedItem();
                emp2 = (EmployeeModel) morningSpinner2.getSelectedItem();


                if((dayOfWeek == 1) || (dayOfWeek == 7)){
                    if(emp1.getEmp_Id() == emp2.getEmp_Id()){

                        if (emp1.getEmp_Id() != -1){
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);
                            Toast.makeText(getApplicationContext(), "<<Shift Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(emp2.getEmp_Id() == -1){
                        employeeDB.deleteSched(date, "Afternoon", 1);
                        employeeDB.deleteSched(date, "Afternoon", 2);
                        employeeDB.addSched(date, emp1.getEmp_Id(), "Afternoon", 1);
                    }
                    else if(emp1.getEmp_Id() == -1){
                        employeeDB.deleteSched(date, "Afternoon", 1);
                        employeeDB.deleteSched(date, "Afternoon", 2);
                        employeeDB.addSched(date, emp2.getEmp_Id(), "Afternoon", 2);
                    } else {
                        if((emp1.isEmp_OpenTrained() == true) && (emp2.isEmp_CloseTrained() == true)){
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);
                            employeeDB.addSched(date, emp1.getEmp_Id(), "Afternoon", 1);
                            employeeDB.addSched(date, emp2.getEmp_Id(), "Afternoon", 2);
                        } else if((emp2.isEmp_OpenTrained() == true) && (emp1.isEmp_CloseTrained() == true)){
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);
                            employeeDB.addSched(date, emp1.getEmp_Id(), "Afternoon", 1);
                            employeeDB.addSched(date, emp2.getEmp_Id(), "Afternoon", 2);
                        } else if((emp1.isEmp_OpenTrained() == true) && (emp1.isEmp_CloseTrained() == true)){
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);
                            employeeDB.addSched(date, emp1.getEmp_Id(), "Afternoon", 1);
                            employeeDB.addSched(date, emp2.getEmp_Id(), "Afternoon", 2);
                        } else if((emp2.isEmp_OpenTrained() == true) && (emp2.isEmp_CloseTrained() == true)){
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);
                            employeeDB.addSched(date, emp1.getEmp_Id(), "Afternoon", 1);
                            employeeDB.addSched(date, emp2.getEmp_Id(), "Afternoon", 2);
                        } else{
                            Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    emp3 = (EmployeeModel) afternoonSpinner.getSelectedItem();
                    emp4 = (EmployeeModel) afternoonSpinner2.getSelectedItem();

                    if (emp1.getEmp_Id() == -1){
                        if((emp3.getEmp_Id() == -1) && (emp4.getEmp_Id() == -1) && (emp2.getEmp_Id() != -1)){
                            employeeDB.deleteSched(date, "Morning", 1);
                            employeeDB.deleteSched(date, "Morning", 2);
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);

                            employeeDB.deleteSched(date, "Morning", 2);
                            employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                        } else if((emp2.getEmp_Id() == -1) && (emp4.getEmp_Id() == -1) && (emp3.getEmp_Id() != -1)){
                            employeeDB.deleteSched(date, "Morning", 1);
                            employeeDB.deleteSched(date, "Morning", 2);
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);

                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                        } else if((emp3.getEmp_Id() == -1) && (emp2.getEmp_Id() == -1) && (emp4.getEmp_Id() != -1)){
                            employeeDB.deleteSched(date, "Morning", 1);
                            employeeDB.deleteSched(date, "Morning", 2);
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);

                            employeeDB.deleteSched(date, "Afternoon", 2);
                            employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                        }

                        else if((emp3.getEmp_Id() == -1) && (emp4.getEmp_Id() != -1) && (emp2.getEmp_Id() != -1)){
                            if (emp2.getEmp_Id() != emp4.getEmp_Id()) {
                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.deleteSched(date, "Afternoon", 2);

                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 2);
                                employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if((emp2.getEmp_Id() != -1) && (emp4.getEmp_Id() == -1) && (emp3.getEmp_Id() != -1)){
                            if (emp2.getEmp_Id() != emp3.getEmp_Id()) {
                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.deleteSched(date, "Afternoon", 2);

                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if((emp3.getEmp_Id() != -1) && (emp2.getEmp_Id() == -1) && (emp4.getEmp_Id() != -1)){
                            if (emp4.getEmp_Id() != emp3.getEmp_Id()) {
                                if((emp3.isEmp_CloseTrained() == true) || (emp4.isEmp_CloseTrained() == true)) {
                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.deleteSched(date, "Afternoon", 2);

                                    employeeDB.deleteSched(date, "Afternoon", 2);
                                    employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if((emp3.getEmp_Id() != -1) && (emp2.getEmp_Id() != -1) && (emp4.getEmp_Id() != -1)){
                            if((emp2.getEmp_Id() != emp3.getEmp_Id()) && (emp2.getEmp_Id() != emp4.getEmp_Id()) && (emp3.getEmp_Id() != emp4.getEmp_Id())){
                                if((emp3.isEmp_CloseTrained() == true) || (emp4.isEmp_CloseTrained() == true)) {
                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.deleteSched(date, "Afternoon", 2);

                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 2);
                                    employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } else {
                            employeeDB.deleteSched(date, "Morning", 1);
                            employeeDB.deleteSched(date, "Morning", 2);
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);

                            Toast.makeText(getApplicationContext(), "<<Shift deleted>>", Toast.LENGTH_SHORT).show();
                        }
                    } else if (emp2.getEmp_Id() == -1){
                        if((emp3.getEmp_Id() == -1) && (emp4.getEmp_Id() == -1) && (emp1.getEmp_Id() != -1)){
                            employeeDB.deleteSched(date, "Morning", 1);
                            employeeDB.deleteSched(date, "Morning", 2);
                            employeeDB.deleteSched(date, "Afternoon", 1);
                            employeeDB.deleteSched(date, "Afternoon", 2);

                            employeeDB.deleteSched(date, "Morning", 1);
                            employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                        } else if((emp3.getEmp_Id() == -1) && (emp4.getEmp_Id() != -1) && (emp1.getEmp_Id() != -1)){
                            if (emp1.getEmp_Id() != emp4.getEmp_Id()) {
                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.deleteSched(date, "Afternoon", 2);

                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                                employeeDB.deleteSched(date, "Afternoon", 2);
                                employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if((emp1.getEmp_Id() != -1) && (emp4.getEmp_Id() == -1) && (emp3.getEmp_Id() != -1)){
                            if (emp1.getEmp_Id() != emp3.getEmp_Id()) {
                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.deleteSched(date, "Afternoon", 2);

                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }  else if((emp3.getEmp_Id() != -1) && (emp1.getEmp_Id() != -1) && (emp4.getEmp_Id() != -1)){
                            if((emp1.getEmp_Id() != emp3.getEmp_Id()) && (emp1.getEmp_Id() != emp4.getEmp_Id()) && (emp3.getEmp_Id() != emp4.getEmp_Id())){
                                if((emp3.isEmp_CloseTrained() == true) || (emp4.isEmp_CloseTrained() == true)) {
                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.deleteSched(date, "Afternoon", 2);

                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                                    employeeDB.deleteSched(date, "Afternoon", 2);
                                    employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    } else if (emp3.getEmp_Id() == -1){
                        if(emp4.getEmp_Id() == -1){
                            if (emp1.getEmp_Id() != emp2.getEmp_Id()) {
                                if((emp1.isEmp_OpenTrained() == true) || (emp2.isEmp_OpenTrained() == true)) {
                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.deleteSched(date, "Afternoon", 2);

                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            if ((emp1.getEmp_Id() != emp2.getEmp_Id()) && (emp1.getEmp_Id() != emp4.getEmp_Id()) && (emp4.getEmp_Id() != emp2.getEmp_Id())  ){
                                if((emp1.isEmp_OpenTrained() == true) || (emp2.isEmp_OpenTrained() == true)) {
                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 1);
                                    employeeDB.deleteSched(date, "Afternoon", 2);

                                    employeeDB.deleteSched(date, "Morning", 1);
                                    employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                                    employeeDB.deleteSched(date, "Morning", 2);
                                    employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                                    employeeDB.deleteSched(date, "Afternoon", 2);
                                    employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } else if (emp4.getEmp_Id() == -1){
                        if ((emp1.getEmp_Id() != emp2.getEmp_Id()) && (emp1.getEmp_Id() != emp3.getEmp_Id()) && (emp3.getEmp_Id() != emp2.getEmp_Id())  ){
                            if((emp1.isEmp_OpenTrained() == true) || (emp2.isEmp_OpenTrained() == true)) {
                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.deleteSched(date, "Afternoon", 2);

                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        if (emp1.getEmp_Id() == emp2.getEmp_Id()){
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (emp1.getEmp_Id() == emp3.getEmp_Id()){
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (emp1.getEmp_Id() == emp4.getEmp_Id()){
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (emp3.getEmp_Id() == emp2.getEmp_Id()){
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (emp3.getEmp_Id() == emp4.getEmp_Id()){
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (emp4.getEmp_Id() == emp2.getEmp_Id()){
                            Toast.makeText(getApplicationContext(), "Cannot use same employee multiple times", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if (((emp1.isEmp_OpenTrained() == true) || (emp2.isEmp_OpenTrained() == true)) && ((emp3.isEmp_CloseTrained() == true) || (emp4.isEmp_CloseTrained() == true))) {
                                employeeDB.deleteSched(date, "Morning", 1);
                                employeeDB.deleteSched(date, "Morning", 2);
                                employeeDB.deleteSched(date, "Afternoon", 1);
                                employeeDB.deleteSched(date, "Afternoon", 2);

                                employeeDB.addSched(date, emp1.getEmp_Id(), "Morning", 1);
                                employeeDB.addSched(date, emp2.getEmp_Id(), "Morning", 2);
                                employeeDB.addSched(date, emp3.getEmp_Id(), "Afternoon", 1);
                                employeeDB.addSched(date, emp4.getEmp_Id(), "Afternoon", 2);
                            } else {
                                Toast.makeText(getApplicationContext(), "Training Criteria not met", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    }

                }



                setResult(RESULT_OK);
                finish();
            }
        });


    }
}