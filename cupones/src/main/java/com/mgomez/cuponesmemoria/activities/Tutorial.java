package com.mgomez.cuponesmemoria.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.fragments.TutorialFragment;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by mgomezacid on 11-04-14.
 */
public class Tutorial extends Activity {

    static class PageDesc {
        Fragment fragment;

        PageDesc(final Fragment fragment) {
            this.fragment = fragment;
        }
    }

    private final static ArrayList<PageDesc> fragments = new ArrayList<PageDesc>();

    static {
        //Page 0

        Bundle args0 = new Bundle();
        args0.putInt("icon", R.drawable.tuto_0);
        args0.putInt("title", R.string.title_tuto_0);
        args0.putInt("message", R.string.message_tuto_0);
        TutorialFragment tutorialPage0 = new TutorialFragment();
        tutorialPage0.setArguments(args0);

        //Page 1

        Bundle args1 = new Bundle();
        args1.putInt("icon", R.drawable.tuto_1);
        args1.putInt("title", R.string.title_tuto_1);
        args1.putInt("message", R.string.message_tuto_1);
        TutorialFragment tutorialPage1 = new TutorialFragment();
        tutorialPage1.setArguments(args1);

        //Page 2

        Bundle args2 = new Bundle();
        args2.putInt("icon", R.drawable.tuto_2);
        args2.putInt("title", R.string.title_tuto_2);
        args2.putInt("message", R.string.message_tuto_2);
        TutorialFragment tutorialPage2 = new TutorialFragment();
        tutorialPage2.setArguments(args2);

        //Page 3

        Bundle args3 = new Bundle();
        args3.putInt("icon", R.drawable.tuto_3);
        args3.putInt("title", R.string.title_tuto_3);
        args3.putInt("message", R.string.message_tuto_3);
        TutorialFragment tutorialPage3 = new TutorialFragment();
        tutorialPage3.setArguments(args3);

        fragments.add(new PageDesc(tutorialPage0));
        fragments.add(new PageDesc(tutorialPage1));
        fragments.add(new PageDesc(tutorialPage2));
        fragments.add(new PageDesc(tutorialPage3));
    }

    Button button;
    Configuration configuration;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Bundle b = getIntent().getExtras();

        configuration = ((CouponApplication) getApplication()).getConfiguration();

        if(b == null && configuration.getProperty(this, Constants.TUTORIAL_SEEN, false)){
            startActivity(new Intent(this, Login.class));
            finish();
        }
        else {
            configuration.setProperty(this, Constants.TUTORIAL_SEEN, true);
        }

        getActionBar().hide();
        setContentView(R.layout.tutorial_view);



        FragmentPagerAdapter adapter = new TutorialPagerAdapter(getFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.tutorial_pager);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(exit);

        if(b!= null && b.containsKey("config")){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            button.setVisibility(View.GONE);
        }

        pager.setAdapter(adapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.tutorial_indicator);
        indicator.setViewPager(pager);
        indicator.setRadius(10);

    }

    private View.OnClickListener exit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Tutorial.this, Login.class));
            finish();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class TutorialPagerAdapter extends FragmentPagerAdapter {

        public TutorialPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == getCount()-1)
                button.setText(getString(R.string.finish));

            return fragments.get(position).fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}


