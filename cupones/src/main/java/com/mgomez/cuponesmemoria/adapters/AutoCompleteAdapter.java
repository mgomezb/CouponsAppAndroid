package com.mgomez.cuponesmemoria.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


import java.util.ArrayList;
import java.util.List;

import com.mgomez.cuponesmemoria.model.Comuna;

/**
 * Created by mgomezacid on 27-05-14.
 */
public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> suggestions;
    private List<Comuna> listaComunas;

    public AutoCompleteAdapter(final Context context, List<Comuna> comunas) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        listaComunas = comunas;
        suggestions = new ArrayList<String>();
    }


    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.length()>2) {
                    suggestions.clear();
                    for (int i=0;i<listaComunas.size();i++) {
                        if(listaComunas.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                            suggestions.add(listaComunas.get(i).getName());
                    }

                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }


}
