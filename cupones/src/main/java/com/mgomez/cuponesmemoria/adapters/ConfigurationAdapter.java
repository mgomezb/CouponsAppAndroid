package com.mgomez.cuponesmemoria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.activities.ConfigurationActivity;
import com.mgomez.cuponesmemoria.model.Category;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.utilities.Configuration;

import org.jraf.android.backport.switchwidget.Switch;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MGomez on 12-06-14.
 */
public class ConfigurationAdapter extends ArrayAdapter<Category>{

    final LayoutInflater layoutInflater;


    CouponDao couponDao;

    Configuration configuration;

    private List<Category> categories;

    public ConfigurationAdapter(Context context, List<Category> categories) {
        super(context, R.layout.item_configuration, categories);
        layoutInflater = LayoutInflater.from(context);
        this.categories = categories;
        couponDao =  ((CouponApplication) context.getApplicationContext()).getCouponDao();
        configuration = ((CouponApplication) context.getApplicationContext()).getConfiguration();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CategoryHolder categoryHolder;
        final Category category = getItem(position);

        if(v == null) {
            v = layoutInflater.inflate(R.layout.item_configuration, parent, false);
        }

        categoryHolder = (CategoryHolder) v.getTag();

        if(categoryHolder == null){
            categoryHolder = new CategoryHolder(v);
            v.setTag(categoryHolder);
        }

        categoryHolder.category.setText(category.getName());
        categoryHolder.switchCategory.setChecked(couponDao.isFilterConfiguration(category.getId()));


        categoryHolder.switchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Switch) view).isChecked()) {
                    couponDao.insertConfiguration(category.getId());
                    setAllCategories();
                }
                else {
                    couponDao.deleteConfiguration(category.getId());
                    ((ConfigurationActivity) getContext()).allCategories.setChecked(false);
                    configuration.setProperty(getContext(), Constants.COUPONS_ACTIVATE, false);
                }
            }
        });

        return v;
    }

    public void setAllCategories(){
        if(couponDao.getTotalConfigurations()==getCount()) {
            ((ConfigurationActivity) getContext()).allCategories.setChecked(true);
            configuration.setProperty(getContext(), Constants.COUPONS_ACTIVATE, true);
        }
    }

    public List<Category> getCategories(){
        return categories;
    }

    static class CategoryHolder{
        TextView category;
        Switch switchCategory;

        public CategoryHolder(View v){
            category = (TextView) v.findViewById(R.id.category);
            switchCategory = (Switch) v.findViewById(R.id.switch1);

        }
    }
}
