package com.example.chaemingyun.deep_nap;

import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2017-01-16.
 */

public class ListViewItem {
    private Drawable iconDrawable;
    private String busTitle ;
    private String busNum ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        busTitle = title ;
    }
    public void setDesc(String desc) {
        busNum=desc;
    }
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.busTitle ;
    }
    public String getDesc() {
        return this.busNum ;
    }
}
