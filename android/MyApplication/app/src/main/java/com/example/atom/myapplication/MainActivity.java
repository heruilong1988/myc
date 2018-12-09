package com.example.atom.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp,MESSAGE";

    String mess = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mess = savedInstanceState.getString(EXTRA_MESSAGE);
            EditText editText = (EditText) findViewById(R.id.editText);
            System.out.println(mess);
            editText.setText(mess);
        } else {
            // Probably initialize members with default values for a new instance
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void sendMesage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        mess = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, mess);
        startActivity(intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_MESSAGE, mess);
        super.onSaveInstanceState(outState);
    }
}
