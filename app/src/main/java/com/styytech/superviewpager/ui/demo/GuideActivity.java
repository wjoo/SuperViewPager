package com.styytech.superviewpager.ui.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.styytech.superviewpager.R;
import com.styytech.superviewpager.widge.viewpagers.BannerView;
import com.styytech.superviewpager.widge.viewpagers.BannerView.IBViewSetDotView;
import com.styytech.superviewpager.widge.viewpagers.BannerView.IBViewSetPageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：引导界面DEMO
 *
 * @author 鱼鱼科技
 */
public class GuideActivity extends Activity {

    /**
     * 应用上下文
     */
    private Context context;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置界面无标题
        /**
         * 如果指定的子view已经有一个父view。<br>
         * 你首先必须让子view 脱离 父view 关系,及父view.removeview(子view)。
         */
//        if (null != rootView) {
//            ViewGroup parent = (ViewGroup) rootView.getParent();
//            if (null != parent) {
//                parent.removeView(rootView);
//            }
//        } else {
//            rootView = View.inflate(this, R.layout.activity_guide, null);
//        }
        View rootView = View.inflate(this, R.layout.activity_guide, null);
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
    }

    /**
     * 设置轮播图视图的一些属性
     *
     * @param bannerView 轮播图对象
     */
    private void setBannerView(final BannerView bannerView) {
        // 设置是否自动轮播
        // bannerView.setAutoPlay(true);
        // bannerView.setAutoPlayIntervalTime(1000);
        // 是否需要循环
        // bannerView.setLoop(true);
        // 是否需要小圆点
        bannerView.setDot(true);

        /** 设置轮播页的图片列表集 */
        bannerView.setOnAddPageView(new IBViewSetPageView() {

            @Override
            public List<Object> setPageView() {
                int[] resid = new int[]{R.drawable.no1, R.drawable.no2,
                        R.drawable.no3, R.drawable.no4, R.drawable.no5};
                List<Object> views = new ArrayList<Object>();
                for (int i = 0; i < resid.length; i++) {
                    ImageView img = new ImageView(context);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    img.setBackgroundResource(resid[i]);
                    views.add(img);
                }
                return views;
            }
        });
        /**
         * 设置轮播页的轮播点(小圆点图片)列表集接口 <br>
         * */
        bannerView.setOnAddDotView(new IBViewSetDotView() {

            @Override
            public void setDotView(ImageView imageView, int positon,
                                   int selected) {
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lparams.setMargins(5, 0, 0, 5);
                imageView.setLayoutParams(lparams);
                int[] resid = new int[]{R.drawable.mdot_red,
                        R.drawable.mdot_dlue, R.drawable.mdot_dlack,
                        R.drawable.mdot_yellow, R.drawable.mdot_write};
                // 获取轮播图页数
                // selected < resid.length 判断确保数组不越界
                if ((positon == selected) && (selected < resid.length)) {
                    imageView.setBackgroundResource(resid[selected]);
                } else {
                    imageView
                            .setBackgroundResource(R.drawable.mdot__new_default);
                }
            }
        });
        // 根据前面的设置，初始ViewPager
        try {
            bannerView.displayViewPager();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
