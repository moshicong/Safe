package com.itcast3.googleplay.fragment;

import com.itcast3.googleplay.ui.widget.LoadingPage.ResultState;
import com.itcast3.googleplay.util.UIUtils;

import android.view.View;
import android.widget.TextView;

public class GameFragment extends BaseFragment {

	@Override
	public View onCreateViewSuccessed() {
		TextView textView = new TextView(UIUtils.getContext());
		textView.setText("GameFragment");
		return textView;
	}

	@Override
	public ResultState onLoad() {
		return ResultState.STATE_SUCCESSED;
	}
}
