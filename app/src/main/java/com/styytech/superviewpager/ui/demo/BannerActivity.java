package com.styytech.superviewpager.ui.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.styytech.superviewpager.MyApplication;
import com.styytech.superviewpager.R;
import com.styytech.superviewpager.widge.viewpagers.BannerView;
import com.styytech.superviewpager.widge.viewpagers.BannerView.IBViewCurrentPageListener;
import com.styytech.superviewpager.widge.viewpagers.BannerView.IBViewSetPageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：首頁轮播界面DEMO
 *
 * @author 鱼鱼科技
 */
public class BannerActivity extends Activity {

    /**
     * 应用上下文
     */
    private Context context;
    /**
     * 轮播图标题
     */
    private TextView tv_banner_titile;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置界面无标题
        View rootView = View.inflate(this, R.layout.activity_banner, null);
        setContentView(rootView);
        initView();
        BannerView bannerView = new BannerView(context, rootView,
                R.id.llyt_viewgroup, R.id.vp_viewpager);
        setBannerView(bannerView);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        context = this;
        findView();
    }

    /**
     * 找控件（初始化控件）
     */
    private void findView() {
        tv_banner_titile = (TextView) findViewById(R.id.tv_banner_titile);
    }

    /**
     * 设置轮播图视图的一些属性
     *
     * @param bannerView 轮播图对象
     */
    private void setBannerView(BannerView bannerView) {
        // 是否需要循环
        // bannerView.setLoop(true);
        // 设置是否自动轮播
        bannerView.setAutoPlay(true);
        bannerView.setAutoPlayIntervalTime(5000);
        // 是否需要小圆点默认是true
        // bannerView.setDot(false);

        /**
         * 参数null,表示使用系统默认轮播点. <br>
         * 这里不举例设置小圆点了，GuideActivity中有例子
         */
        // bannerView.setOnAddDotView(null);

        /** 设置轮播页的图片列表集 */
        bannerView.setOnAddPageView(new IBViewSetPageView() {

            @Override
            public List<Object> setPageView() {
                List<String> imgList = new ArrayList<String>();
                imgList.add("http://www.shstzz.com/ydimages/001.jpg");
                imgList.add("http://www.shstzz.com/ydimages/002.jpg");
                imgList.add("http://www.shstzz.com/ydimages/003.jpg");
                imgList.add("http://www.shstzz.com/ydimages/004.jpg");
                imgList.add("http://www.shstzz.com/ydimages/005.jpg");
                /** 用户自定义设置轮播页的图片列表集 */
                List<Object> pageViews = new ArrayList<Object>();
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    // 获取网络图片(Mainfest.xml要加访问网络权限)
                    MyApplication.showImage(imgList.get(i).toString(),
                            imageView, R.drawable.ic_launcher);
                    pageViews.add(imageView);
                }
                return pageViews;
            }
        });
        /** 设置滑动到当前轮播页所需要执行的事件 */
        bannerView.setOnCurrentPageListener(new IBViewCurrentPageListener() {

            @Override
            public void setCurrentPageEvent(Object obj, final int positioncur) {
                tv_banner_titile.setText("我是轮播标题" + positioncur);
                View view = (View) obj;
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.i("123", "" + positioncur);
                        Toast.makeText(context, positioncur + ":页被点击了", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });

        /**
         * 显示VIEWPAGER：轮播图片，轮播小圆点，轮播标题等。
         * 所以小圆点，轮播标题应在显示VIEWPAGER前进行自定义设置，不设置则默认系统设置
         */
        try {
            bannerView.displayViewPager();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
