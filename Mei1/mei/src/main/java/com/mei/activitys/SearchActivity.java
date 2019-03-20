package com.mei.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mei.R;

public class SearchActivity extends Activity {
    private TextView search;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activigy);
        ((TextView) findViewById(R.id.title)).setText("搜索");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        editText = (EditText) findViewById(R.id.search_edit);

        search = (TextView) findViewById(R.id.do_search);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText() != null) {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("keyword", editText.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(SearchActivity.this, "请先输入关键字", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
