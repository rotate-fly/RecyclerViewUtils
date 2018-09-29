package com.hzs.github.drag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hzs.github.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzs on 20180929
 * */
public class ItemTouchHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_item_touch_helper_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Channel> selectedDatas = new ArrayList<>();
        List<Channel> unselectedDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Channel channel = new Channel();
            channel.setChannelName("已选" + i);
            channel.setChannelId("" + i);
            selectedDatas.add(channel);
        }
        for (int i = 0; i < 10; i++) {
            Channel channel = new Channel();
            channel.setChannelName("未选" + i);
            channel.setChannelId(i + "");
            unselectedDatas.add(channel);
        }

        ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance(selectedDatas, unselectedDatas);
        dialogFragment.setChannelDialogFragmentListener(new ChannelDialogFragment.ChannelDialogFragmentListener() {
            @Override
            public void click(List<Channel> myChannel, List<Channel> otherChannel, String s, boolean isFresh) {

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "");
    }
}
