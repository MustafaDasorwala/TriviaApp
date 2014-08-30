package com.example.app.Model.Data;

import java.io.Serializable;

/**
 * Created by Mustafa on 11/19/13.
 */
public class QuestionInfo implements Serializable{

    String mQuestion;
    String mQuestionType;
    QuestionImage mQuestionImage;
    String[] mAnswers;
    String mCorrectAns;
    String mTFAns;

    public QuestionInfo() {
    }

    public QuestionInfo(String mQuestion, String mQuestionType, QuestionImage mQuestionImage, String[] mAnswers, String mCorrectAns, String mTFAns) {
        this.mQuestion = mQuestion;
        this.mQuestionType = mQuestionType;
        this.mQuestionImage = mQuestionImage;
        this.mAnswers = mAnswers;
        this.mCorrectAns = mCorrectAns;
        this.mTFAns = mTFAns;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getQuestionType() {
        return mQuestionType;
    }

    public void setQuestionType(String mQuestionType) {
        this.mQuestionType = mQuestionType;
    }

    public QuestionImage getQuestionImage() {
        return mQuestionImage;
    }

    public void setQuestionImage(QuestionImage questionImage) {
        this.mQuestionImage = questionImage;
    }

    public String[] getAnswers() {
        return mAnswers;
    }

    public void setAnswers(String[] mAnswers) {
        this.mAnswers = mAnswers;
    }

    public String getCorrectAns() {
        return mCorrectAns;
    }

    public void setCorrectAns(String mCorrectAns) {
        this.mCorrectAns = mCorrectAns;
    }

    public String getTFAns() {
        return mTFAns;
    }

    public void setTFAns(String mTFAns) {
        this.mTFAns = mTFAns;
    }
}
