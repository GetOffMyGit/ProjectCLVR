package com.agile.dawndev.projectclvr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zoe on 3/10/16.
 */
public class GraphGenActivity extends AppCompatActivity {
    ToneAnalyserRadarFragment toneAnalyserRadarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToneAnalyzer toneAnalyzerService = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        toneAnalyzerService.setUsernameAndPassword("345d437c-b0d0-4f07-8b0e-3a5bb21a4931", "qsWAYzFipvWy");

        setContentView(R.layout.activity_graph_gen);

//        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
//        rootLayout.setupWithViewPager(viewPager);
    }

    public String getJsonResult() {
        return this.output;
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        toneAnalyserRadarFragment = new ToneAnalyserRadarFragment();
        adapter.addFragment(toneAnalyserRadarFragment, "Radar");

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

    String output = "{\n" +
            "   \"tone_categories\": [\n" +
            "     {\n" +
            "       \"category_id\": \"emotion_tone\",\n" +
            "       \"category_name\": \"Emotion Tone\",\n" +
            "       \"tones\": [\n" +
            "         {\n" +
            "           \"tone_id\": \"anger\",\n" +
            "           \"tone_name\": \"Anger\",\n" +
            "           \"score\": 0.83414\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"disgust\",\n" +
            "           \"tone_name\": \"Disgust\",\n" +
            "           \"score\": 0.229384\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"fear\",\n" +
            "           \"tone_name\": \"Fear\",\n" +
            "           \"score\": 0.263215\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"joy\",\n" +
            "           \"tone_name\": \"Joy\",\n" +
            "           \"score\": 0.018623\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"sadness\",\n" +
            "           \"tone_name\": \"Sadness\",\n" +
            "           \"score\": 0.153338\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"category_id\": \"language_tone\",\n" +
            "       \"category_name\": \"Language Tone\",\n" +
            "       \"tones\": [\n" +
            "         {\n" +
            "           \"tone_id\": \"analytical\",\n" +
            "           \"tone_name\": \"Analytical\",\n" +
            "           \"score\": 0.665\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"confident\",\n" +
            "           \"tone_name\": \"Confident\",\n" +
            "           \"score\": 0.0\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"tentative\",\n" +
            "           \"tone_name\": \"Tentative\",\n" +
            "           \"score\": 0.0\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"category_id\": \"social_tone\",\n" +
            "       \"category_name\": \"Social Tone\",\n" +
            "       \"tones\": [\n" +
            "         {\n" +
            "           \"tone_id\": \"openness_big5\",\n" +
            "           \"tone_name\": \"Openness\",\n" +
            "           \"score\": 0.04\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"conscientiousness_big5\",\n" +
            "           \"tone_name\": \"Conscientiousness\",\n" +
            "           \"score\": 0.076\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"extraversion_big5\",\n" +
            "           \"tone_name\": \"Extraversion\",\n" +
            "           \"score\": 0.648\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"agreeableness_big5\",\n" +
            "           \"tone_name\": \"Agreeableness\",\n" +
            "           \"score\": 0.869\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"emotional_range_big5\",\n" +
            "           \"tone_name\": \"Emotional Range\",\n" +
            "           \"score\": 0.966\n" +
            "         }\n" +
            "       ]\n" +
            "     }\n" +
            "   ]\n" +
            " }\n";
}
