package com.dbgs.recyclercitylist;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import com.dbgs.recyclercitylist.listener.GroupListener;

/**
 * Created by acerhss on 17-10-27.
 */

public class GroupDecoration extends RecyclerView.ItemDecoration {
    @ColorInt
    int mGroupBackground = Color.parseColor("#00000000");//group背景色，默认透明
    int mGroupHeight = 80;  //悬浮栏高度
    boolean isAlignLeft = true; //是否靠左边
    @ColorInt
    int mDivideColor = Color.parseColor("#CCCCCC");//分割线颜色，默认灰色
    int mDivideHeight = 5;      //分割线高度

    Paint mDividePaint;
    private TextPaint mTextPaint;
    private Paint mGroutPaint;
    GroupListener groupListener;
    private int mGroupTextColor = Color.BLACK;//字体颜色，默认白色
    private int mSideMargin = 10;   //边距 靠左时为左边距  靠右时为右边距
    private int mTextSize = 40;     //字体大小

    public GroupDecoration(GroupListener groupListener) {
        super();
        mDividePaint = new Paint();
        mDividePaint.setColor(mDivideColor);
        this.groupListener = groupListener;

        //设置悬浮栏的画笔---mGroutPaint
        mGroutPaint = new Paint();
        mGroutPaint.setColor(mGroupBackground);
        //设置悬浮栏中文本的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mGroupTextColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int left = parent.getLeft()+parent.getPaddingLeft();
        int right = parent.getRight()- parent.getPaddingRight();
        int bottom = 0;
        int top = 0;
        for (int i=0;i<childCount;i++){
            final View childView = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(childView);

            bottom  = childView.getTop();
            boolean isGroup =  isGroup(pos);
            Log.e("StickDecoration"," i = "+i+"  isGroup = "+isGroup);
            if (isGroup){
                top = bottom - mGroupHeight;

                c.drawRect(left,top,right,bottom,mGroutPaint);

                String name = groupListener.getGroupName(pos);
                Log.e("StickDecoration", "GroupName = "+name);
                Rect rect = new Rect();
                mTextPaint.getTextBounds(name,0,name.length(),rect);
//                Log.e("StickDecoration","rect.bottom = "+rect.bottom+"   rect.height() = "+rect.height());

                c.drawText(name,left+rect.left,top+rect.bottom+rect.height(),mTextPaint);
            }else {
                top = bottom - mDivideHeight;
                c.drawRect(left,top,right,bottom,mDividePaint);
            }
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (isGroup(pos)){
            outRect.top = mGroupHeight;
        }else {
            outRect.top = mDivideHeight;
        }
    }



    ArrayMap<String, Integer>  group = new ArrayMap<>();
     boolean isGroup(int position){
        if (groupListener == null){
            return false;
        }
        String name = groupListener.getGroupName(position);
        if (group.containsKey(name)){
            if (group.get(name) == position ){
                return true;
            }
            return false;
        } else{
            group.put(name,position);
            return true;
        }
    }
}
