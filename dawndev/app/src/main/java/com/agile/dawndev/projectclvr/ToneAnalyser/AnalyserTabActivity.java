package com.agile.dawndev.projectclvr.ToneAnalyser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.agile.dawndev.projectclvr.R;

import java.util.ArrayList;
import java.util.List;

/*
    Activity that contains the tablayout for the graphs
 */
public class AnalyserTabActivity extends AppCompatActivity implements ToneAnalyserBarFragment.OnTextSelectedListener{

    // String to pass into ToneAnalyserBarFragment
    private String barGraphString;
    private String recordedSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyser_tab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Radar"));
        tabLayout.addTab(tabLayout.newTab().setText("Bar"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        recordedSpeech = savedInstanceState.getString("message");
        Log.d("CHRISTINA", recordedSpeech);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RadarFragment(), "Radar");
        adapter.addFragment(new ToneAnalyserBarFragment(), "Bar");
        viewPager.setAdapter(adapter);
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    public String getBarGraphString() {
        return this.barGraphString;
    }

    public void onTextSelected(String text) {
        barGraphString = text;
    }
}
