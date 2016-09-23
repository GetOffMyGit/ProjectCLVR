package com.agile.dawndev.projectclvr.ToneAnalyser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.agile.dawndev.projectclvr.R;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;

import java.util.ArrayList;
import java.util.List;

/*
    Activity that contains the tablayout for the graphs
 */
public class ToneAnalyserTabActivity extends AppCompatActivity implements AsyncResponse{

    private String resultJson;

    ToneAnalyserBarFragment toneAnalyserBarFragment;
    ToneAnalyserRadarFragment toneAnalyserRadarFragment;

    private String textInput = "Hi Team,\n" +
            "\n" +
            "The times are difficult! Our sales have been disappointing for the past three quarters for our data analytics product suite. We have a competitive data analytics product suite in the industry. But we are not doing a good job at selling it.\n" +
            "\n" +
            "We need to acknowledge and fix our sales challenges. We cannot blame the economy for our lack of execution! We are missing critical sales opportunities. Our clients are hungry for analytical tools to improve their business outcomes. In fact, it is in times such as this, our clients want to get the insights they need to turn their businesses around. We need to buckle up and execute.\n" +
            "\n" +
            "In summary, we have a competitive product, and a hungry market. We have to do our job to close the deals.\n" +
            "\n" +
            "Jennifer Baker\n" +
            "\n" +
            "Sales Leader, North-East region\n" +
            "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToneAnalyzer toneAnalyzerService = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        toneAnalyzerService.setUsernameAndPassword("345d437c-b0d0-4f07-8b0e-3a5bb21a4931", "qsWAYzFipvWy");

        ToneAnalyzerAsync toneAnalyzerAsyncTask = new ToneAnalyzerAsync(this);
        toneAnalyzerAsyncTask.delegate = ToneAnalyserTabActivity.this;
        toneAnalyzerAsyncTask.execute(toneAnalyzerService, textInput);

        setContentView(R.layout.activity_tone_tab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Radar"));
        tabLayout.addTab(tabLayout.newTab().setText("Bar"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public String getJsonResult() {
        return this.resultJson;
    }

    @Override
    public void processFinish(String resultJson){
        this.resultJson = resultJson;
        toneAnalyserBarFragment.createGraphs(ToneAnalyserTabActivity.this);
        toneAnalyserRadarFragment.createGraphs(ToneAnalyserTabActivity.this);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        toneAnalyserRadarFragment = new ToneAnalyserRadarFragment();
        adapter.addFragment(toneAnalyserRadarFragment, "Radar");

        toneAnalyserBarFragment = new ToneAnalyserBarFragment();
        adapter.addFragment(toneAnalyserBarFragment, "Bar");

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
}
