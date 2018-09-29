package com.hzs.github;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hzs.github.drag.ItemTouchHelperActivity;
/**
 * Created by hzs on 20180929
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView ryvTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ryvTxt = findViewById(R.id.ryv_drag);
        ryvTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ryv_drag:
                Intent intent = new Intent(this, ItemTouchHelperActivity.class);
                startActivity(intent);
                break;

        }
    }
}
