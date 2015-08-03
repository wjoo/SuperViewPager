package com.styytech.superviewpager.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.styytech.superviewpager.R;
import com.styytech.superviewpager.ui.demo.BannerActivity;
import com.styytech.superviewpager.ui.demo.CustomLtFragment;
import com.styytech.superviewpager.ui.demo.GuideActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * 该方法已被xml文件的button控件绑定， 注意方法类型必须是public类型
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_guide:// 引导界面Demo
			startCustomActivity(GuideActivity.class);
			break;
		case R.id.btn_banner://首頁轮播界面DEMO
			startCustomActivity(BannerActivity.class);
			break;
		case R.id.btn_custom://自定义布局轮播界面DEMO
			startCustomActivity(CustomLtFragment.class);
			break;

		default:
			break;
		}
	}

	/**
	 * 打开界面
	 * 
	 * @param clazz
	 */
	private void startCustomActivity(Class clazz) {

		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}
}
