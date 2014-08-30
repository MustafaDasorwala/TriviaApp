package com.example.app;

import com.example.app.Model.Data.QuestionInfo;

import android.app.Fragment;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mustafa on 11/22/13.
 */
public class MultiFragment extends Fragment implements OnClickListener{
	
	QuestionInfo mQuestionInfo;
	ImageView mImageView;
	TextView mQuestionTextView;
	TextView[] mOptionsTextViews;
	ImageView mNextImageView;
	
	
	
    public QuestionInfo getQuestionInfo() {
		return mQuestionInfo;
	}



	public void setQuestionInfo(QuestionInfo mQuestionInfo) {
		this.mQuestionInfo = mQuestionInfo;
	}



	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_multi, container, false);
        mImageView =(ImageView) view.findViewById(R.id.imageView) ;
        mQuestionTextView = (TextView) view.findViewById(R.id.textView_question);
        mOptionsTextViews = new TextView[4];
        mOptionsTextViews[0] = (TextView) view.findViewById(R.id.textView_op1);
        mOptionsTextViews[1] = (TextView) view.findViewById(R.id.textView_op2);
        mOptionsTextViews[2] = (TextView) view.findViewById(R.id.textView_op3);
        mOptionsTextViews[3] = (TextView) view.findViewById(R.id.textView_op4);
        mNextImageView = (ImageView) view.findViewById(R.id.imageView_next);
        mOptionsTextViews[0].setOnClickListener(this);
        mOptionsTextViews[1].setOnClickListener(this);
        mOptionsTextViews[2].setOnClickListener(this);
        mOptionsTextViews[3].setOnClickListener(this);
        mNextImageView.setOnClickListener(this);
        
        FillData();
        return view;
    }
	
	public void FillData(){
		
		if(mQuestionInfo.getQuestionImage() != null)
        {
        	mImageView.setImageBitmap(mQuestionInfo.getQuestionImage().getBitmap());      	
        }
        mQuestionTextView.setText(mQuestionInfo.getQuestion());
        String[] ans = mQuestionInfo.getAnswers();
        mOptionsTextViews[0].setText(ans[0]);
        mOptionsTextViews[1].setText(ans[1]);
        mOptionsTextViews[2].setText(ans[2]);
        mOptionsTextViews[3].setText(ans[3]);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		mOptionsTextViews[0].setEnabled(false);
        mOptionsTextViews[1].setEnabled(false);
        mOptionsTextViews[2].setEnabled(false);
        mOptionsTextViews[3].setEnabled(false);
		
		if(v.getId() == R.id.imageView_next)
		{
			QuizActivity q = (QuizActivity) getActivity();
			q.nextFragment();
		}
		else{
			TextView tv = (TextView)v;
			if(tv.getText().equals(mQuestionInfo.getCorrectAns()))
			{
				Toast.makeText(getActivity(),"CORRECT", Toast.LENGTH_LONG).show();
				((QuizActivity) getActivity()).mScore++;
				final MediaPlayer mp1=MediaPlayer.create(getActivity(), R.raw.yes);  
		        mp1.start();
				
			}
			else{
				for(int i=0;i<mOptionsTextViews.length;i++){
					if(mOptionsTextViews[i].getText().equals(mQuestionInfo.getCorrectAns())){
						mOptionsTextViews[i].setTypeface(null,Typeface.BOLD);
					}
				}
			}
			getActivity().getActionBar().setTitle("Score:"+((QuizActivity)getActivity()).mScore+" HighScore:"+((QuizActivity)getActivity()).mHighScore);
			mNextImageView.setVisibility(1);
			
			
		}
		
	}
	
	
}