package com.itcast3.googleplay.ui.widget;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.util.UIUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout {
	private float ratio;

	//能够获取到xml中编写属性的构造方法
	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		getRatio(attrs);
	}

	private void getRatio(AttributeSet attrs) {
		//1,获取此控件中所有的属性
		TypedArray array = UIUtils.getContext().obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		//2,获取一个名称为ratio,类型为float类型的具体的属性值
		ratio = array.getFloat(R.styleable.RatioLayout_ratio, 0.0f);
	}
	//3,获取imageView宽高值,获取当前控件的宽高值
	//4,RatioLayout的ratioWidth宽度是定值
	//5,ratioWidth - getPaddingLeft()-getPaddingRight() = 图片的宽度
	//6,图片宽度/ratio = 图片高度
	//7,图片的高度+getPaddingTop()+getPaddingBottom();
	//8,将当前控件的高度模式高度精确值,重新拼接heightMeasureSpec参数
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//模式的获取
		//宽度模式(精确)
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		//高度的模式
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		//大小的获取
		//32位的二进制的(0,1),前两位代表模式(三种模式),后30位代表控件的具体大小
		//三种模式:精确,至多,未定义
		//MeasureSpec.EXACTLY 确切
		//MeasureSpec.AT_MOST 至多
		//MeasureSpec.UNSPECIFIED 未定义
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int imageWidth = widthSize-getPaddingRight()-getPaddingLeft();
		
		if(widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio!=0.0f){
			//控件的高度 = 图片高度+底部内间距+顶部的内间距
			heightSize = (int) (imageWidth/ratio+getPaddingBottom()+getPaddingTop());
			//告知此控件,按照我计算出来的高度去做绘画
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}