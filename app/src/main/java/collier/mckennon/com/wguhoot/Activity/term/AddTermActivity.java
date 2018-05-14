package collier.mckennon.com.wguhoot.Activity.term;

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

import collier.mckennon.com.wguhoot.Model.Term;
import collier.mckennon.com.wguhoot.R;

public class AddTermActivity extends AppCompatActivity implements View.OnClickListener {
    EditText titleET, startDateET, endDateET;
    DatePickerDialog startDateDialog, endDateDialog;
    String title, startDate, endDate;
    boolean editingTerm;
    long time;
    Button save;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        titleET = findViewById(R.id.termTitleET);
        startDateET = findViewById(R.id.startDateET);
        endDateET = findViewById(R.id.endDateET);
        save = findViewById(R.id.btnSaveTerm);
        sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        addDatePickerDialog();

        //handle intent
        editingTerm = getIntent().getBooleanExtra("isEditing", false);
        if (editingTerm) {
            title = getIntent().getStringExtra("term_title");
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
                //add term to db
                String newTitle = titleET.getText().toString();
                String newStartDate = startDateET.getText().toString();
                String newEndDate = endDateET.getText().toString();
                long newTime = System.currentTimeMillis();
                if (!editingTerm) {
                    Term term = new Term(newTitle, newStartDate, newEndDate, newTime);
                    term.save();
                } else {
                    List<Term> terms = Term.find(Term.class, "title = ?", title);
                    if (terms.size() > 0) {
                        Term term = terms.get(0);
                        Log.d("Got term", "note: " + term.getTitle());
                        term.setTitle(newTitle);
                        term.setStartDate(newStartDate);
                        term.setEndDate(newEndDate);
                        term.save();
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