package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.util.BitmapHelp;
import com.itcast3.googleplay.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailAppDesHolder extends BaseHolder<AppInfo>{
	private TextView app_des,app_author;
	private ImageView app_arrow;
	private AppInfo data;
	private LinearLayout ll_root;
	//默认显示最短高度(7行)
	private boolean isOpen = false;
	private ValueAnimator valueAnimator;
	private LayoutParams layoutParams;
	@Override
	public void refreshView() {
		data = getData();
		
		app_des.setText(data.getDes());
		app_author.setText(data.getAuthor());
		
		//获取一个七行显示textView方法
		int shortHeight = getShortHeight();
		//获取封装了高度的对象
		
		layoutParams = app_des.getLayoutParams();
		
		layoutParams.height = shortHeight;
		//设置修改了高度属性后的规则,给原生控件
		app_des.setLayoutParams(app_des.getLayoutParams());
		
		//点击view对象整个模块,需要去做一个扩展开的动作,并且如果内容超长,需要让屏幕滚动起来
		ll_root.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//扩展,收缩
				expend();
			}
		});
	}
	

	private void expend() {
		int shortHeight = getShortHeight();
		int longHeight = getLongHeight();
		if(!isOpen){
			//1,最短--->最长(扩展开来)
			isOpen = true;
			if(shortHeight<longHeight){
				valueAnimator = ValueAnimator.ofInt(shortHeight,longHeight);
			}
		}else{
			//2,最长--->最短(收缩起来)
			isOpen = false;
			if(shortHeight<longHeight){
				valueAnimator = ValueAnimator.ofInt(longHeight,shortHeight);
			}
		}
		
		if(valueAnimator!=null){
			//扩展app_des对应TextView过程
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					//valueAnimator包含控件在扩展或者收缩过程中,时时刻刻的高度具体值
					layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
					//将修改过高度的规则,作用在控件上
					app_des.setLayoutParams(layoutParams);
				}
			});
			
			valueAnimator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator arg0) {
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0) {
				}
				
				@Override
				public void onAnimationEnd(Animator arg0) {
					if(isOpen){
						//如果扩展开,就需要将箭头朝上
						app_arrow.setImageResource(R.drawable.arrow_up);
					}else{
						//如果没有扩展开,就需要将箭头朝下
						app_arrow.setImageResource(R.drawable.arrow_down);
					}
					//动画执行完成后,高度就是定值,如果此定值超出屏幕高度,则需要去做整个界面的滚动
					final ScrollView scrollView = getScrollView();
					//触底滚动方法
					scrollView.post(new Runnable() {
						@Override
						public void run() {
							//scrollView触底(焦点在最低端)的时候,全屏滚动
							scrollView.fullScroll(ScrollView.FOCUS_DOWN);
						}
					});
				}
				
				@Override
				public void onAnimationCancel(Animator arg0) {
				}
			});
			
			valueAnimator.start();
			valueAnimator.setDuration(300);
		}
	}

	//最大高度和最小(7行)高度,差异就在于,最短高度指定了7行显示
	private int getLongHeight() {
		//(精确模式,至多,未定义)
		int width = app_des.getMeasuredWidth();
		
		//结合模式和具体值,定义一个宽度的参数
		int widthMakeMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		int heightMakeMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
		
		//只能显示七行,模拟控件
		TextView textView = new TextView(UIUtils.getContext());
		textView.setText(data.getDes());
		//app_des得到的规则要作用在模拟的textView上,保持其高度一致
		textView.measure(widthMakeMeasureSpec, heightMakeMeasureSpec);

		return textView.getMeasuredHeight();
	}


	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_app_des);
		app_arrow = (ImageView) view.findViewById(R.id.app_arrow);
		
		app_des = (TextView) view.findViewById(R.id.app_des);
		app_author = (TextView) view.findViewById(R.id.app_author);
		
		ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
		
		return view;
	}

	private int getShortHeight() {
		//(精确模式,至多,未定义)
		int width = app_des.getMeasuredWidth();
		
		//结合模式和具体值,定义一个宽度的参数
		int widthMakeMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		int heightMakeMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
		
		//只能显示七行,模拟控件
		TextView textView = new TextView(UIUtils.getContext());
		textView.setMaxLines(7);
		textView.setLines(7);
		textView.setText(data.getDes());
		//app_des得到的规则要作用在模拟的textView上,保持其高度一致
		textView.measure(widthMakeMeasureSpec, heightMakeMeasureSpec);

		return textView.getMeasuredHeight();
	}
	
	//循环,循环跳出条件就是找到了scrollView对象
	public ScrollView getScrollView(){
		View parentView = (View) ll_root.getParent();
		//如果得到的夫控件是scrollView对象,则跳出循环,做返回scrollView操作
		while(!(parentView instanceof ScrollView)){
			//否则继续去找其夫控件,知道找到scrollView为止
			parentView = (View) parentView.getParent();
		}
		return (ScrollView) parentView;
	}
}
