package com.example.vasileios.simplefiledialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vasileios on 22-Mar-15.
 */
public class ListViewAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList file = new ArrayList();

    private ArrayList<String> name = new ArrayList<String>();

    ListViewAdapter adapter = this;

    public ListViewAdapter(Context context, int layoutResourceId,
                           ArrayList file, ArrayList<String> name) {
        super(context, layoutResourceId, file);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.file = file;
        this.name = name;
    }

    ViewHolder holder = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();

            holder.fileName = (TextView) row.findViewById(R.id.text);
            holder.fileImage = (ImageView) row.findViewById(R.id.image);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ListItems item = (ListItems) file.get(position);
        holder.fileName.setText(item.getTitle());
        holder.fileImage.setImageBitmap(item.getImage());

        return row;
    }

    static class ViewHolder {
        TextView fileName;
        ImageView fileImage;
    }

}
