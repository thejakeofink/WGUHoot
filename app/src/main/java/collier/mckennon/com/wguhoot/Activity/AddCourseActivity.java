package collier.mckennon.com.wguhoot.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import collier.mckennon.com.wguhoot.R;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText startDateET;
    private EditText endDateET;
    private DatePickerDialog startDateDialog;
    private DatePickerDialog endDateDialog;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        startDateET = findViewById(R.id.startDateET);
        endDateET = findViewById(R.id.endDateET);
        sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        addDatePickerDialog();
    }

    @Override
    public void onClick(View v) {
        if (v == startDateET) {
            startDateDialog.show();
        }
        if (v == endDateET) {
            endDateDialog.show();
        }
    }

    private void addDatePickerDialog() {
        startDateET.setOnClickListener(this);
        endDateET.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        startDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                startDateET.setText(sdf.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        endDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                endDateET.setText(sdf.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        startDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startDateDialog.show();
                }
            }
        });
    }
}
