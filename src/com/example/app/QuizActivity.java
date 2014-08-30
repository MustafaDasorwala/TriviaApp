package com.example.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData.Item;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.app.Model.Data.QuestionInfo;
import com.example.app.Model.JsonParser;
import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends Activity {

    ArrayList<QuestionInfo> mQuestionInfoList;
    static int mCount = 0;
    Fragment mCurrentFragment;
    boolean mOrientationChange = false;
    public static int mScore=0;
    Menu mMenuActionBar;
    public int mHighScore;
    Bundle savedData;
    SharedPreferences mShPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

    /*    if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
        //Toast.makeText(QuizActivity.this,"ON_START", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        String jsonString = intent.getExtras().getBundle("bundle").getString("jsonString");
        //if(!mOrientationChange)
        //{
        	mQuestionInfoList = (new JsonParser()).ParseJson(jsonString);
        	shuffleArray(mQuestionInfoList);
        	loadSharedPrefrences();
        //}
        //mCurrentFragment = getFragmentManager().findFragmentById(R.id.container);
        //onStart();
        	setScoreInActionBar();
        	if(savedInstanceState != null)
        	{
        		mQuestionInfoList = (ArrayList<QuestionInfo>)savedInstanceState.getSerializable("questionList");
        	      mCount = (int) savedInstanceState.getInt("count", 0);
        	      mHighScore = savedInstanceState.getInt("highScore",0);
        	      mCount--;
        	}
    }
    
    private void loadSharedPrefrences()
    {
    	mShPref = PreferenceManager.getDefaultSharedPreferences(this);
    	mHighScore = mShPref.getInt("highScore", 0);
    	
    }
    
    static void shuffleArray(ArrayList<QuestionInfo> ar)
    {
        Random rnd = new Random();
        for (int i = ar.size() - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            QuestionInfo a = ar.get(index);
            ar.set(index, ar.get(i));
            ar.set(i, a);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //System.out.println("3333333333333333333333333333333");
        //Toast.makeText(this,"ON_START", Toast.LENGTH_LONG).show();
        nextFragment();
        //for(int i =0; i< mQuestionInfoList.size();)


    }
    
    public void nextFragment()
    {
    	FragmentManager fragmentManager =  getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //Toast.makeText(this,"ON_START", Toast.LENGTH_LONG).show();
        //setScoreInActionBar();
        if(mCount < mQuestionInfoList.size())
        {
        	//Toast.makeText(this,"1", Toast.LENGTH_LONG).show();
        	//setScoreInActionBar();
        	QuestionInfo qInfo =(QuestionInfo)mQuestionInfoList.get(mCount);
        	mCount++;
        	if(qInfo.getQuestionType().equals("multipleChoice")){
        		MultiFragment multiFragment = new MultiFragment();
        		multiFragment.setQuestionInfo(qInfo);
        		fragmentTransaction.replace(R.id.container,multiFragment);
        		//fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        		multiFragment.setRetainInstance(true);
        		fragmentTransaction.addToBackStack(null);
        		fragmentTransaction.commit();
        		//Toast.makeText(this,qInfo.getQuestionType(), Toast.LENGTH_LONG).show();
        		//mCurrentFragment = multiFragment;
        	}
        	else{
        		//Toast.makeText(this,"1", Toast.LENGTH_LONG).show();
        		BoolFragment boolFragment = new BoolFragment();
        		boolFragment.setQuestionInfo(qInfo);
        		boolFragment.setRetainInstance(true);
        		fragmentTransaction.replace(R.id.container,boolFragment);
        		fragmentTransaction.addToBackStack(null);
        		fragmentTransaction.commit();
        		//mCurrentFragment = boolFragment;
        	}
        }
        else
        {
        	//Toast.makeText(this,"1", Toast.LENGTH_LONG).show();
        	if(mScore > mHighScore)
        	{
        		mHighScore = mScore;
        	}
        	mShPref = PreferenceManager.getDefaultSharedPreferences(this);
        	Editor toEdit = mShPref.edit();
        	toEdit.putInt("highScore", mHighScore);
        	toEdit.commit();
        	Intent intent = new Intent(QuizActivity.this, FullscreenActivity.class);
        	startActivity(intent);
        	mCount =0;
        	mScore=0;        	
            finish();
        }
    }
    
    public void setScoreInActionBar(){
    	
    	/*MenuItem item = (MenuItem) mMenuActionBar.findItem(R.id.action_score);
    	item.setTitle("Score:"+mScore);*/
    	getActionBar().setTitle("Score:"+mScore+" HighScore:"+mHighScore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        mMenuActionBar = menu;
        //setScoreInActionBar();
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
        	Intent intent = new Intent(QuizActivity.this, SettingsActivity.class);
        	startActivity(intent);
        	//finish();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
            return rootView;
        }
    }
    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	mCount= mCount-1;
    	mOrientationChange=true;
    	super.onConfigurationChanged(newConfig);
    	mOrientationChange=false;
        //nextFragment();
    }*/
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);    
      savedInstanceState.putSerializable("questionList", mQuestionInfoList);
      savedInstanceState.putInt("count", mCount);
      savedInstanceState.putInt("highScore", mHighScore);
      mOrientationChange=true;
      //savedInstanceState.putBoolean("orientationChange", mOrientationChange);
      
    }
    
    /*@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      mQuestionInfoList = (ArrayList<QuestionInfo>)savedInstanceState.getSerializable("questionList");
      mCount = (int) savedInstanceState.getInt("count", 0);
      mHighScore = savedInstanceState.getInt("highScore",0);
      mCount--;
      
    }*/
    
    @Override
    public void onBackPressed() {
    	mCount=0;
    	mScore=0;
       Intent setIntent = new Intent( QuizActivity.this,FullscreenActivity.class);
       startActivity(setIntent);
       finish();
    }

}
