package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Created by Bruce on 10/12/2015.
 *
 * Drawer items for navigation drawer resources
 */
public class DrawerItem {

    private int iconId;
    private String text;

    public DrawerItem(int iconId, String text) {

        this.iconId = iconId;
        this.text = text;
    }

    public int getIconId() {
        return iconId;
    }

    public String getText() {
        return text;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setText(String text) {
        this.text = text;
    }
}
