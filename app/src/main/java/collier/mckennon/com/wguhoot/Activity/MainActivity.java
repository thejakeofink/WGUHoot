package collier.mckennon.com.wguhoot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import collier.mckennon.com.wguhoot.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewTermsButtonClicked(View view) {
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    public void viewCoursesButtonClicked(View view) {
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }

    public void viewAssessmentsButtonClicked(View view) {
        Intent intent = new Intent(this, AssessmentsActivity.class);
        startActivity(intent);
    }

    public void viewMentorsButtonClicked(View view) {
        Intent intent = new Intent(this, MentorsActivity.class);
        startActivity(intent);
    }
}
