package collier.mckennon.com.wguhoot.Activity.term;

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

import collier.mckennon.com.wguhoot.Activity.course.AddCourseActivity;
import collier.mckennon.com.wguhoot.Activity.course.CoursesActivity;
import collier.mckennon.com.wguhoot.Adapter.CoursesAdapter;
import collier.mckennon.com.wguhoot.Adapter.TermAdapter;
import collier.mckennon.com.wguhoot.Model.Course;
import collier.mckennon.com.wguhoot.Model.Term;
import collier.mckennon.com.wguhoot.R;

public class TermsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TermAdapter termAdapter;
    List<Term> terms = new ArrayList<>();

    long initialCount;
    int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        SugarContext.init(this.getApplicationContext());
        recyclerView = findViewById(R.id.termRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(termAdapter);
        initialCount = Term.count(Term.class);

        if (savedInstanceState != null) {
            modifyPos = savedInstanceState.getInt("modify");
        }

        if (initialCount >= 0) {
            terms = Term.listAll(Term.class);
            termAdapter = new TermAdapter(TermsActivity.this, terms);
            recyclerView.setAdapter(termAdapter);
        }

        // Handling swipe to delete terms
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Term term = terms.get(viewHolder.getAdapterPosition());
                terms.remove(viewHolder.getAdapterPosition());
                termAdapter.notifyItemRemoved(position);

                term.delete();
                initialCount -= 1;

                Snackbar.make(recyclerView, "Term deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                term.save();
                                terms.add(position, term);
                                termAdapter.notifyItemInserted(position);
                                initialCount += 1;
                            }
                        })
                        .show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        if (initialCount >= 0) {
            termAdapter.SetOnItemClickListener(new TermAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent i = new Intent(TermsActivity.this, AddTermActivity.class);
                    i.putExtra("isEditing", true);
                    i.putExtra("term_title", terms.get(position).getTitle());
                    i.putExtra("start_date", terms.get(position).getStartDate());
                    i.putExtra("end_date", terms.get(position).getEndDate());
                    modifyPos = position;
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_terms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.addTerm:
                Intent intent = new Intent(this, AddTermActivity.class);
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

        final long newCount = Term.count(Term.class);

        if (newCount > initialCount) {
            // A note is added
            Log.d("TermsActivity", "Adding new course");
            // Just load the last added note (new)
            Term course = Term.last(Term.class);
            terms.add(course);
            termAdapter.notifyItemInserted((int) newCount);
            initialCount = newCount;
        }
        if (modifyPos != -1) {
            terms.set(modifyPos, Term.listAll(Term.class).get(modifyPos));
            termAdapter.notifyItemChanged(modifyPos);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long date) {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(date));
    }
}