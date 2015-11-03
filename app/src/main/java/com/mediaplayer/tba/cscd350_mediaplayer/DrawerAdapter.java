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
 * Created by Bruce on 10/12/2015.
 *
 * Adapter for the Drawer in MainActivity
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    int resourceId;
    DrawerItem[] drawerItems;

    public DrawerAdapter(Context context, int resourceId, DrawerItem[] drawerItems) {
        super(context, resourceId, drawerItems);

        this.context = context;
        this.resourceId = resourceId;
        this.drawerItems = drawerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {

            // inflate the view
            Activity activity = (Activity) context;
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_item, null);
        }

        // set text view
        TextView textView = (TextView) convertView.findViewById(R.id.drawer_item_text_view);
        textView.setText(drawerItems[position].getText());

        // set image resource
        ImageView imageView = (ImageView) convertView.findViewById(R.id.drawer_item_image_view);
        imageView.setImageResource(drawerItems[position].getIconId());

        return convertView;
    }
}
