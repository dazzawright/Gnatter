package com.bintonet.gnatter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLogin;
    protected Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            UserList.user = currentUser;
            startActivity(new Intent(LoginActivity.this, UserList.class));
            finish();
        } else {
            // show the signup or login screen

            mUsername = (EditText) findViewById(R.id.editTextLoginActivityUsername);
            mPassword = (EditText) findViewById(R.id.editTextLoginActivityPassword);
            mLogin = (Button) findViewById(R.id.buttonLoginActivityLogin);
            mRegister = (Button) findViewById(R.id.buttonLoginActivityRegister);

            mRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });

            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String p = mPassword.getText().toString();
                    String u = mUsername.getText().toString();
                    Log.d("LoginActivity", "Username: " + mUsername);
                    Log.d("LoginActivity", "Password: " + mPassword);
                    if (p.length() == 0 || u.length() == 0) {
                        Utils.showDialog(v.getContext(), R.string.err_fields_empty);
                        return;
                    }
                    final ProgressDialog dia = ProgressDialog.show(v.getContext(), null,
                            getString(R.string.alert_wait));

                    ParseUser.logInInBackground(u.toString(), p.toString(), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            dia.dismiss();
                            if (user != null) {
                                // Hooray! The user is logged in.

                                Log.d("LoginActivity", "Hooray!! We logged in");

                                UserList.user = user;
                                startActivity(new Intent(LoginActivity.this, UserList.class));
                                finish();
                            } else {
                                // Signup failed. Look at the ParseException to see what happened.
                                Log.d("LoginActivity", "Uh Oh, something went wrong");
                                Utils.showDialog(
                                        LoginActivity.this,
                                        getString(R.string.err_login) + " "
                                                + e.getMessage());
                                e.printStackTrace();

                            }
                        }
                    });


                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
