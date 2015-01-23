package chat.android.com.nedochat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import chat.android.com.nedochat.DBUtils.Utils;
import chat.android.com.nedochat.R;


public class StartActivity extends Activity {

    private EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Utils utils = Utils.get(getApplicationContext());

        txtName = (EditText) findViewById(R.id.name);

        //if user is logged in , then redirect to ChatActivity
        if (utils.getSessionUser() != null) {
            Intent intent = new Intent(StartActivity.this,
                    ChatActivity.class);
            intent.putExtra("name", utils.getSessionUser());
            startActivity(intent);
        }

    }

    public void onJoinButtClick(View view) {
        //Get text from EditText , and start ChatActivity
        if (txtName.getText().toString().trim().length() > 0) {

            String name = txtName.getText().toString().trim();

            Intent intent = new Intent(StartActivity.this,
                    ChatActivity.class);
            intent.putExtra("name", name);

            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your name", Toast.LENGTH_LONG).show();
        }
    }
}
