package com.agile.dawndev.projectclvr.PersonalityInsight;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.agile.dawndev.projectclvr.AsyncResponse;
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;

/**
 * Created by Zoe on 21/09/16.
 */
public class PersonalityInsightsAsync extends AsyncTask<Object, Void, String> {
    private Context context;

    public AsyncResponse delegate = null;

    boolean useAPI = false;

    public PersonalityInsightsAsync(Context context){
        this.context = context;
    }

    //retrieves the input text and sends it to the API to be analyzed
    @Override
    protected String doInBackground(Object... input) {
        PersonalityInsights service = (PersonalityInsights) input[0];
        String text = (String) input[1];

        if(this.useAPI){
            return service.getProfile(text).execute().toString();
        } else{
            Log.d("PersonalityInsights: ", "Just using saved output");
            return this.output;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            delegate.processFinish(result);
        }
    }

    //Storing the tone analyser output so we don't have to waste API calls for testing
    // This is for the bar graph
    String output = "{\n" +
            "  \"id\": \"*UNKNOWN*\",\n" +
            "  \"source\": \"*UNKNOWN*\",\n" +
            "  \"word_count\": 7020,\n" +
            "  \"processed_lang\": \"en\",\n" +
            "  \"tree\": {\n" +
            "    \"id\": \"r\",\n" +
            "    \"name\": \"root\",\n" +
            "    \"children\": [\n" +
            "      {\n" +
            "        \"id\": \"personality\",\n" +
            "        \"name\": \"Big 5\",\n" +
            "        \"children\": [\n" +
            "          {\n" +
            "            \"id\": \"Neuroticism_parent\",\n" +
            "            \"name\": \"Emotional range\",\n" +
            "            \"category\": \"personality\",\n" +
            "            \"percentage\": 0.9529868730168459,\n" +
            "            \"children\": [\n" +
            "              {\n" +
            "                \"id\": \"Openness\",\n" +
            "                \"name\": \"Openness\",\n" +
            "                \"category\": \"personality\",\n" +
            "                \"percentage\": 0.943620703468731,\n" +
            "                \"sampling_error\": 0.050433228000000004,\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"id\": \"Adventurousness\",\n" +
            "                    \"name\": \"Adventurousness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.8728046722494263,\n" +
            "                    \"sampling_error\": 0.044439042399999996\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Artistic interests\",\n" +
            "                    \"name\": \"Artistic interests\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.4184832909070454,\n" +
            "                    \"sampling_error\": 0.0915974812\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Emotionality\",\n" +
            "                    \"name\": \"Emotionality\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.15836577957164555,\n" +
            "                    \"sampling_error\": 0.0408893652\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Imagination\",\n" +
            "                    \"name\": \"Imagination\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.04481189401582769,\n" +
            "                    \"sampling_error\": 0.055123702\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Intellect\",\n" +
            "                    \"name\": \"Intellect\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.9963934719775018,\n" +
            "                    \"sampling_error\": 0.0478221852\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Liberalism\",\n" +
            "                    \"name\": \"Authority-challenging\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.8835343648439694,\n" +
            "                    \"sampling_error\": 0.0737321412\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Conscientiousness\",\n" +
            "                \"name\": \"Conscientiousness\",\n" +
            "                \"category\": \"personality\",\n" +
            "                \"percentage\": 0.8717383918743098,\n" +
            "                \"sampling_error\": 0.0640670652,\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"id\": \"Achievement striving\",\n" +
            "                    \"name\": \"Achievement striving\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.8987448251947701,\n" +
            "                    \"sampling_error\": 0.0868682\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Cautiousness\",\n" +
            "                    \"name\": \"Cautiousness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.9712886050822256,\n" +
            "                    \"sampling_error\": 0.0799760964\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Dutifulness\",\n" +
            "                    \"name\": \"Dutifulness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.8133994248325802,\n" +
            "                    \"sampling_error\": 0.0534036904\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Orderliness\",\n" +
            "                    \"name\": \"Orderliness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.2877418132557438,\n" +
            "                    \"sampling_error\": 0.061099612000000005\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Self-discipline\",\n" +
            "                    \"name\": \"Self-discipline\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.8532835038541262,\n" +
            "                    \"sampling_error\": 0.041565227600000004\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Self-efficacy\",\n" +
            "                    \"name\": \"Self-efficacy\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.907224670713983,\n" +
            "                    \"sampling_error\": 0.0799151848\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Extraversion\",\n" +
            "                \"name\": \"Extraversion\",\n" +
            "                \"category\": \"personality\",\n" +
            "                \"percentage\": 0.6337763778533128,\n" +
            "                \"sampling_error\": 0.0480567116,\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"id\": \"Activity level\",\n" +
            "                    \"name\": \"Activity level\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.8385116473609908,\n" +
            "                    \"sampling_error\": 0.06717252\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Assertiveness\",\n" +
            "                    \"name\": \"Assertiveness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.9962249573928508,\n" +
            "                    \"sampling_error\": 0.0714607836\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Cheerfulness\",\n" +
            "                    \"name\": \"Cheerfulness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.27053701380300665,\n" +
            "                    \"sampling_error\": 0.08931556160000001\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Excitement-seeking\",\n" +
            "                    \"name\": \"Excitement-seeking\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.03837863748658232,\n" +
            "                    \"sampling_error\": 0.0736439404\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Friendliness\",\n" +
            "                    \"name\": \"Outgoing\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.6540476669386505,\n" +
            "                    \"sampling_error\": 0.06573601920000001\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Gregariousness\",\n" +
            "                    \"name\": \"Gregariousness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.3193122437289925,\n" +
            "                    \"sampling_error\": 0.0509565784\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Agreeableness\",\n" +
            "                \"name\": \"Agreeableness\",\n" +
            "                \"category\": \"personality\",\n" +
            "                \"percentage\": 0.2164721952851409,\n" +
            "                \"sampling_error\": 0.08592564800000001,\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"id\": \"Altruism\",\n" +
            "                    \"name\": \"Altruism\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.8838134070911626,\n" +
            "                    \"sampling_error\": 0.0614342244\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Cooperation\",\n" +
            "                    \"name\": \"Cooperation\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.7023301790139942,\n" +
            "                    \"sampling_error\": 0.07084548560000001\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Modesty\",\n" +
            "                    \"name\": \"Modesty\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.21784046710915178,\n" +
            "                    \"sampling_error\": 0.047325734\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Morality\",\n" +
            "                    \"name\": \"Uncompromising\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.9236372740673221,\n" +
            "                    \"sampling_error\": 0.054774162\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Sympathy\",\n" +
            "                    \"name\": \"Sympathy\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.9873886400757069,\n" +
            "                    \"sampling_error\": 0.0854910516\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Trust\",\n" +
            "                    \"name\": \"Trust\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.49817604319980757,\n" +
            "                    \"sampling_error\": 0.047586447600000005\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Neuroticism\",\n" +
            "                \"name\": \"Emotional range\",\n" +
            "                \"category\": \"personality\",\n" +
            "                \"percentage\": 0.9529868730168459,\n" +
            "                \"sampling_error\": 0.0781423444,\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"id\": \"Anger\",\n" +
            "                    \"name\": \"Fiery\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.009729046238299233,\n" +
            "                    \"sampling_error\": 0.0827282748\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Anxiety\",\n" +
            "                    \"name\": \"Prone to worry\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.010776264386158696,\n" +
            "                    \"sampling_error\": 0.046816477599999996\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Depression\",\n" +
            "                    \"name\": \"Melancholy\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.17154966258693838,\n" +
            "                    \"sampling_error\": 0.0490326032\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Immoderation\",\n" +
            "                    \"name\": \"Immoderation\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.12963720671866819,\n" +
            "                    \"sampling_error\": 0.0458356808\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Self-consciousness\",\n" +
            "                    \"name\": \"Self-consciousness\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.06943652192683436,\n" +
            "                    \"sampling_error\": 0.048011719599999995\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"id\": \"Vulnerability\",\n" +
            "                    \"name\": \"Susceptible to stress\",\n" +
            "                    \"category\": \"personality\",\n" +
            "                    \"percentage\": 0.01604419579799027,\n" +
            "                    \"sampling_error\": 0.07325365519999999\n" +
            "                  }\n" +
            "                ]\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"needs\",\n" +
            "        \"name\": \"Needs\",\n" +
            "        \"children\": [\n" +
            "          {\n" +
            "            \"id\": \"Self-expression_parent\",\n" +
            "            \"name\": \"Self-expression\",\n" +
            "            \"category\": \"needs\",\n" +
            "            \"percentage\": 0.006771991960655199,\n" +
            "            \"children\": [\n" +
            "              {\n" +
            "                \"id\": \"Challenge\",\n" +
            "                \"name\": \"Challenge\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.1462723241117927,\n" +
            "                \"sampling_error\": 0.0717114844\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Closeness\",\n" +
            "                \"name\": \"Closeness\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.022737402795760586,\n" +
            "                \"sampling_error\": 0.0713261348\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Curiosity\",\n" +
            "                \"name\": \"Curiosity\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.3674660152353394,\n" +
            "                \"sampling_error\": 0.1001614856\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Excitement\",\n" +
            "                \"name\": \"Excitement\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.04089878567298039,\n" +
            "                \"sampling_error\": 0.0899203856\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Harmony\",\n" +
            "                \"name\": \"Harmony\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.012458800439233475,\n" +
            "                \"sampling_error\": 0.0896520856\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Ideal\",\n" +
            "                \"name\": \"Ideal\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.07155147639020804,\n" +
            "                \"sampling_error\": 0.0821927012\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Liberty\",\n" +
            "                \"name\": \"Liberty\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.029329578184054517,\n" +
            "                \"sampling_error\": 0.12202053040000001\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Love\",\n" +
            "                \"name\": \"Love\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.010630360533171623,\n" +
            "                \"sampling_error\": 0.0822588712\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Practicality\",\n" +
            "                \"name\": \"Practicality\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.11263264769174736,\n" +
            "                \"sampling_error\": 0.0729176192\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Self-expression\",\n" +
            "                \"name\": \"Self-expression\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.006771991960655199,\n" +
            "                \"sampling_error\": 0.0700432676\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Stability\",\n" +
            "                \"name\": \"Stability\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.10550476655170982,\n" +
            "                \"sampling_error\": 0.08936107680000001\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Structure\",\n" +
            "                \"name\": \"Structure\",\n" +
            "                \"category\": \"needs\",\n" +
            "                \"percentage\": 0.7397657021209085,\n" +
            "                \"sampling_error\": 0.06783009640000001\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"values\",\n" +
            "        \"name\": \"Values\",\n" +
            "        \"children\": [\n" +
            "          {\n" +
            "            \"id\": \"Self-enhancement_parent\",\n" +
            "            \"name\": \"Self-enhancement\",\n" +
            "            \"category\": \"values\",\n" +
            "            \"percentage\": 0.004194989604622668,\n" +
            "            \"children\": [\n" +
            "              {\n" +
            "                \"id\": \"Conservation\",\n" +
            "                \"name\": \"Conservation\",\n" +
            "                \"category\": \"values\",\n" +
            "                \"percentage\": 0.09807109217832172,\n" +
            "                \"sampling_error\": 0.062084738800000004\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Openness to change\",\n" +
            "                \"name\": \"Openness to change\",\n" +
            "                \"category\": \"values\",\n" +
            "                \"percentage\": 0.1420357059296135,\n" +
            "                \"sampling_error\": 0.0565946368\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Hedonism\",\n" +
            "                \"name\": \"Hedonism\",\n" +
            "                \"category\": \"values\",\n" +
            "                \"percentage\": 0.011761019463446543,\n" +
            "                \"sampling_error\": 0.113030484\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Self-enhancement\",\n" +
            "                \"name\": \"Self-enhancement\",\n" +
            "                \"category\": \"values\",\n" +
            "                \"percentage\": 0.004194989604622668,\n" +
            "                \"sampling_error\": 0.0863945912\n" +
            "              },\n" +
            "              {\n" +
            "                \"id\": \"Self-transcendence\",\n" +
            "                \"name\": \"Self-transcendence\",\n" +
            "                \"category\": \"values\",\n" +
            "                \"percentage\": 0.05870952071041813,\n" +
            "                \"sampling_error\": 0.0650828276\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"warnings\": []\n" +
            "}";
}