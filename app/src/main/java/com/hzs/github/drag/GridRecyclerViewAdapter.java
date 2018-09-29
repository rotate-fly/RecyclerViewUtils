package com.hzs.github.drag;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.hzs.github.R;

import java.util.List;

/**
 * Created by hzs on 20180929
 */

public class GridRecyclerViewAdapter extends RecyclerView.Adapter implements OnItemMovedListener {
    List<Channel> myChannels;//选择的频道
    List<Channel> otherChannels;//其他频道
    public static final int MY_CHANNEL_NAME = 1;
    public static final int MY_CHANNELS = 3;
    public static final int OTHER_CHANNEL_NAME = 2;
    public static final int OTHER_CHANNELS = 4;

    ItemTouchHelper itemTouchHelper;
    private Activity mActivity;

    boolean isEditMode = false;
    private ChannelItemClickListener mChannelItemClickListener;
    private boolean isFresh = false;//是否对栏目进行了增删操作

    public boolean isFresh() {
        return isFresh;
    }

    public void setChannelItemClickListener(ChannelItemClickListener channelItemClickListener) {
        mChannelItemClickListener = channelItemClickListener;
    }

    public GridRecyclerViewAdapter(List<Channel> myChannels, List<Channel> otherChannels, ItemTouchHelper itemTouchHelper) {
        this.myChannels = myChannels;
        this.otherChannels = otherChannels;
        this.itemTouchHelper = itemTouchHelper;
    }

    public GridRecyclerViewAdapter(Activity mActivity, List<Channel> myChannels, List<Channel> otherChannels, ItemTouchHelper itemTouchHelper) {
        this.mActivity = mActivity;
        this.myChannels = myChannels;
        this.otherChannels = otherChannels;
        this.itemTouchHelper = itemTouchHelper;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MY_CHANNEL_NAME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_channel_header, parent, false);
                final MyChannelHeaderViewHolder myChannelHeaderViewHolder = new MyChannelHeaderViewHolder(view);

                return myChannelHeaderViewHolder;
            case MY_CHANNELS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my, parent, false);
                final MyChannelViewHolder myChannelViewHolder = new MyChannelViewHolder(view);
                myChannelViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (1 == myChannelViewHolder.getAdapterPosition())
                            return true;
                        itemTouchHelper.startDrag(myChannelViewHolder);
                        startEditMode((RecyclerView) parent);
                        View view1 = parent.getChildAt(0);
                        TextView textView = (TextView) view1.findViewById(R.id.tv_btn_edit);
                        if (textView != null) {
                            textView.setText("完成");
                        }

                        return true;
                    }
                });
                myChannelViewHolder.deleteIm.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (1 == myChannelViewHolder.getAdapterPosition())
                            return true;
                        itemTouchHelper.startDrag(myChannelViewHolder);
                        startEditMode((RecyclerView) parent);
                        View view1 = parent.getChildAt(0);
                        TextView textView = (TextView) view1.findViewById(R.id.tv_btn_edit);
                        if (textView != null) {
                            textView.setText("完成");
                        }

                        return true;
                    }
                });


                myChannelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEditMode)
                            return;
                        int position = myChannelViewHolder.getAdapterPosition();
                        if (1 == position)
                            return;
                        if (null != mChannelItemClickListener)
                            mChannelItemClickListener.click(myChannels, otherChannels, myChannelViewHolder.nameTv.getText().toString(), isFresh);
