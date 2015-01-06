package com.bintonet.gnatter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by darren on 20/12/14.
 */
public class UserListAdapter extends ArrayAdapter<ParseObject>{

    protected Context mContext;
    protected List<ParseObject> mFriendsList;


    public UserListAdapter(Context context, List<ParseObject> friendsList) {
        super(context, R.layout.user_list_layout, friendsList);

        mContext = context;
        mFriendsList = friendsList;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.user_list_layout, null);
            holder = new ViewHolder();
            holder.friendsName = (TextView) convertView.findViewById(R.id.textFriendsName);
            holder.onlineStatus = (ImageView) convertView.findViewById(R.id.imgOnlineStatus);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject friendObject = mFriendsList.get(position);

        //Online Status Image
        Boolean isFriendOnline = (Boolean) friendObject.getBoolean("online");
        if(isFriendOnline){
            //friend is online, lets set image to online image

        }else{
            //friend is offline, lets set image to be offline

        }
//        if(friendObject.getBoolean("online") ){
//            holder.onlineStatus
//        }

        //Friends Name
        String friendsUserName = (String) friendObject.get("username");

        holder.friendsName.setText(friendsUserName);

        return convertView;


    }

    public static  class ViewHolder {
        TextView friendsName;
        ImageView onlineStatus;
    }
}
