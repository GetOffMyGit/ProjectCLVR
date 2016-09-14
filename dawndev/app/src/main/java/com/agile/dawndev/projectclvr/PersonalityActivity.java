package com.agile.dawndev.projectclvr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile;

import org.w3c.dom.Text;

public class PersonalityActivity extends AppCompatActivity implements PersonalityAsyncTask.Personality {

    private TextView mOutputTextView;
    private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality);

        mOutputTextView = (TextView)findViewById(R.id.output_text);

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PersonalityAsyncTask task = new PersonalityAsyncTask(PersonalityActivity.this);

                    //Execute async task.
                    task.execute(text);

                } catch (Exception e) {
                    Log.e("Excpetion", "excpeiton", e);
                }
            }
        });

        //CONSTRAINT 100 words
        text =
                "You know, four years ago, I said that I'm not a perfect man and I wouldn't be a perfect president.\n" +
                "And that's probably a promise that Governor Romney thinks I've kept. But I also promised that\n" +
                "I'd fight every single day on behalf of the American people, the middle class, and all those who\n" +
                "were striving to get into the middle class. I've kept that promise and if you'll vote for me, then I\n" +
                "promise I'll fight just as hard in a second term. \n"
        + "You know, four years ago we went through the worst financial crisis since the Great Depression.\n" +
                        "Millions of jobs were lost, the auto industry was on the brink of collapse. The financial system\n" +
                        "had frozen up.";

        mOutputTextView.setText(text);
    }

    @Override
    public void getText(String string) {
        Log.d("Text", string);
        mOutputTextView.setText(string);
    }

}
