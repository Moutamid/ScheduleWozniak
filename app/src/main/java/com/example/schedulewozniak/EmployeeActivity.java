package com.example.schedulewozniak;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class EmployeeActivity extends AppCompatActivity {

    // Declare variables for views
    private TextInputLayout txFirstName, txLastName, txNickname, txEmail, txPhoneNumber;
    private Button saveButton, deleteButton;
    private Spinner spinnerCert, spinnerMon, spinnerTue, spinnerWed, spinnerThu, spinnerFri, spinnerSat, spinnerSun;
    EmployeeDB employeeDB = DatabaseSingleton.getInstance(this).getEmployeeDatabase();
    EmployeeModel newEmployee;
    EmployeeModel currentEmployee = null;

    // Check the email format for validation
    private boolean isValidEmail(String email) {
        // Define a regular expression pattern for a valid email address
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z.]{2,}";

        // Use the pattern to match the email
        return email.matches(emailPattern);
    }

    // Check the phone number (10 digits) for validation
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Remove any non-numeric characters from the phone number
        String numericPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        // Check if the numeric phone number has exactly 10 digits
        return numericPhoneNumber.length() == 10;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_employee_fragment);

        // Initialize views
        txFirstName = findViewById(R.id.etFirstName);
        txLastName = findViewById(R.id.etLastName);
        txNickname = findViewById(R.id.etNickname);
        txEmail = findViewById(R.id.etEmail);
        txPhoneNumber = findViewById(R.id.etPhoneNumber);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Get the edit texts from text input layout
        EditText etFirstName = txFirstName.getEditText();
        EditText etLastName = txLastName.getEditText();
        EditText etNickname = txNickname.getEditText();
        EditText etEmail = txEmail.getEditText();
        EditText etPhoneNumber = txPhoneNumber.getEditText();

        // Initialize Spinners
        spinnerCert = findViewById(R.id.spCertification);
        spinnerMon = findViewById(R.id.spinnerMon);
        spinnerTue = findViewById(R.id.spinnerTue);
        spinnerWed = findViewById(R.id.spinnerWed);
        spinnerThu = findViewById(R.id.spinnerThu);
        spinnerFri = findViewById(R.id.spinnerFri);
        spinnerSat = findViewById(R.id.spinnerSat);
        spinnerSun = findViewById(R.id.spinnerSun);

        List<String > weekend= new ArrayList<>();
        weekend.add(("Unavailable"));
        weekend.add("Full");
        ArrayAdapter<String> spinnerSatSunAdapter = new ArrayAdapter<>(EmployeeActivity.this, android.R.layout.simple_spinner_item, weekend);
        spinnerSatSunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSat.setAdapter(spinnerSatSunAdapter);
        spinnerSun.setAdapter(spinnerSatSunAdapter);

        List<String > certifs = new ArrayList<>();
        certifs.add(("None"));
        certifs.add("Opening");
        certifs.add(("Closing"));
        certifs.add("Both");
        ArrayAdapter<String> spinnerCOptions = new ArrayAdapter<>(EmployeeActivity.this, android.R.layout.simple_spinner_item, certifs);
        spinnerCOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSat.setAdapter(spinnerSatSunAdapter);
        spinnerCert.setAdapter(spinnerCOptions);

        if(getIntent().getExtras() != null){

            deleteButton.setVisibility(View.VISIBLE);
            newEmployee = (EmployeeModel) getIntent().getSerializableExtra("EDIT_THIS_EMPLOYEE");

            newEmployee = employeeDB.getEmpDetails(newEmployee);
            etFirstName.setText(newEmployee.getEmp_FName());
            etNickname.setText(newEmployee.getEmp_nName());
            etLastName.setText(newEmployee.getEmp_LName());
            etPhoneNumber.setText(newEmployee.getEmp_Phone());
            etEmail.setText(newEmployee.getEmp_Email());
            if ((newEmployee.isEmp_OpenTrained() == true) && (newEmployee.isEmp_CloseTrained() == true)){
                spinnerCert.setSelection(3);
            }
            else if(newEmployee.isEmp_OpenTrained() == true){
                spinnerCert.setSelection(1);
            }
            else if(newEmployee.isEmp_CloseTrained() == true){
                spinnerCert.setSelection(2);
            }

            for(EmployeeAvailability availability : newEmployee.getWeeklyAvailability()){
                if(availability.getDay().equals("Monday")){
                    if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == false){
                        spinnerMon.setSelection(1);
                    }
                    else if(availability.isMorningAvailable() == false && availability.isEveningAvailable() == true){
                        spinnerMon.setSelection(2);
                    }
                    else if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == true){
                        spinnerMon.setSelection(3);
                    } else{ spinnerMon.setSelection(0);}
                }

                if(availability.getDay().equals("Tuesday")){
                    if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == false){
                        spinnerTue.setSelection(1);
                    }
                    else if(availability.isMorningAvailable() == false && availability.isEveningAvailable() == true){
                        spinnerTue.setSelection(2);
                    }
                    else if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == true){
                        spinnerTue.setSelection(3);
                    } else{ spinnerTue.setSelection(0);}
                }

                if(availability.getDay().equals("Wednesday")){
                    if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == false){
                        spinnerWed.setSelection(1);
                    }
                    else if(availability.isMorningAvailable() == false && availability.isEveningAvailable() == true){
                        spinnerWed.setSelection(2);
                    }
                    else if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == true){
                        spinnerWed.setSelection(3);
                    } else{ spinnerWed.setSelection(0);}
                }

                if(availability.getDay().equals("Thursday")){
                    if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == false){
                        spinnerThu.setSelection(1);
                    }
                    else if(availability.isMorningAvailable() == false && availability.isEveningAvailable() == true){
                        spinnerThu.setSelection(2);
                    }
                    else if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == true){
                        spinnerThu.setSelection(3);
                    } else{ spinnerThu.setSelection(0);}
                }

                if(availability.getDay().equals("Friday")){
                    if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == false){
                        spinnerFri.setSelection(1);
                    }
                    else if(availability.isMorningAvailable() == false && availability.isEveningAvailable() == true){
                        spinnerFri.setSelection(2);
                    }
                    else if(availability.isMorningAvailable() == true && availability.isEveningAvailable() == true){
                        spinnerFri.setSelection(3);
                    } else{ spinnerFri.setSelection(0);}
                }

                if(availability.getDay().equals("Saturday")){
                    if(availability.isEveningAvailable() == true){
                        spinnerSat.setSelection(1);
                    }else{ spinnerSat.setSelection(0);}
                }

                if(availability.getDay().equals("Sunday")){
                    if(availability.isEveningAvailable() == true){
                        spinnerSun.setSelection(1);
                    }else{ spinnerSun.setSelection(0);}
                }

            }

            if(newEmployee.isEmp_Active() == false){

                spinnerCert.setEnabled(false);
                spinnerMon.setEnabled(false);
                spinnerTue.setEnabled(false);
                spinnerWed.setEnabled(false);
                spinnerThu.setEnabled(false);
                spinnerFri.setEnabled(false);
                spinnerSat.setEnabled(false);
                spinnerSun.setEnabled(false);

                deleteButton.setVisibility(View.GONE);
            }

        } else{
            deleteButton.setVisibility(View.GONE);
        }

        // Set click listener for the save button


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getIntent().getExtras() != null) {
                    newEmployee = (EmployeeModel) getIntent().getSerializableExtra("EDIT_THIS_EMPLOYEE");
                } else {
                    newEmployee = new EmployeeModel();
                    newEmployee.setEmp_Id(-1);
                }

                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String nickname = etNickname.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();

                newEmployee.setEmp_FName(firstName);
                newEmployee.setEmp_LName(lastName);
                newEmployee.setEmp_nName(nickname);
                newEmployee.setEmp_Email(email);
                newEmployee.setEmp_Phone(phoneNumber);

                boolean hasError = false; // A flag to track if there are input errors

                // Check if fields are empty and show error messages
                if (firstName.isEmpty()) {
                    txFirstName.setError("First name is required");
                    hasError = true;
                } else {
                    txFirstName.setError(null); // Clear the error
                }

                if (lastName.isEmpty()) {
                    txLastName.setError("Last name is required");
                    hasError = true;
                } else {
                    txLastName.setError(null);
                }

                if (email.isEmpty()) {
                    txEmail.setError("Email is required");
                    hasError = true;
                } else if (!isValidEmail(email)) {
                    txEmail.setError("Invalid email address");
                    hasError = true;
                } else {
                    txEmail.setError(null);
                }

                if (phoneNumber.isEmpty()) {
                    txPhoneNumber.setError("Phone number is required");
                    hasError = true;
                } else if (!isValidPhoneNumber(phoneNumber)) {
                    txPhoneNumber.setError("Invalid 10-digit phone number");
                    hasError = true;
                } else {
                    txPhoneNumber.setError(null);
                }

                // If there are input errors, stop.
                if (hasError) {
                    return;
                }

                //Training
                if(spinnerCert.getSelectedItem().toString().trim().equals("None")){
                    newEmployee.setEmp_OpenTrained(false);
                    newEmployee.setEmp_CloseTrained(false);
                }else if(spinnerCert.getSelectedItem().toString().trim().equals("Opening")){
                    newEmployee.setEmp_OpenTrained(true);
                    newEmployee.setEmp_CloseTrained(false);
                } else if(spinnerCert.getSelectedItem().toString().trim().equals("Closing")){
                    newEmployee.setEmp_OpenTrained(false);
                    newEmployee.setEmp_CloseTrained(true);
                } else{
                    newEmployee.setEmp_OpenTrained(true);
                    newEmployee.setEmp_CloseTrained(true);
                }

                EmployeeAvailability avlMon, avlTue, avlWed, avlThu, avlFri, avlSat, avlSun;
                List<EmployeeAvailability> aList = new ArrayList<>();

                avlMon = new EmployeeAvailability();
                avlTue = new EmployeeAvailability();
                avlWed = new EmployeeAvailability();
                avlThu = new EmployeeAvailability();
                avlFri = new EmployeeAvailability();
                avlSat = new EmployeeAvailability();
                avlSun = new EmployeeAvailability();

                if(spinnerMon.getSelectedItem().toString().trim().equals("Morning")){
                    avlMon.setDay("Monday");
                    avlMon.setMorningAvailable(true);
                    avlMon.setEveningAvailable(false);
                } else if (spinnerMon.getSelectedItem().toString().trim().equals("Afternoon")){
                    avlMon.setDay("Monday");
                    avlMon.setMorningAvailable(false);
                    avlMon.setEveningAvailable(true);
                }else if (spinnerMon.getSelectedItem().toString().trim().equals("Both")){
                    avlMon.setDay("Monday");
                    avlMon.setMorningAvailable(true);
                    avlMon.setEveningAvailable(true);
                }else{
                    avlMon.setDay("Monday");
                    avlMon.setMorningAvailable(false);
                    avlMon.setEveningAvailable(false);
                }
                aList.add(avlMon);

                if(spinnerTue.getSelectedItem().toString().trim().equals("Morning")){
                    avlTue.setDay("Tuesday");
                    avlTue.setMorningAvailable(true);
                    avlTue.setEveningAvailable(false);
                } else if (spinnerTue.getSelectedItem().toString().trim().equals("Afternoon")){
                    avlTue.setDay("Tuesday");
                    avlTue.setMorningAvailable(false);
                    avlTue.setEveningAvailable(true);
                }else if (spinnerTue.getSelectedItem().toString().trim().equals("Both")){
                    avlTue.setDay("Tuesday");
                    avlTue.setMorningAvailable(true);
                    avlTue.setEveningAvailable(true);
                }else{
                    avlTue.setDay("Tuesday");
                    avlTue.setMorningAvailable(false);
                    avlTue.setEveningAvailable(false);
                }
                aList.add(avlTue);

                if(spinnerWed.getSelectedItem().toString().trim().equals("Morning")){
                    avlWed.setDay("Wednesday");
                    avlWed.setMorningAvailable(true);
                    avlWed.setEveningAvailable(false);
                } else if (spinnerWed.getSelectedItem().toString().trim().equals("Afternoon")){
                    avlWed.setDay("Wednesday");
                    avlWed.setMorningAvailable(false);
                    avlWed.setEveningAvailable(true);
                }else if (spinnerWed.getSelectedItem().toString().trim().equals("Both")){
                    avlWed.setDay("Wednesday");
                    avlWed.setMorningAvailable(true);
                    avlWed.setEveningAvailable(true);
                }else{
                    avlWed.setDay("Wednesday");
                    avlWed.setMorningAvailable(false);
                    avlWed.setEveningAvailable(false);
                }
                aList.add(avlWed);

                if(spinnerThu.getSelectedItem().toString().trim().equals("Morning")){
                    avlThu.setDay("Thursday");
                    avlThu.setMorningAvailable(true);
                    avlThu.setEveningAvailable(false);
                } else if (spinnerThu.getSelectedItem().toString().trim().equals("Afternoon")){
                    avlThu.setDay("Thursday");
                    avlThu.setMorningAvailable(false);
                    avlThu.setEveningAvailable(true);
                }else if (spinnerThu.getSelectedItem().toString().trim().equals("Both")){
                    avlThu.setDay("Thursday");
                    avlThu.setMorningAvailable(true);
                    avlThu.setEveningAvailable(true);
                }else{
                    avlThu.setDay("Thursday");
                    avlThu.setMorningAvailable(false);
                    avlThu.setEveningAvailable(false);
                }
                aList.add(avlThu);

                if(spinnerFri.getSelectedItem().toString().trim().equals("Morning")){
                    avlFri.setDay("Friday");
                    avlFri.setMorningAvailable(true);
                    avlFri.setEveningAvailable(false);
                } else if (spinnerFri.getSelectedItem().toString().trim().equals("Afternoon")){
                    avlFri.setDay("Friday");
                    avlFri.setMorningAvailable(false);
                    avlFri.setEveningAvailable(true);
                } else if (spinnerFri.getSelectedItem().toString().trim().equals("Both")){
                    avlFri.setDay("Friday");
                    avlFri.setMorningAvailable(true);
                    avlFri.setEveningAvailable(true);
                }else{
                    avlFri.setDay("Friday");
                    avlFri.setMorningAvailable(false);
                    avlFri.setEveningAvailable(false);
                }
                aList.add(avlFri);

                if(spinnerSat.getSelectedItem().toString().trim().equals("Full")){
                    avlSat.setDay("Saturday");
                    avlSat.setMorningAvailable(true);
                    avlSat.setEveningAvailable(true);
                }else{
                    avlSat.setDay("Saturday");
                    avlSat.setMorningAvailable(false);
                    avlSat.setEveningAvailable(false);
                }
                aList.add(avlSat);

                if(spinnerSun.getSelectedItem().toString().trim().equals("Full")){
                    avlSun.setDay("Sunday");
                    avlSun.setMorningAvailable(true);
                    avlSun.setEveningAvailable(true);
                }else{
                    avlSun.setDay("Sunday");
                    avlSun.setMorningAvailable(false);
                    avlSun.setEveningAvailable(false);
                }
                aList.add(avlSun);

                newEmployee.setEmployeeAvailabilityList(aList);

                if (employeeDB.checkDuplicate(newEmployee) == 1){
                    Toast.makeText(getApplicationContext(), "Full Name already exists please use a nickname", Toast.LENGTH_SHORT).show();
                    return;
                } else if (employeeDB.checkDuplicate(newEmployee) == 2){
                    Toast.makeText(getApplicationContext(), "Nickname already in use", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(getIntent().getExtras() != null){
                    employeeDB.updateEmployee(newEmployee);
                }else {

                    employeeDB.addEmployee(newEmployee);
                }


                // Show the message using a Snackbar
                setResult(RESULT_OK);

                // Finish the EmployeeActivity
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras() != null) {
                    newEmployee = (EmployeeModel) getIntent().getSerializableExtra("EDIT_THIS_EMPLOYEE");
                    newEmployee = employeeDB.getEmpDetails(newEmployee);
                    showDeleteConfirmationDialog(newEmployee);

                }

            }
        });
    }

    private void showDeleteConfirmationDialog(final EmployeeModel employeeToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked "Yes," so call db.delete with the employeeToDelete object
                        employeeDB.deleteEmployee(employeeToDelete);

                        setResult(RESULT_OK);

                        // Finish the EmployeeActivity
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked "No," do nothing or handle accordingly
                    }
                });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
