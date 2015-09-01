package com.styytech.superviewpager.ui.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.styytech.superviewpager.R;

/**
 * 描述：自定义布局View<br>
 * 用于自定义布局轮播界面DEMO
 *
 * @author 鱼鱼科技
 */
public class CustomLtFragmentItem extends Fragment {
    private Context context;
    private View rootView;
    private TextView tv_title;
    private TextView tv_show;
    private ImageView iv_theme_img;
    private String content;

    /**
     * 构造
     */
    public CustomLtFragmentItem() {
    }


    public static CustomLtFragmentItem newInstance() {
        CustomLtFragmentItem fragment = new CustomLtFragmentItem();
        return fragment;
    }

    /**
     * 当fragment被加入到activity时调用（在这个方法中可以获得所在的activity）。交互的时候还是可以用到的。
     */
    @Override
    public void onAttach(Activity activity) {
        this.context = getActivity();
        super.onAttach(activity);
    }

    /**
     * 系统创建Fragments 时调用，可做执行初始化工作或者当程序被暂停或停止时用来恢复状态，跟Activity 中的onCreate相当。
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 用于首次绘制用户界面的回调方法，必须返回要创建的Fragments 视图UI。假如你不希望提供Fragments 用户界面则可以返回NULL。
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * 如果指定的子view已经有一个父view。<br>
         * 你首先必须让子view 脱离 父view 关系,及父view.removeview(子view)。
         */
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.item_banner_customlyt, null);
        }
        // rootView = inflater.inflate(R.layout.item_banner_customlyt,
        // container, false);
        initFragment();

        return rootView;
    }

    /**
     * 初始化Fragment视图界面
     */
    private void initFragment() {
        findView();
        fillData();

    }

    /**
     * 找控件
     */
    private void findView() {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_show = (TextView) rootView.findViewById(R.id.tv_show);
        iv_theme_img = (ImageView) rootView.findViewById(R.id.iv_theme_img);
        // 事件监听
        listener();

    }

    /**
     * 监听处理
     */
    private void listener() {
        // 图片监听
        iv_theme_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "你点击了大大LOGO", Toast.LENGTH_SHORT).show();
            }
        });
        // 布局监听
        // rootView.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // Toast.makeText(context, "你点击了整个布局", 0).show();
        // }
        // });
    }

    /**
     * 填数据
     */
    private void fillData() {
        content = getArguments().getString("content").toString();
        if (content != null) {
            tv_show.setText(content);
        }
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_show() {
        return tv_show;
    }

    public void setTv_show(TextView tv_show) {
        this.tv_show = tv_show;
    }

    public ImageView getIv_theme_img() {
        return iv_theme_img;
    }

    public void setIv_theme_img(ImageView iv_theme_img) {
        this.iv_theme_img = iv_theme_img;
    }

}