//                        Toast.makeText(mActivity, myChannelViewHolder.nameTv.getText(), Toast.LENGTH_SHORT).show();

                    }
                });
                myChannelViewHolder.deleteIm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = myChannelViewHolder.getAdapterPosition();
                        if (1 == position)
                            return;
                        RecyclerView recyclerView = (RecyclerView) parent;
                        if (isEditMode) {
                            View targetView = recyclerView.getLayoutManager().findViewByPosition(myChannels.size() + 2);
                            View currentView = recyclerView.getLayoutManager().findViewByPosition(position);
                            //如果目标targetView(indexOfChild>=0)在屏幕内，则需要添加动画效果；
                            // 如果不在屏幕内，那么recyclerview的notifyItemMoved会有向目标移动的动画
                            if (recyclerView.indexOfChild(targetView) >= 0) {
                                //目标位置的坐标
                                float targetX, targetY;
                                int spanCount = ((GridLayoutManager) (recyclerView.getLayoutManager())).getSpanCount();
                                //如果已选的item在最后一行的第一个位置，那么移动后高度会变化，所以目标View就是当前View的下一个view
                                if ((myChannels.size() - 1) % spanCount == 0) {
                                    View preTargetView = recyclerView.getLayoutManager().findViewByPosition(myChannels.size() + 1);
                                    targetX = preTargetView.getLeft();
                                    targetY = preTargetView.getTop();
                                } else {
                                    targetX = targetView.getLeft();
                                    targetY = targetView.getTop();
                                }
                                moveMineToOthers(myChannelViewHolder);
                                startAnimation((RecyclerView) parent, currentView, targetX, targetY);


                            } else {
                                moveMineToOthers(myChannelViewHolder);

                            }
                        }
                    }
                });
                return myChannelViewHolder;
            case OTHER_CHANNEL_NAME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_channel_header, parent, false);
                return new OtherHeaderViewHolder(view);
            case OTHER_CHANNELS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other, parent, false);
                final OthersViewHolder othersViewHolder = new OthersViewHolder(view);
                othersViewHolder.nameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerView recyclerView = (RecyclerView) parent;
                        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                        int currentPosition = othersViewHolder.getAdapterPosition();
                        View currentView = manager.findViewByPosition(currentPosition);
                        //目标位置的前一个
                        View preTargetView = manager.findViewByPosition(myChannels.size() - 1 + 1);
                        if (recyclerView.indexOfChild(preTargetView) >= 0) {
                            int targetX = preTargetView.getLeft();
                            int targetY = preTargetView.getTop();
                            int targetPosition = myChannels.size() - 1 + 2;
                            int spanCount = ((GridLayoutManager) manager).getSpanCount();
                            //目标位置在最后一行的第一个
                            if ((targetPosition - 1) % spanCount == 0) {
                                View targetView = manager.findViewByPosition(targetPosition);
                                targetX = targetView.getLeft();
                                targetY = targetView.getTop();
                            } else {
                                targetX += preTargetView.getWidth();
                                //最后一个item可见
                                if (manager.findLastVisibleItemPosition() == getItemCount() - 1) {
                                    //最后的item在最后一行的第一个
                                    if ((otherChannels.size() - 1) % spanCount == 0) {
                                        int firstVisiblePosition = manager.findFirstVisibleItemPosition();
                                        if (firstVisiblePosition == 0) {

                                        } else {
                                            targetY += preTargetView.getHeight();
                                        }

                                    }
                                }
                            }

                            //当前位置是最后一个并且目标位置不是每一行的第一个，则不会触发ItemAnimator,直接刷新界面
                            //因此此时要延时250执行notifyMove
                            if (currentPosition == manager.findLastVisibleItemPosition()

                                    && (targetPosition - 1) % spanCount != 0) {
                                moveOthersToMineWidthDelay(othersViewHolder);
                            } else {
                                moveOthersToMine(othersViewHolder);
                            }

                            startAnimation(recyclerView, currentView, targetX, targetY);


                        } else {
                            moveOthersToMine(othersViewHolder);
                        }


                    }
                });
                return othersViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyChannelViewHolder) {
            ((MyChannelViewHolder) holder).nameTv.setText(myChannels.get(position - 1).getChannelName());
            if (position == 1) {
                ((MyChannelViewHolder) holder).nameTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ((MyChannelViewHolder) holder).nameTv.setTextColor(Color.parseColor("#ef4836"));
            }
            if (isEditMode && position != 1) {
                ((MyChannelViewHolder) holder).deleteIm.setVisibility(View.VISIBLE);
            } else {
                ((MyChannelViewHolder) holder).deleteIm.setVisibility(View.INVISIBLE);
            }

        } else if (holder instanceof OthersViewHolder) {
            ((OthersViewHolder) holder).nameTv.setText(otherChannels.get(position - 2 - myChannels.size()).getChannelName());
        } else if (holder instanceof MyChannelHeaderViewHolder) {
            if (isEditMode) {
                ((MyChannelHeaderViewHolder) holder).editTextView.setText("完成");
            } else {
                ((MyChannelHeaderViewHolder) holder).editTextView.setText("编辑");
            }
            ((MyChannelHeaderViewHolder) holder).editTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isEditMode = !isEditMode;
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return myChannels.size() + otherChannels.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MY_CHANNEL_NAME;
        } else if (position > 0 && position < myChannels.size() + 1) {
            return MY_CHANNELS;
        } else if (position == myChannels.size() + 1) {
            return OTHER_CHANNEL_NAME;
        } else {
            return OTHER_CHANNELS;
        }

    }

    private void moveMineToOthers(RecyclerView.ViewHolder viewHolder) {
        isFresh = true;
        int startPosition = viewHolder.getAdapterPosition() - 1;
        Channel entity = myChannels.get(startPosition);
        myChannels.remove(startPosition);
        otherChannels.add(0, entity);
        notifyItemMoved(viewHolder.getAdapterPosition(), myChannels.size() + 2);

    }

    private void moveOthersToMine(RecyclerView.ViewHolder viewHolder) {
        isFresh = true;
        int position = viewHolder.getAdapterPosition();
        int startPosition = position - myChannels.size() - 2;
        Channel entity = otherChannels.get(startPosition);
        otherChannels.remove(startPosition);
        myChannels.add(entity);
        notifyItemMoved(position, myChannels.size() - 1 + 1);

    }

    private void moveOthersToMineWidthDelay(RecyclerView.ViewHolder viewHolder) {
        isFresh = true;
        final int position = viewHolder.getAdapterPosition();
        int startPosition = position - myChannels.size() - 2;
        Channel entity = otherChannels.get(startPosition);
        otherChannels.remove(startPosition);
        myChannels.add(entity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemMoved(position, myChannels.size() - 1 + 1);

            }
        }, 250);
    }

    private void startAnimation(final RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
        final ImageView tempImageView = addTempView((ViewGroup) recyclerView.getParent(), recyclerView, currentView);
        TranslateAnimation animation =
                new TranslateAnimation
                        (TranslateAnimation.RELATIVE_TO_SELF, 0f,
                                TranslateAnimation.ABSOLUTE, targetX - currentView.getLeft(),
                                TranslateAnimation.RELATIVE_TO_SELF, 0f,
                                TranslateAnimation.ABSOLUTE, targetY - currentView.getTop()
                        );
        animation.setDuration(360);
        animation.setFillAfter(true);
        currentView.setVisibility(View.INVISIBLE);
        tempImageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewGroup) (recyclerView.getParent())).removeView(tempImageView);
                currentView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private ImageView addTempView(ViewGroup parent, RecyclerView recyclerView, View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        ImageView tempImageView = new ImageView(parent.getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        tempImageView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int[] parentLocations = new int[2];
        recyclerView.getLocationOnScreen(parentLocations);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parentLocations[1], 0, 0);
        parent.addView(tempImageView, params);
        return tempImageView;
    }

    private void startEditMode(RecyclerView recyclerView) {
        isEditMode = true;
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = recyclerView.getChildAt(i);
            ImageView deleteIm = (ImageView) view.findViewById(R.id.img_edit);
            if (deleteIm != null && i != 1) {
                deleteIm.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (toPosition == 1) {
            if (fromPosition != 2)
                toPosition = 2;
            else
                return;
        }
        Channel Channel = myChannels.get(fromPosition - 1);
        myChannels.remove(fromPosition - 1);
        myChannels.add(toPosition - 1, Channel);
        isFresh = true;
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<Channel> getMyChannelsData() {
        return myChannels;
    }

    public List<Channel> getOtherChannelsData() {
        return otherChannels;
    }

    class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView editTextView;

        public MyChannelHeaderViewHolder(View itemView) {
            super(itemView);
            editTextView = (TextView) itemView.findViewById(R.id.tv_btn_edit);
        }
    }

    class MyChannelViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        ImageView deleteIm;

        public MyChannelViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.tv);
            deleteIm = (ImageView) itemView.findViewById(R.id.img_edit);
        }
    }

    class OtherHeaderViewHolder extends RecyclerView.ViewHolder {

        public OtherHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OthersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;

        public OthersViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public interface ChannelItemClickListener {
        void click(List<Channel> myChannel, List<Channel> otherChannel, String s, boolean isFresh);
    }
}
