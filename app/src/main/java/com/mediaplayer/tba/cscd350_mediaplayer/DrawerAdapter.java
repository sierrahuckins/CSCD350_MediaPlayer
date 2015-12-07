package com.mediaplayer.tba.cscd350_mediaplayer;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * DrawerAdapter.java
 * Author: Bruce Emehiser
 * Date: 20151012
 * Description: Adapter for the Navigation Drawer in MainActivity.java
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    private Context mContext;
    private DrawerItem[] mDrawerItems;

    public DrawerAdapter(Context context, int resourceId, DrawerItem[] drawerItems) {
        super(context, resourceId, drawerItems);

        mContext = context;
        mDrawerItems = drawerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {

            // inflate the view
            Activity activity = (Activity) mContext;
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_item, null);
        }

        // set text view
        TextView textView = (TextView) convertView.findViewById(R.id.drawer_item_text_view);
        textView.setText(mDrawerItems[position].getText());

        // set image resource
        ImageView imageView = (ImageView) convertView.findViewById(R.id.drawer_item_image_view);
        imageView.setImageResource(mDrawerItems[position].getIconId());

        return convertView;
    }
}
