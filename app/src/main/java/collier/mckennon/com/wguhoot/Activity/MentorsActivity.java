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

import collier.mckennon.com.wguhoot.Adapter.MentorAdapter;
import collier.mckennon.com.wguhoot.Model.Mentor;
import collier.mckennon.com.wguhoot.R;

public class MentorsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MentorAdapter adapter;
    List<Mentor> mentors = new ArrayList<>();

    long initialCount;
    int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors);
        SugarContext.init(this.getApplicationContext());
        recyclerView = findViewById(R.id.mentorRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        initialCount = Mentor.count(Mentor.class);

        if (savedInstanceState != null) {
            modifyPos = savedInstanceState.getInt("modify");
        }

        if (initialCount >= 0) {
            mentors = Mentor.listAll(Mentor.class);
            recyclerView.setAdapter(adapter);
        }

        // Handling swipe to delete mentors
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Mentor mentor = mentors.get(viewHolder.getAdapterPosition());
                mentors.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(position);

                mentor.delete();
                initialCount -= 1;

                Snackbar.make(recyclerView, "Mentor deleted", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mentor.save();
                                mentors.add(position, mentor);
                                adapter.notifyItemInserted(position);
                                initialCount += 1;
                            }
                        })
                        .show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter = new MentorAdapter(MentorsActivity.this, mentors);
        adapter.SetOnItemClickListener(new MentorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(MentorsActivity.this, AddMentorActivity.class);
                i.putExtra("isEditing", true);
                i.putExtra("mentor_name", mentors.get(position).getName());
                i.putExtra("mentor_phone", mentors.get(position).getPhone());
                i.putExtra("mentor_email", mentors.get(position).getEmail());
                modifyPos = position;
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mentors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.addMentor:
                Intent intent = new Intent(this, AddMentorActivity.class);
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

        final long newCount = Mentor.count(Mentor.class);

        if (newCount > initialCount) {
            // A note is added
            Log.d("MentorsActivity", "Adding new mentor");
            // Just load the last added note (new)
            Mentor mentor = Mentor.last(Mentor.class);
            mentors.add(mentor);
            adapter.notifyItemInserted((int) newCount);
            initialCount = newCount;
        }
        if (modifyPos != -1) {
            mentors.set(modifyPos, Mentor.listAll(Mentor.class).get(modifyPos));
            adapter.notifyItemChanged(modifyPos);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long date) {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(date));
    }

}
