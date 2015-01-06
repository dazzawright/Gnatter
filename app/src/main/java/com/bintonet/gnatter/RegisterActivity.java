package com.bintonet.gnatter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends ActionBarActivity {

    protected EditText mUsername;
    protected EditText mEmailAddress;
    protected EditText mPassword;
    protected Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = (EditText) findViewById(R.id.editTextRegisterActivityUserName);
        mEmailAddress = (EditText) findViewById(R.id.editTextRegisterActivityEmailAddress);
        mPassword = (EditText) findViewById(R.id.editTextRegisterActivityPassword);
        mSubmit = (Button) findViewById(R.id.buttonRegisterActivitySubmit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(v.getContext(), String.valueOf(mUsername.getText()),Toast.LENGTH_SHORT).show();
                String u = mUsername.getText().toString();
                String p = mPassword.getText().toString();
                String e = mEmailAddress.getText().toString();
                if (u.length() == 0 || p.length() == 0 || e.length() == 0)
                {
                    Utils.showDialog(v.getContext(), R.string.err_fields_empty);
                    return;
                }
                final ProgressDialog dia = ProgressDialog.show(v.getContext(), null,
                        getString(R.string.alert_wait));

                final ParseUser pu = new ParseUser();
                pu.setEmail(e);
                pu.setPassword(p);
                pu.setUsername(u);
                pu.signUpInBackground(new SignUpCallback() {

                    @Override
                    public void done(ParseException e)
                    {
                        dia.dismiss();
                        if (e == null)
                        {
                            UserList.user = pu;
                            startActivity(new Intent(RegisterActivity.this, UserList.class));
                            setResult(RESULT_OK);
                            finish();
                        }
                        else
                        {
                            Utils.showDialog(
                                    RegisterActivity.this,
                                    getString(R.string.err_singup) + " "
                                            + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

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
