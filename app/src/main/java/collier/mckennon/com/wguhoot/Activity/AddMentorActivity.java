package collier.mckennon.com.wguhoot.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import collier.mckennon.com.wguhoot.Model.Mentor;
import collier.mckennon.com.wguhoot.R;

public class AddMentorActivity extends AppCompatActivity {

    EditText nameET, phoneET, emailET;
    boolean editingMentor;
    String name, phone, email;
    long time;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mentor);

        nameET = (EditText) findViewById(R.id.nameET);
        phoneET = (EditText) findViewById(R.id.phoneET);
        emailET = (EditText) findViewById(R.id.emailET);
        save = (Button) findViewById(R.id.btnSaveMentor);

        //handle intent
        editingMentor = getIntent().getBooleanExtra("isEditing", false);
        if (editingMentor) {
            name = getIntent().getStringExtra("mentor_name");
            phone = getIntent().getStringExtra("mentor_phone");
            email = getIntent().getStringExtra("mentor_email");
            time = getIntent().getLongExtra("note_time", 0);
            nameET.setText(name);
            phoneET.setText(phone);
            emailET.setText(email);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add mentor to db
                String newName = nameET.getText().toString();
                String newPhone = phoneET.getText().toString();
                String newEmail = emailET.getText().toString();
                long newTime = System.currentTimeMillis();
                if (!editingMentor) {
                    Mentor mentor = new Mentor(newName, newPhone, newEmail, newTime);
                    mentor.save();
                } else {
                    List<Mentor> mentors = Mentor.find(Mentor.class, "name = ?", name);
                    if (mentors.size() > 0) {
                        Mentor mentor = mentors.get(0);
                        Log.d("Got mentor", "note: " + mentor.getName() + mentor.getPhone());
                        mentor.getName() = newName;
                        mentor.getPhone() = newPhone;
                        mentor.getEmail() = newEmail;
                        mentor.save();
                    }
                }
                finish();
            }
        });
    }
}
