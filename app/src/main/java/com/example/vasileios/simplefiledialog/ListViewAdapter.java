package com.example.vasileios.simplefiledialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasileios on 22-Mar-15.
 */
public class ListViewAdapter extends ArrayAdapter implements Filterable {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ListItems> file = new ArrayList();

    private ArrayList<ListItems> filteredData  = new ArrayList();

    private ArrayList<String> name = new ArrayList<String>();

    ListViewAdapter adapter = this;

    private ItemFilter mFilter = new ItemFilter();

    public ListViewAdapter(Context context, int layoutResourceId,
                           ArrayList file, ArrayList<String> name) {
        super(context, layoutResourceId, file);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.file = file;
        this.filteredData = file;
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

        ListItems item =  file.get(position);
        holder.fileName.setText(item.getTitle());
        holder.fileImage.setImageBitmap(item.getImage());

        return row;
    }

    static class ViewHolder {
        TextView fileName;
        ImageView fileImage;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<ListItems> list = file;

            int count = list.size();

            final ArrayList values = new ArrayList(count);
            String filterableString;

            for (int i = 0; i < count; i++)
            {
                filterableString = list.get(i).getTitle();
                if (filterableString.toLowerCase().contains(filterString)) {
                    values.add(filterableString);
                }
            }

            results.values = values;
            results.count = values.size();

            return  results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }

}
