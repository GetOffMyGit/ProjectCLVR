package com.agile.dawndev.projectclvr;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

/**
 * Created by Zoe on 19/08/16.
 */
public class ToneAnalyzerAsync extends AsyncTask<Object, Void, String> {
    private TextView tv;
    boolean useAPI = false;
    String output = " {\n" +
            "   \"document_tone\": {\n" +
            "    \"tone_categories\": [\n" +
            "      {\n" +
            "        \"category_id\": \"emotion_tone\",\n" +
            "        \"category_name\": \"Emotion Tone\",\n" +
            "        \"tones\": [\n" +
            "          {\n" +
            "            \"tone_id\": \"anger\",\n" +
            "            \"tone_name\": \"Anger\",\n" +
            "            \"score\": 0.83414\n" +
            "          },\n" +
            "          {\n" +
            "            \"tone_id\": \"disgust\",\n" +
            "            \"tone_name\": \"Disgust\",\n" +
            "            \"score\": 0.229384\n" +
            "          },\n" +
            "          {\n" +
            "            \"tone_id\": \"fear\",\n" +
            "            \"tone_name\": \"Fear\",\n" +
            "            \"score\": 0.263215\n" +
            "          },\n" +
            "          {\n" +
            "            \"tone_id\": \"joy\",\n" +
            "            \"tone_name\": \"Joy\",\n" +
            "            \"score\": 0.018623\n" +
            "          },\n" +
            "          {\n" +
            "            \"tone_id\": \"sadness\",\n" +
            "            \"tone_name\": \"Sadness\",\n" +
            "            \"score\": 0.153338\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"category_id\": \"language_tone\",\n" +
            "        \"category_name\": \"Language Tone\",\n" +
            "        \"tones\": [\n" +
            "          {\n" +
            "            \"tone_id\": \"analytical\",\n" +
            "            \"tone_name\": \"Analytical\",\n" +
            "            \"score\": 0.665\n" +
            "           },\n" +
            "           {\n" +
            "             \"tone_id\": \"confident\",\n" +
            "             \"tone_name\": \"Confident\",\n" +
            "             \"score\": 0.0\n" +
            "           },\n" +
            "           {\n" +
            "             \"tone_id\": \"tentative\",\n" +
            "             \"tone_name\": \"Tentative\",\n" +
            "             \"score\": 0.0\n" +
            "           }\n" +
            "         ]\n" +
            "       },\n" +
            "       {\n" +
            "         \"category_id\": \"social_tone\",\n" +
            "         \"category_name\": \"Social Tone\",\n" +
            "         \"tones\": [\n" +
            "           {\n" +
            "             \"tone_id\": \"openness_big5\",\n" +
            "             \"tone_name\": \"Openness\",\n" +
            "             \"score\": 0.04\n" +
            "           },\n" +
            "           {\n" +
            "             \"tone_id\": \"conscientiousness_big5\",\n" +
            "             \"tone_name\": \"Conscientiousness\",\n" +
            "             \"score\": 0.076\n" +
            "           },\n" +
            "           {\n" +
            "             \"tone_id\": \"extraversion_big5\",\n" +
            "             \"tone_name\": \"Extraversion\",\n" +
            "             \"score\": 0.648\n" +
            "           },\n" +
            "           {\n" +
            "             \"tone_id\": \"agreeableness_big5\",\n" +
            "             \"tone_name\": \"Agreeableness\",\n" +
            "             \"score\": 0.869\n" +
            "           },\n" +
            "           {\n" +
            "             \"tone_id\": \"emotional_range_big5\",\n" +
            "             \"tone_name\": \"Emotional Range\",\n" +
            "             \"score\": 0.966\n" +
            "           }\n" +
            "         ]\n" +
            "       }\n" +
            "     ]\n" +
            "   },\n" +
            "   \"sentences_tone\": [\n" +
            "     {\n" +
            "       \"sentence_id\": 0,\n" +
            "       \"input_from\": 0,\n" +
            "       \"input_to\": 31,\n" +
            "       \"text\": \"I know the times are difficult!\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.26191\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.105675\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.312834\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.369193\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.303605\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.892\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.06\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.291\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.357\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.15\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.966\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 1,\n" +
            "       \"input_from\": 32,\n" +
            "       \"input_to\": 131,\n" +
            "       \"text\": \"Our sales have been disappointing for the past three quarters for our data analytics product suite.\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.437543\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.206324\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.137719\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.180326\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.272556\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.164\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.372\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.399\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.759\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.807\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 2,\n" +
            "       \"input_from\": 132,\n" +
            "       \"input_to\": 199,\n" +
            "       \"text\": \"We have a competitive data analytics product suite in the industry.\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.308401\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.298622\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.254084\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.161348\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.224839\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.628\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.926\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.031\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.526\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.54\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 3,\n" +
            "       \"input_from\": 200,\n" +
            "       \"input_to\": 237,\n" +
            "       \"text\": \"But we need to do our job selling it!\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.182246\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.101415\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.26452\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.486167\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.217572\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.013\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.018\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.833\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.952\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.975\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 4,\n" +
            "       \"input_from\": 238,\n" +
            "       \"input_to\": 290,\n" +
            "       \"text\": \"We need to acknowledge and fix our sales challenges.\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.362124\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.115696\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.396878\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.229465\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.150913\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.928\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.028\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.479\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.934\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.991\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.884\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 5,\n" +
            "       \"input_from\": 291,\n" +
            "       \"input_to\": 344,\n" +
            "       \"text\": \"We can’t blame the economy for our lack of execution!\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.546747\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.363746\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.421424\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.032694\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.140699\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.102\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.077\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.934\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.773\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.852\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 6,\n" +
            "       \"input_from\": 345,\n" +
            "       \"input_to\": 389,\n" +
            "       \"text\": \"We are missing critical sales opportunities.\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.146933\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.046159\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.216426\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.236162\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.486568\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.048\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.002\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.933\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.11\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.99\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 7,\n" +
            "       \"input_from\": 390,\n" +
            "       \"input_to\": 451,\n" +
            "       \"text\": \"Our product is in no way inferior to the competitor products.\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.2605\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.305878\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.225758\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.156873\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.349302\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.477\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.177\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.157\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.382\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.919\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 8,\n" +
            "       \"input_from\": 452,\n" +
            "       \"input_to\": 531,\n" +
            "       \"text\": \"Our clients are hungry for analytical tools to improve their business outcomes.\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.390098\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.194602\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.161436\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.322554\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.086525\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.236\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.138\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.73\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.741\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.895\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.604\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"sentence_id\": 9,\n" +
            "       \"input_from\": 532,\n" +
            "       \"input_to\": 566,\n" +
            "       \"text\": \"Economy has nothing to do with it.\",\n" +
            "       \"tone_categories\": [\n" +
            "         {\n" +
            "           \"category_id\": \"emotion_tone\",\n" +
            "           \"category_name\": \"Emotion Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"anger\",\n" +
            "               \"tone_name\": \"Anger\",\n" +
            "               \"score\": 0.365299\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"disgust\",\n" +
            "               \"tone_name\": \"Disgust\",\n" +
            "               \"score\": 0.359429\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"fear\",\n" +
            "               \"tone_name\": \"Fear\",\n" +
            "               \"score\": 0.348577\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"joy\",\n" +
            "               \"tone_name\": \"Joy\",\n" +
            "               \"score\": 0.066544\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"sadness\",\n" +
            "               \"tone_name\": \"Sadness\",\n" +
            "               \"score\": 0.245117\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"language_tone\",\n" +
            "           \"category_name\": \"Language Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"analytical\",\n" +
            "               \"tone_name\": \"Analytical\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"confident\",\n" +
            "               \"tone_name\": \"Confident\",\n" +
            "               \"score\": 0.0\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"tentative\",\n" +
            "               \"tone_name\": \"Tentative\",\n" +
            "               \"score\": 0.0\n" +
            "             }\n" +
            "           ]\n" +
            "         },\n" +
            "         {\n" +
            "           \"category_id\": \"social_tone\",\n" +
            "           \"category_name\": \"Social Tone\",\n" +
            "           \"tones\": [\n" +
            "             {\n" +
            "               \"tone_id\": \"openness_big5\",\n" +
            "               \"tone_name\": \"Openness\",\n" +
            "               \"score\": 0.46\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"conscientiousness_big5\",\n" +
            "               \"tone_name\": \"Conscientiousness\",\n" +
            "               \"score\": 0.224\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"extraversion_big5\",\n" +
            "               \"tone_name\": \"Extraversion\",\n" +
            "               \"score\": 0.062\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"agreeableness_big5\",\n" +
            "               \"tone_name\": \"Agreeableness\",\n" +
            "               \"score\": 0.275\n" +
            "             },\n" +
            "             {\n" +
            "               \"tone_id\": \"emotional_range_big5\",\n" +
            "               \"tone_name\": \"Emotional Range\",\n" +
            "               \"score\": 0.697\n" +
            "             }\n" +
            "           ]\n" +
            "         }\n" +
            "       ]\n" +
            "     }\n" +
            "   ]\n" +
            " }";

    @Override
    protected String doInBackground(Object... input) {
        ToneAnalyzer service = (ToneAnalyzer) input[0];
        String text = (String) input[1];
        tv = (TextView) input[2];
        if(this.useAPI){
            ToneAnalysis tone = service.getTone(text, null).execute();
            return tone.toString();
        } else{
            Log.d("Zoe: ", "Just using saved output");
            return this.output;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            tv.setText(s);
        }
    }

    interface Tone {
        void getText(String string);
    }
}
