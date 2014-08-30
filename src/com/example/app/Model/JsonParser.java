package com.example.app.Model;

import com.example.app.Model.Data.QuestionImage;
import com.example.app.Model.Data.QuestionInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mustafa on 11/19/13.
 */
public class JsonParser {

    public ArrayList<QuestionInfo> ParseJson(String data)
    {
        ArrayList<QuestionInfo> qtsInfo = new ArrayList<QuestionInfo>();
        QuestionImage qtsImg;
        JSONObject jsonObject;
        JSONArray jArray;

        String question="";
        String questionType="";
        QuestionImage questionImage = null;
        String[] answers = null;
        String correctAns ="";
        String tFAns ="";
        String imgUrl="";

        //String data = (new HttpTask()).doInBackground();
        try{
            jsonObject = new JSONObject(data);
            jArray = jsonObject.getJSONArray("questions");
            JSONObject jObj;
            for(int i=0;i<jArray.length();i++)
            {
                jObj = jArray.getJSONObject(i);
                question = jObj.getString("question");
                questionType = jObj.getString("questionType");
                correctAns = jObj.getString("correctAnswer");
                imgUrl = jObj.getString("imageUrl");
                if(!imgUrl.equals("null"))
                {
                    questionImage = new QuestionImage(imgUrl);
                }
                else
                {
                    questionImage = null;
                }
                if(questionType.equals("multipleChoice"))
                {
                    JSONArray ja  = jObj.getJSONArray("incorrectAnswers");
                    answers = new String[]{ja.getString(0),ja.getString(1),ja.getString(2),correctAns};
                    JsonParser.shuffleArray(answers);
                    tFAns="";
                }
                else
                {
                    tFAns = jObj.getString("answer");
                    answers=null;
                }
                qtsInfo.add(new QuestionInfo(question,questionType,questionImage,answers,correctAns,tFAns));
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return qtsInfo;
    }

    static void shuffleArray(String[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
