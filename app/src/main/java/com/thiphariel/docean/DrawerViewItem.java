package com.thiphariel.docean;

/**
 * Created by Thiphariel on 26/09/2015.
 */
public class DrawerViewItem {
    private int resourceId;
    private String title;

    public DrawerViewItem(int resourceId, String title) {
        this.resourceId = resourceId;
        this.title = title;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getTitle() {
        return title;
    }
}
