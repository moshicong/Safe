package com.itcast3.googleplay.holder;

import com.itcast3.googleplay.R;
import com.itcast3.googleplay.bean.AppInfo;
import com.itcast3.googleplay.http.HttpHelper;
import com.itcast3.googleplay.util.BitmapHelp;
import com.itcast3.googleplay.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailAppSafeHolder extends BaseHolder<AppInfo>{
	private ImageView[] imageView;
	private LinearLayout[] linearLayouts;
	private ImageView[] imageIcon;
	private TextView[] textViews;
	private LinearLayout ll_root;
	private LinearLayout ll_parent;
	private boolean isOpen = false;
	private int longHeight;
	private LayoutParams layoutParams;
	private ImageView arrow;

	@Override
	public void refreshView() {
		AppInfo data = getData();
		BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils();
		
		//data.getSafeUrlList()返回的就是顶部绿色图片的个数
		for(int i=0;i<4;i++){
			if(i<data.getSafeUrlList().size()){
				imageView[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(imageView[i],HttpHelper.URL+"image?name="+data.getSafeUrlList().get(i));
			}else{
				imageView[i].setVisibility(View.GONE);
			}
		}
		
		for(int i = 0;i<4;i++){
			if(i<data.getSafeDesUrlList().size()){
				linearLayouts[i].setVisibility(View.VISIBLE);
				textViews[i].setText(data.getSafeDesList().get(i));
				bitmapUtils.display(imageIcon[i],HttpHelper.URL+"image?name="+data.getSafeDesUrlList().get(i));
			}else{
				linearLayouts[i].setVisibility(View.GONE);
			}
		}
		
		getLongHeight();
	}

	private void getLongHeight() {
		ll_root.measure(0, 0);
		longHeight = ll_root.getMeasuredHeight();
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_appsafe);
		
		imageView = new ImageView[4];
		imageView[0] = (ImageView) view.findViewById(R.id.appsafe_icon1);
		imageView[1] = (ImageView) view.findViewById(R.id.appsafe_icon2);
		imageView[2] = (ImageView) view.findViewById(R.id.appsafe_icon3);
		imageView[3] = (ImageView) view.findViewById(R.id.appsafe_icon4);
		
		linearLayouts = new LinearLayout[4];
		linearLayouts[0] = (LinearLayout) view.findViewById(R.id.safe_ll1);
		linearLayouts[1] = (LinearLayout) view.findViewById(R.id.safe_ll2);
		linearLayouts[2] = (LinearLayout) view.findViewById(R.id.safe_ll3);
		linearLayouts[3] = (LinearLayout) view.findViewById(R.id.safe_ll4);
		
		
		imageIcon = new ImageView[4];
		imageIcon[0] = (ImageView) view.findViewById(R.id.safe_selector1);
		imageIcon[1] = (ImageView)view.findViewById(R.id.safe_selector2);
		imageIcon[2] = (ImageView)view.findViewById(R.id.safe_selector3);
		imageIcon[3] = (ImageView)view.findViewById(R.id.safe_selector4);

		textViews = new TextView[4];
		textViews[0] = (TextView) view.findViewById(R.id.safe_des1);
		textViews[1] = (TextView) view.findViewById(R.id.safe_des2);
		textViews[2] = (TextView) view.findViewById(R.id.safe_des3);
		textViews[3] = (TextView) view.findViewById(R.id.safe_des4);
		
		ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
		//获取其封装高度的对象
		layoutParams = ll_root.getLayoutParams();
		//手动将其高度字段设置成0
		layoutParams.height = 0;
		//将这个规则再一次作用在根线性布局上
		ll_root.setLayoutParams(layoutParams);
		
		ll_parent = (LinearLayout) view.findViewById(R.id.ll_parent);
		
		ll_parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				expend();
			}
		});
		
		arrow = (ImageView) view.findViewById(R.id.arrow);
		
		return view;
	}

	private void expend() {
		ValueAnimator animator = null;
		if(isOpen){
			//如果之前是打开--->关闭
			isOpen = false;
			//长的longHeight--->短的(0)
			animator = ValueAnimator.ofInt(longHeight,0);
		}else{
			//如果之前是关闭---?打开
			isOpen = true;
			//短的(0)--->长的longHeight
			animator = ValueAnimator.ofInt(0,longHeight);
		}
		
		if(animator!=null){
			//时刻监听对应控件要去扩展开的高度值
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					//控件在当前时刻所处的高度
					layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
					//时刻改变的高度赋值给控件的操作
					ll_root.setLayoutParams(layoutParams);
				}
			});
			//监听动画执行结束的操作
			animator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator arg0) {
					
				}
				@Override
				public void onAnimationRepeat(Animator arg0) {
					
				}
				@Override
				public void onAnimationEnd(Animator arg0) {
					if(isOpen){
						//展开,箭头朝上,告知用户可以缩回
						arrow.setImageResource(R.drawable.arrow_up);
					}else{
						//收缩,箭头朝下,告知用户可以展开
						arrow.setImageResource(R.drawable.arrow_down);
					}
				}
				@Override
				public void onAnimationCancel(Animator arg0) {
					
				}
			});
			
			animator.start();
			animator.setDuration(100);
		}
	}
}
