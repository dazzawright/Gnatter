package com.bintonet.gnatter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UserList extends ListActivity{

    /** The Chat list. */
    private ArrayList<ParseUser> uList;
    protected List<ParseObject> mUsers;

    /** The user. */
    public static ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        updateUserStatus(true);
//        loadUserList();
        loadFriendsList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
        if (id == R.id.user_list_action_logout) {
            ParseUser.logOut();
            startActivity(new Intent(UserList.this, LoginActivity.class));
            finish();
            return true;
        }
        if (id == R.id.user_list_action_add_friend) {

            startActivity(new Intent(UserList.this, AddFriendActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUserStatus(boolean online)
    {
        user.put("online", online);
        user.saveEventually();
    }

    private void loadFriendsList() {

        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));

        final ParseUser currentUser = ParseUser.getCurrentUser();

        ParseRelation relation = currentUser.getRelation("Friends");
        relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                dia.dismiss();
                if (e != null) {
                    // There was an error
                    Utils.showDialog(
                            UserList.this,
                            getString(R.string.err_users) + " "
                                    + e.getMessage());
                    e.printStackTrace();
                } else {
                    Log.d("Friends List", "Friends found" + results.size());
                    mUsers = results;

                    UserListAdapter adapter = new UserListAdapter(getListView().getContext(), mUsers);
                    setListAdapter(adapter);

                }
            }
        });


    }


    private void loadUserList()
    {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));
        ParseUser.getQuery().whereNotEqualTo("username", user.getUsername())
                .findInBackground(new FindCallback<ParseUser>() {

                    @Override
                    public void done(List<ParseUser> li, ParseException e) {
                        dia.dismiss();
                        if (li != null) {
                            if (li.size() == 0)
                                Toast.makeText(UserList.this,
                                        R.string.msg_no_user_found,
                                        Toast.LENGTH_SHORT).show();

                            uList = new ArrayList<ParseUser>(li);
                            ListView list = (ListView) findViewById(R.id.list);
                            list.setAdapter(new UserAdapter());
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int pos, long arg3) {
                                    startActivity(new Intent(UserList.this,
                                            ChatActivity.class).putExtra("EXTRA_DATA", uList.get(pos)
                                            .getUsername()));
                                }
                            });
                        } else {
                            Utils.showDialog(
                                    UserList.this,
                                    getString(R.string.err_users) + " "
                                            + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
    }

                        /**
                         * The Class UserAdapter is the adapter class for User ListView. This
                         * adapter shows the user name and it's only online status for each item.
                         */
        private class UserAdapter extends BaseAdapter
        {

            /* (non-Javadoc)
             * @see android.widget.Adapter#getCount()
             */
            @Override
            public int getCount()
            {
                return uList.size();
            }

            /* (non-Javadoc)
             * @see android.widget.Adapter#getItem(int)
             */
            @Override
            public ParseUser getItem(int arg0)
            {
                return uList.get(arg0);
            }

            /* (non-Javadoc)
             * @see android.widget.Adapter#getItemId(int)
             */
            @Override
            public long getItemId(int arg0)
            {
                return arg0;
            }

            /* (non-Javadoc)
             * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
             */
            @Override
            public View getView(int pos, View v, ViewGroup arg2)
            {
                if (v == null)
                    v = getLayoutInflater().inflate(R.layout.chat_item, null);

                ParseUser c = getItem(pos);
                TextView lbl = (TextView) v;
                lbl.setText(c.getUsername());
                lbl.setCompoundDrawablesWithIntrinsicBounds(
                        c.getBoolean("online") ? R.drawable.ic_online
                                : R.drawable.ic_offline, 0, R.drawable.arrow, 0);

                return v;
            }

        }
}
