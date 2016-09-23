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

    private String textInput = "IBM announced new and expanded solutions and features including cognitive technologies that enable companies to deliver personalized customer experiences.\n" +
            "\n" +
            "According to a new report from Nucleus Research, IBM delivers $15.82 return on investment (ROI) for every dollar spent on its marketing, sales, merchandising and analytics offerings1 which today are being used by leading brands such as ING Direct, The Home Shopping Network (HSN) and Standard Life to meet the personalized, real-time needs of their customers. The importance of these experiences was highlighted in a recent IBM Study, “Redefining Markets,” where 66 percent of CxOs stated they are interested in creating more digital, individualized experiences. \n" +
            "\n" +
            "IBM is infusing cognitive technologies into the tools that practitioners work with today to help companies deliver these complete end-to-end customer experiences. These new offerings will leverage cognitive’s ability to understand, reason and learn over time and in the end provide teams with the expert in-the-moment advice, insights and recommendations to help them make better, more informed decisions at each step in the brand journey.\n" +
            "\n" +
            "\n" +
            "By continuously investing in building portfolio that includes new cognitive solutions, IBM is helping completely transform the way companies serve each customer. With the help of IBM cognitive tools, businesses will be able to quickly understand, reason and learn from every customer interaction - and use that knowledge to find new opportunities from insights.\n";

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
