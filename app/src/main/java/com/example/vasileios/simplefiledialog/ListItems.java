package com.example.vasileios.simplefiledialog;

import android.graphics.Bitmap;

/**
 * Created by Vasileios on 22-Mar-15.
 */
public class ListItems implements Comparable<ListItems> {
    private Bitmap image;
    private String title;

    public ListItems(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(ListItems another) {
        return this.title.toLowerCase().charAt(0) > another.title.toLowerCase().charAt(0) ? 1 : (this.title.toLowerCase().charAt(0) < another.title.toLowerCase().charAt(0) ? -1 : 0);
    }
}
