package com.hzs.github.drag;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hzs.github.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by hzs on 20180929
 * */

public class ChannelDialogFragment extends DialogFragment implements GridRecyclerViewAdapter.ChannelItemClickListener, DialogInterface.OnKeyListener {
    private List<Channel> mDatas = new ArrayList<>();

    RecyclerView mRecyclerView;
    private ItemTouchHelper mHelper;
    private ImageView miVClose;
    private boolean isUpdate = false;
    private GridRecyclerViewAdapter adapter;
    List<Channel> mSelectedDatas;
    List<Channel> mUnSelectedDatas;
    private ChannelDialogFragmentListener mChannelDialogFragmentListener;
//    private boolean isFresh = false;//是否对栏目进行了增删操作

    public void setChannelDialogFragmentListener(ChannelDialogFragmentListener channelDialogFragmentListener) {
        mChannelDialogFragmentListener = channelDialogFragmentListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            //添加动画
            dialog.getWindow().setWindowAnimations(R.style.dialogSlideAnim);
        }
        return inflater.inflate(R.layout.dialog_channel, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setOnKeyListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        miVClose = (ImageView) view.findViewById(R.id.icon_collapse);
        miVClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChannelDialogFragmentListener != null) {
                    mChannelDialogFragmentListener.click(adapter.getMyChannelsData(), adapter.getOtherChannelsData(), "", adapter.isFresh());
                }
                dismiss();
            }
        });
        Bundle bundle = getArguments();
        mSelectedDatas = (List<Channel>) bundle.getSerializable("dataSelected");
        mUnSelectedDatas = (List<Channel>) bundle.getSerializable("dataUnselected");
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        adapter = new GridRecyclerViewAdapter(getActivity(), mSelectedDatas, mUnSelectedDatas, itemTouchHelper);
        adapter.setChannelItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int realPosition = adapter.getItemViewType(position);
                if (realPosition == GridRecyclerViewAdapter.MY_CHANNEL_NAME || realPosition == GridRecyclerViewAdapter.OTHER_CHANNEL_NAME) {
                    return 4;
                } else {
                    return 1;
                }

            }
        });
        mRecyclerView.setLayoutManager(manager);
    }

    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas, List<Channel> unselectedDatas) {
        ChannelDialogFragment dialogFragment = new ChannelDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataSelected", (Serializable) selectedDatas);
        bundle.putSerializable("dataUnselected", (Serializable) unselectedDatas);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void click(List<Channel> myChannel, List<Channel> otherChannel, String s, boolean isFresh) {
        if (mChannelDialogFragmentListener != null) {
            mChannelDialogFragmentListener.click(myChannel, otherChannel, s, isFresh);
        }
        dismiss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mChannelDialogFragmentListener != null) {
                mChannelDialogFragmentListener.click(adapter.getMyChannelsData(), adapter.getOtherChannelsData(), "", adapter.isFresh());
            }
            dismiss();
            return true;
        } else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            return false;
        }

    }


    public interface ChannelDialogFragmentListener {
        void click(List<Channel> myChannel, List<Channel> otherChannel, String s, boolean isFresh);

    }

}
