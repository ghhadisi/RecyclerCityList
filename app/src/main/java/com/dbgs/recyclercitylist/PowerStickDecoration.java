package com.dbgs.recyclercitylist;

import android.graphics.Bitmap;
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
import android.view.ViewGroup;

import com.dbgs.recyclercitylist.listener.GroupListener;
import com.dbgs.recyclercitylist.listener.PowerGroupListener;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by acerhss on 17-10-27.
 */

public class PowerStickDecoration extends RecyclerView.ItemDecoration {
    String TAG = "StickDecoration";
    @ColorInt
    int mGroupBackground = Color.WHITE;//group背景色，默认透明
    int mGroupHeight = 80;  //悬浮栏高度
    boolean isAlignLeft = true; //是否靠左边
    @ColorInt
    int mDivideColor = Color.parseColor("#CCCCCC");//分割线颜色，默认灰色
    int mDivideHeight = 5;      //分割线高度

    Paint mDividePaint;
    private TextPaint mTextPaint;
    private Paint mGroutPaint;
    PowerGroupListener groupListener;
    private int mGroupTextColor = Color.BLACK;//字体颜色，默认白色
    private int mSideMargin = 10;   //边距 靠左时为左边距  靠右时为右边距
    private int mTextSize = 40;     //字体大小

    public PowerStickDecoration(PowerGroupListener groupListener) {
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
//            Log.e("StickDecoration"," i = "+i+"  isGroup = "+isGroup);
            if (isGroup){
                top = bottom - mGroupHeight;

//                c.drawRect(left,top,right,bottom,mGroutPaint);

//                String name = groupListener.getGroupName(pos);
//                Log.e("StickDecoration", "GroupName = "+name);
//                Rect rect = new Rect();
//                mTextPaint.getTextBounds(name,0,name.length(),rect);
//                Log.e("StickDecoration","rect.bottom = "+rect.bottom+"   rect.height() = "+rect.height());

//                c.drawText(name,left+rect.left,top+rect.bottom+rect.height(),mTextPaint);


                View groupView = groupListener.getGroupView(pos);

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(right, mGroupHeight);
                groupView.setLayoutParams(layoutParams);
                groupView.setDrawingCacheEnabled(true);
                groupView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //指定高度、宽度的groupView
                groupView.layout(left, 0, right, mGroupHeight);
                Log.e(TAG,"groupView.getHeight() = "+groupView.getHeight());
                groupView.buildDrawingCache();
                Bitmap bitmap = groupView.getDrawingCache();
                c.drawBitmap(bitmap,left,top-mGroupHeight,mTextPaint);

            }else {
                top = bottom - mDivideHeight;
                c.drawRect(left,top,right,bottom,mDividePaint);
            }
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        if (childCount >0){
            int left = parent.getLeft()+parent.getPaddingLeft();
            int right = parent.getRight()- parent.getPaddingRight();
            View child = parent.getChildAt(0);

//            Log.e(TAG,"getTop = " +child.getTop()+"  getBottom = "+child.getBottom());
            int top =  child.getTop()- mGroupHeight;

            int position = parent.getChildAdapterPosition(child);
            c.drawRect(left,0,right,mGroupHeight,mGroutPaint);

            String text = groupName(position);
//            Log.e(TAG,"position = "+position +"  groupName = "+text);
            View groupView = groupListener.getGroupView(position);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(right, mGroupHeight);
            groupView.setLayoutParams(layoutParams);
            groupView.setDrawingCacheEnabled(true);
            groupView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //指定高度、宽度的groupView
            groupView.layout(0, 0, right, mGroupHeight);
            Log.e(TAG,"groupView.getHeight() = "+groupView.getHeight());
            groupView.buildDrawingCache();
            Bitmap bitmap = groupView.getDrawingCache();
            c.drawBitmap(bitmap,left,0,mTextPaint);



//            Rect rect = new Rect();
//            mTextPaint.getTextBounds(text,0,text.length(),rect);
//            c.drawText(text,left+rect.left,rect.bottom+rect.height(),mTextPaint);

        }
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
    TreeMap<Integer, String> group2 = new TreeMap<>();

    TreeSet<Integer> indexSet = new TreeSet<>();
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
            group2.put(position,name);
            return true;
        }
    }

    String groupName(int position){
        String result ="";
        Iterator<Map.Entry<Integer , String>> iterator= group2.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer , String>  entry=  iterator.next();
            int value = entry.getKey();
            if (position>=value){
                result = entry.getValue();

            }else {
                break;
            }
        }
        return  result;
    }
}
