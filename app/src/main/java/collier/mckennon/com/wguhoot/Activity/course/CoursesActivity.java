package collier.mckennon.com.wguhoot.Activity.course;

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

import collier.mckennon.com.wguhoot.Adapter.CoursesAdapter;
import collier.mckennon.com.wguhoot.Model.Course;
import collier.mckennon.com.wguhoot.R;

public class CoursesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CoursesAdapter adapterCourses;
    List<Course> courses = new ArrayList<>();

    long initialCount;
    int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        SugarContext.init(this.getApplicationContext());
        recyclerView = findViewById(R.id.coursesRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterCourses);
        initialCount = Course.count(Course.class);

        if (savedInstanceState != null) {
            modifyPos = savedInstanceState.getInt("modify");
        }

        if (initialCount >= 0) {
            courses = Course.listAll(Course.class);
            adapterCourses = new CoursesAdapter(CoursesActivity.this, courses);
            recyclerView.setAdapter(adapterCourses);
        }

        // Handling swipe to delete courses
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Course course = courses.get(viewHolder.getAdapterPosition());
                courses.remove(viewHolder.getAdapterPosition());
                adapterCourses.notifyItemRemoved(position);

                course.delete();
                initialCount -= 1;

                Snackbar.make(recyclerView, "Courses deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                course.save();
                                courses.add(position, course);
                                adapterCourses.notifyItemInserted(position);
                                initialCount += 1;
                            }
                        })
                        .show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        if (initialCount >= 0) {
            adapterCourses.SetOnItemClickListener(new CoursesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent i = new Intent(CoursesActivity.this, AddCourseActivity.class);
                    i.putExtra("isEditing", true);
                    i.putExtra("course_title", courses.get(position).getTitle());
                    i.putExtra("start_date", courses.get(position).getStartDate());
                    i.putExtra("end_date", courses.get(position).getEndDate());
                    modifyPos = position;
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_courses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.addCourse:
                Intent intent = new Intent(this, AddCourseActivity.class);
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

        final long newCount = Course.count(Course.class);

        if (newCount > initialCount) {
            // A note is added
            Log.d("CoursesActivity", "Adding new course");
            // Just load the last added note (new)
            Course course = Course.last(Course.class);
            courses.add(course);
            adapterCourses.notifyItemInserted((int) newCount);
            initialCount = newCount;
        }
        if (modifyPos != -1) {
            courses.set(modifyPos, Course.listAll(Course.class).get(modifyPos));
            adapterCourses.notifyItemChanged(modifyPos);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long date) {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(date));
    }
}
