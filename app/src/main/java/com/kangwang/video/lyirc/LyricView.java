package com.kangwang.video.lyirc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kangwang.video.R;

import java.util.ArrayList;
import java.util.List;

public class LyricView extends TextView{

    private Paint paint;
    private int mHalfVieW;
    private int mHalfVieH;
    private List<LyricBean> lyricBeans;
    private int currentLine;
    private float hangHight;

    public LyricView(Context context) {
        super(context);
        init();
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //初始化高亮行的颜色  大小
        int mHightColor = getResources().getColor(R.color.white);
        float mHightSize = getResources().getDimension(R.dimen.hight_size);
        //初始化普通行的颜色   大小
        paint = new Paint();
        paint.setColor(mHightColor);
        paint.setTextSize(mHightSize);
        paint.setAntiAlias(true);

        lyricBeans = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            lyricBeans.add(new LyricBean(i*2000,"这是第"+i+"条数据"));
        }

        hangHight = getResources().getDimension(R.dimen.hang_size);
        currentLine = 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMult(canvas);
    }

    private void drawMult(Canvas canvas) {
        String text = lyricBeans.get(currentLine).getContent();
        Rect rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);

        //字体 的宽高
        int mHalfTextH = rect.height()/2;

        int centerY = mHalfVieH + mHalfTextH;
        for (int i = 0; i < lyricBeans.size() - 1; i++) {
            int drawY = (int)(centerY+(i-currentLine)*hangHight);
            drawHirLine(canvas, lyricBeans.get(i).getContent(), drawY);
        }
    }


    private void drawSingle(Canvas canvas) {
        String text = "加载中……";
        Rect rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);

        //字体 的宽高
        int mHalfTextH = rect.height()/2;

        int drawY = mHalfVieH + mHalfTextH;
        drawHirLine(canvas, text, drawY);
    }

    private void drawHirLine(Canvas canvas, String text, int drawY) {
        int mHalfTextW = (int)paint.measureText(text,0,text.length());
        int drawX = mHalfVieW - mHalfTextW/2;
        canvas.drawText(text,drawX,drawY,paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHalfVieW = w / 2;
        mHalfVieH = h / 2;
    }


    public void roll(int currentTime,int totalTime){
        for (int i = 0; i < lyricBeans.size(); i++) {
            int nextPoint;
            if (i == lyricBeans.size() - 1){
                nextPoint = totalTime;
            }else {

                nextPoint = lyricBeans.get(i+1).getStartTime();
            }

            int startTime = lyricBeans.get(i).getStartTime();
            if (currentTime >= startTime && currentTime<nextPoint){
                currentLine = i;
                break;
            }
        }
        invalidate();
    }
}
