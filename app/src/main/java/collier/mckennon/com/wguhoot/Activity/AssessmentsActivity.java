package collier.mckennon.com.wguhoot.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.orm.SugarContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import collier.mckennon.com.wguhoot.Adapter.AssessmentAdapter;
import collier.mckennon.com.wguhoot.Model.Assessment;
import collier.mckennon.com.wguhoot.R;

public class AssessmentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AssessmentAdapter adapter;
    List<Assessment> assessments = new ArrayList<>();

    long initialCount;
    int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        SugarContext.init(this.getApplicationContext());
        recyclerView = findViewById(R.id.assessmentsRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        initialCount = Assessment.count(Assessment.class);

        if (savedInstanceState != null) {
            modifyPos = savedInstanceState.getInt("modify");
        }

        if (initialCount >= 0) {
            assessments = Assessment.listAll(Assessment.class);
            adapter = new AssessmentAdapter(AssessmentsActivity.this, assessments);
            recyclerView.setAdapter(adapter);
        }

        // Handling swipe to delete assessments
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Assessment assessment = assessments.get(viewHolder.getAdapterPosition());
                assessments.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(position);

                assessment.delete();
                initialCount -= 1;

                Snackbar.make(recyclerView, "Assessment deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                assessment.save();
                                assessments.add(position, assessment);
                                adapter.notifyItemInserted(position);
                                initialCount += 1;
                            }
                        })
                        .show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        adapter.SetOnItemClickListener(new AssessmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(AssessmentsActivity.this, AddAssessmentActivity.class);
                i.putExtra("isEditing", true);
                i.putExtra("assessment_title", assessments.get(position).getTitle());
                i.putExtra("start_date", assessments.get(position).getStartDate());
                i.putExtra("end_date", assessments.get(position).getEndDate());
                modifyPos = position;
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_assessments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.addAssessment:
                Intent intent = new Intent(this, AddAssessmentActivity.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("modify", modifyPos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        modifyPos = savedInstanceState.getInt("modify");
    }

    @Override
    protected void onResume() {
        super.onResume();

        final long newCount = Assessment.count(Assessment.class);

        if (newCount > initialCount) {
            // A note is added
            Log.d("AssessmentsActivity", "Adding new assessment");
            // Just load the last added note (new)
            Assessment assessment = Assessment.last(Assessment.class);
            assessments.add(assessment);
            adapter.notifyItemInserted((int) newCount);
            initialCount = newCount;
        }
        if (modifyPos != -1) {
            assessments.set(modifyPos, Assessment.listAll(Assessment.class).get(modifyPos));
            adapter.notifyItemChanged(modifyPos);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long date) {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(date));
    }
}
