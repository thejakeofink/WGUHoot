package collier.mckennon.com.wguhoot.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import collier.mckennon.com.wguhoot.Model.Assessment;
import collier.mckennon.com.wguhoot.R;

public class AddAssessmentActivity extends AppCompatActivity implements View.OnClickListener {
    EditText titleET, startDateET, endDateET;
    DatePickerDialog startDateDialog, endDateDialog;
    String title, startDate, endDate;
    boolean editingAssessment;
    long time;
    Button save;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        titleET = findViewById(R.id.assessmentTitleEditText);
        startDateET = findViewById(R.id.startDateET);
        endDateET = findViewById(R.id.endDateET);
        save = findViewById(R.id.btnSaveAssessment);
        sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        addDatePickerDialog();

        //handle intent
        editingAssessment = getIntent().getBooleanExtra("isEditing", false);
        if (editingAssessment) {
            title = getIntent().getStringExtra("assessment_title");
            startDate = getIntent().getStringExtra("start_date");
            endDate = getIntent().getStringExtra("end_date");
            time = getIntent().getLongExtra("note_time", 0);
            titleET.setText(title);
            startDateET.setText(startDate);
            endDateET.setText(endDate);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add assessment to db
                String newTitle = titleET.getText().toString();
                String newStartDate = startDateET.getText().toString();
                String newEndDate = endDateET.getText().toString();
                long newTime = System.currentTimeMillis();
                if (!editingAssessment) {
                    Assessment assessment = new Assessment(newTitle, newStartDate, newEndDate, newTime);
                    assessment.save();
                } else {
                    List<Assessment> assessments = Assessment.find(Assessment.class, "title = ?", title);
                    if (assessments.size() > 0) {
                        Assessment assessment = assessments.get(0);
                        Log.d("Got assessment", "note: " + assessment.getTitle());
                        assessment.setTitle(newTitle);
                        assessment.setStartDate(newStartDate);
                        assessment.setStartDate(newEndDate);
                        assessment.save();
                    }
                }
                finish();
            }
        });
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
