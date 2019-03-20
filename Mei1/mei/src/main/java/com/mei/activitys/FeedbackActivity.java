package com.mei.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mei.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;

public class FeedbackActivity extends Activity implements OnClickListener {
	private ImageView mBack;
	private TextView mTitle;
	private EditText mInput;
	private TextView mSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText("Suggestion Feedback");
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
		mInput = (EditText) findViewById(R.id.input);
		mSubmit = (TextView) findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.submit:
			if(mInput.getText().length() > 0){
				FeedbackAgent agent = new FeedbackAgent(this);
				Conversation conversation = agent.getDefaultConversation();
				conversation.addUserReply(mInput.getText().toString());
				agent.sync();
				finish();
			}
			break;
		}
	}
}
