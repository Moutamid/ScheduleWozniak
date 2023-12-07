package com.example.schedulewozniak;

import android.content.Intent;
import android.os.Bundle;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.schedulewozniak.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Intent myIntnet = null;
    private CompactCalendarView compactCalendarView;
    private TextView calendarDayText;
    SimpleDateFormat monthFORMAT = new SimpleDateFormat("MMMM", Locale.getDefault());
    EmployeeDB employeeDB = DatabaseSingleton.getInstance(this).getEmployeeDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        setSupportActionBar(binding.toolbar);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.calendar);
        calendarDayText = findViewById(R.id.calendarDayText);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        String d = monthFORMAT.format(new Date());
        calendarDayText.setText(d);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        List<EmployeeModel> list = employeeDB.getAll();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Calendar myCal = Calendar.getInstance();
                myCal.setTime(dateClicked);

                Intent intent = new Intent(MainActivity.this, DateActivity.class);

                intent.putExtra("selectedCalendar", myCal);

                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date date) {
                String d = monthFORMAT.format(date);
                calendarDayText.setText(d);
                update();
                Log.d("DateCh1", "Month was scrolled to: " + date);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        try {
            Date startDate = compactCalendarView.getFirstDayOfCurrentMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date endDate = calendar.getTime();
            calendar.setTime(startDate);

// Iterate through each day of the visible month
            while (!calendar.getTime().after(endDate)) {
                Date currentDate = calendar.getTime();

                // Call your isScheduled function for the current date
                boolean isScheduled = employeeDB.isScheduled(currentDate);

                if (!isScheduled) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        Event event = new Event(getResources().getColor(R.color.search_opaque), currentDate.getTime(), "Schedule Name");
                        compactCalendarView.addEvent(event);
                    }
                }

                // Print or handle the result as needed
                System.out.println("Date: " + currentDate + ", Scheduled: " + isScheduled);

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        if (id == R.id.action_schedule) {
            // Navigate to the FirstFragment
            navController.navigate(R.id.CalenderFragment);
            return true;
        } else if (id == R.id.action_employee) {
            // Navigate to the SecondFragment
            myIntnet = new Intent(getApplicationContext(), SecondActivity.class);

            myIntnet.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(myIntnet);

            return true;
            //noinspection SimplifiableIfStatement
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}