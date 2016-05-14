package com.heima.mobileplayersh2.lyrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.heima.mobileplayersh2.R;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 */
public class LyricsView extends TextView{

    private Paint mPaint;
    private float mHighlightSize;
    private float mNormalSize;
    private int mLineHeight;
    private int mHighlightColor;
    private int mNormalColor;
    private int mHalfViewW;
    private int mHalfViewH;
    private List<Lyrics> mLyricsList;
    private int mCurentLine;
    private int mDuration;
    private int mPosition;

    public LyricsView(Context context) {
        super(context);
        initView();
    }

    public LyricsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LyricsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mHighlightSize = getResources().getDimension(R.dimen.lyric_highlight_size);
        mNormalSize = getResources().getDimension(R.dimen.lyric_normal_size);
        mLineHeight = getResources().getDimensionPixelSize(R.dimen.lyric_line_height);

        mHighlightColor = Color.GREEN;
        mNormalColor = Color.WHITE;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setTextSize(mHighlightSize); // 文字大小
        mPaint.setColor(mHighlightColor); // 文字颜色

//        // 模拟初始化数据
//        mLyricsList = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            mLyricsList.add(new Lyrics(i * 2000, "当前歌词行数为：" + i));
//        }
//
//        // 获取高亮行行数
//        mCurentLine = 15;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取view的一半宽高
        mHalfViewW = w / 2;
        mHalfViewH = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLyricsList == null || mLyricsList.size() == 0) {
            drawSingleText(canvas);
        } else {
            drawMulitText(canvas);
        }
    }

    /** 根据当前歌词数据列表和高亮行位置绘制多行文本 */
    private void drawMulitText(Canvas canvas) {
        Lyrics lyrics = mLyricsList.get(mCurentLine);

//        偏移位置 = 经过的时间百分比  *  行高
//        经过的时间百分比 = 经过的时间 / 行可用的时间
//        经过的时间 = 已播放时间 - 行起始时间
//        行可用时间 = 下一行起始时间 - 当前行起始时间
        int endPoint ;
        if (mCurentLine == mLyricsList.size()-1){
            // 最后一行
            endPoint = mDuration;
        }else {
            Lyrics nextLyric = mLyricsList.get(mCurentLine +1);
            endPoint = nextLyric.getStartPoint();
        }

        int lineTime = endPoint - lyrics.getStartPoint();

        int pastTime = mPosition - lyrics.getStartPoint();

        float pastPercent = pastTime / (float)lineTime;

        int offsetY = (int) (pastPercent * mLineHeight);

        canvas.translate(0, -offsetY);

        // 获取高亮行的Y位置
        Rect bounds = new Rect();
        mPaint.getTextBounds(lyrics.getContent(), 0, lyrics.getContent().length(), bounds);

        int halfTextH = bounds.height() / 2;
        int centerY = mHalfViewH + halfTextH /*- offsetY*/;

        // 按行绘制文本
        for (int i = 0; i < mLyricsList.size(); i++) {
            if (i == mCurentLine){
                // 高亮行
                mPaint.setTextSize(mHighlightSize);
                mPaint.setColor(mHighlightColor);
            }else {
                // 普通行
                mPaint.setTextSize(mNormalSize);
                mPaint.setColor(mNormalColor);
            }
            // x = 水平居中使用的x
            // y = 居中行的Y位置 + (绘制行的行数 - 高亮行的行数) * 行号
            int drawY = centerY + (i - mCurentLine) * mLineHeight;
            drawHorizontalText(canvas, mLyricsList.get(i).getContent(), drawY);
        }

    }

    /** 在view中间绘制一行文本 */
    private void drawSingleText(Canvas canvas) {
        String text = "正在加载歌词...";

        // 计算文本的一半宽高
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);

        int halfTextH = bounds.height() / 2;
        int drawY = mHalfViewH + halfTextH;
        drawHorizontalText(canvas, text, drawY);
    }

    /** 水平绘制一行文本 */
    private void drawHorizontalText(Canvas canvas, String text, int drawY) {
        //        int halfTextW = bounds.width() / 2;
        int halfTextW = (int) (mPaint.measureText(text) / 2);

//        x = view一半宽度 - 文字的一半宽度
//        y = view一半高度 + 文字的一半高度
        int drawX = mHalfViewW - halfTextW;
        canvas.drawText(text, drawX, drawY, mPaint);
    }

    /** 根据当前播放时间，选择高亮行 */
    public void roll(int position, int duration){
        mDuration = duration;
        mPosition = position;

        // 高亮行位置
//        起始时间 <= 播放时间
//        下一行起始时间 > 播放时间
        for (int i = 0; i < mLyricsList.size(); i++) {
            Lyrics lyrics = mLyricsList.get(i);
            int endPoint;

            if (i == mLyricsList.size() -1){
                // 最后一行
                endPoint = duration;
            }else {
                Lyrics nextLyrics = mLyricsList.get(i + 1);
                endPoint = nextLyrics.getStartPoint();
            }

            if(lyrics.getStartPoint() <= position && endPoint > position){
                mCurentLine = i;
                break;
            }
        }

        invalidate();
    }

    public void setLyricFile(File lyricFile){
        mLyricsList = LyricsParser.parserFromFile(lyricFile);
        mCurentLine = 0;
    }
}
