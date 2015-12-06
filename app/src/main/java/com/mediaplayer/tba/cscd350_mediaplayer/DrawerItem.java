package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * DrawerItem.java
 * Author: Bruce Emehiser
 * Date: 20151012
 * Description: Drawer items for navigation drawer resources (wrapper class)
 */
public class DrawerItem {

    private int mIconId;
    private String mText;

    public DrawerItem(int iconId, String text) {

        mIconId = iconId;
        mText = text;
    }

    public int getIconId() {
        return mIconId;
    }

    public String getText() {
        return mText;
    }

    public void setIconId(int iconId) {
        mIconId = iconId;
    }

    public void setText(String text) {
        mText = text;
    }

    public String toString() {
        return mText + " " + mIconId;
    }
}
