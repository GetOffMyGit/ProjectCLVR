package com.agile.dawndev.projectclvr.PersonalityInsight;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.agile.dawndev.projectclvr.R;
import com.agile.dawndev.projectclvr.ToneAnalyser.AsyncResponse;
import com.agile.dawndev.projectclvr.ToneAnalyser.RadarFragment;
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;

import java.util.ArrayList;
import java.util.List;

public class PersonalityTabActivity extends AppCompatActivity implements AsyncResponse {

    // String to pass into ToneAnalyserBarFragment
    private String textInput;

    private String resultJson;

    PersonalityInsightsBarFragment personalityInsightsBarFragment;
    PersonalityInsightsRadarFragment personalityInsightsRadarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textInput = "You know, four years ago, I said that I'm not a perfect man and I wouldn't be a perfect president.\n" +
                "And that's probably a promise that Governor Romney thinks I've kept. But I also promised that\n" +
                "I'd fight every single day on behalf of the American people, the middle class, and all those who\n" +
                "were striving to get into the middle class. I've kept that promise and if you'll vote for me, then I\n" +
                "promise I'll fight just as hard in a second term. \n"
                + "You know, four years ago we went through the worst financial crisis since the Great Depression.\n" +
                "Millions of jobs were lost, the auto industry was on the brink of collapse. The financial system\n" +
                "had frozen up.";

        PersonalityInsights personalityInsightsService = new PersonalityInsights();
        personalityInsightsService.setUsernameAndPassword("345d437c-b0d0-4f07-8b0e-3a5bb21a4931", "qsWAYzFipvWy");

        PersonalityInsightsAsync personalityAsyncTask = new PersonalityInsightsAsync(this);
        personalityAsyncTask.delegate = PersonalityTabActivity.this;
        personalityAsyncTask.execute(new PersonalityInsights(), textInput);

        setContentView(R.layout.activity_personality_tab);

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
        personalityInsightsBarFragment.createGraphs(PersonalityTabActivity.this);
        personalityInsightsRadarFragment.createGraphs(PersonalityTabActivity.this);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        personalityInsightsRadarFragment = new PersonalityInsightsRadarFragment();
        adapter.addFragment(personalityInsightsRadarFragment, "Radar");

        personalityInsightsBarFragment = new PersonalityInsightsBarFragment();
        adapter.addFragment(personalityInsightsBarFragment, "Bar");

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
