package com.bintonet.gnatter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class AddFriendActivity extends ActionBarActivity {

    protected EditText mEmailAddress;
    protected Button mSubmit;
    protected TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mResult = (TextView) findViewById(R.id.textViewAddFriendActivityResult);
        mEmailAddress = (EditText) findViewById(R.id.editTextAddFriendActivityEmail);
        mSubmit = (Button) findViewById(R.id.btnAddFriendActivitySubmit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String findAFriend = mEmailAddress.getText().toString();
                final ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    {
                        final ParseObject friend = new ParseObject("Friends");
                        friend.put("username", findAFriend);

                        friend.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                // TODO Auto-generated method stub
                                ParseRelation relation = currentUser.getRelation("Friends");
                                relation.add(friend);
                                currentUser.saveInBackground();
                            }

                        });


                        // create an entry in the Follow table
                        Toast.makeText(getApplicationContext(), "Friend Has Been Added",
                                Toast.LENGTH_LONG).show();
                        mEmailAddress.setText("");
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
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
