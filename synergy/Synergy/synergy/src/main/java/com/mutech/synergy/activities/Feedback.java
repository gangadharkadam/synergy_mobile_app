package com.mutech.synergy.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.mutech.synergy.R;

public class Feedback extends ActionBarActivity  {

    private Button submit,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        submit=(Button) findViewById(R.id.submit);
        cancel=(Button) findViewById(R.id.cancel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                submit.setEnabled(false);
                        new AlertDialog.Builder(Feedback.this)
                                .setCancelable(false)
                                .setTitle("Feedback")
                                .setMessage("Your feedback has been registered")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent actInt = new Intent(Feedback.this,
                                                HomeActivity.class);
                                        startActivity(actInt);
                                        finish();

                                    }
                                })
                                .show();
                    }

        });
    }

}
