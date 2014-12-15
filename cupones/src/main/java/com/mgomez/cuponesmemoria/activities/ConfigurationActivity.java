package com.mgomez.cuponesmemoria.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.utilities.Configuration;

import org.jraf.android.backport.switchwidget.Switch;


/**
 * Created by MGomez on 12-06-14.
 */
public class ConfigurationActivity extends Activity {


    Configuration configuration;
    ListView listView;
    public Switch generalInformation, allCategories;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.coupons_configuration);

        configuration = ((CouponApplication) getApplication()).getConfiguration();
        listView = (ListView) findViewById(R.id.listView);
        generalInformation = (Switch) findViewById(R.id.information_switch);
        allCategories = (Switch) findViewById(R.id.all_category_switch);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        generalInformation.setOnCheckedChangeListener(generalInformationListener);
        allCategories.setOnClickListener(allCategoriesListener);

        generalInformation.setChecked(configuration.getProperty(getBaseContext(), Constants.ALERTS_ACTIVATE, true));
        allCategories.setChecked(configuration.getProperty(getBaseContext(), Constants.COUPONS_ACTIVATE, true));

        getActionBar().setDisplayHomeAsUpEnabled(true);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        setAllCategoriesFirstUsage();
    }

    private void setAllCategoriesFirstUsage() {
        if(configuration.getProperty(getBaseContext(), Constants.COUPON_IS_FIRST, true)){
            configuration.setProperty(getBaseContext(), Constants.COUPON_IS_FIRST, false);
        }
    }

    private CompoundButton.OnCheckedChangeListener generalInformationListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            configuration.setProperty(getBaseContext(), Constants.ALERTS_ACTIVATE, checked);
        }
    };

    private View.OnClickListener allCategoriesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            configuration.setProperty(getBaseContext(), Constants.COUPONS_ACTIVATE, allCategories.isChecked());
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        if(item.getItemId() == R.id.configuration){
            Intent i = new Intent(ConfigurationActivity.this, Tutorial.class);
            i.putExtra("config", true);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.coupons, menu);

        //menu.findItem(R.id.configuration).setIcon(R.drawable.info_normal);

        return super.onCreateOptionsMenu(menu);
    }
}
