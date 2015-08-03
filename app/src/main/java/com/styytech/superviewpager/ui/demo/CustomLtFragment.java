package com.styytech.superviewpager.ui.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.styytech.superviewpager.R;
import com.styytech.superviewpager.widge.viewpagers.BannerView;
import com.styytech.superviewpager.widge.viewpagers.BannerView.IBViewCurrentPageListener;
import com.styytech.superviewpager.widge.viewpagers.BannerView.IBViewSetPageView;

/**
 * 描述：自定义布局轮播界面DEMO
 *
 * @author 鱼鱼科技
 */
public class CustomLtFragment extends FragmentActivity {
    private Context context;
    private FragmentManager fm;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        /**
         * 如果指定的子view已经有一个父view。<br>
         * 你首先必须让子view 脱离 父view 关系,及父view.removeview(子view)。
         */
//		if (null != rootView) {
//			ViewGroup parent = (ViewGroup) rootView.getParent();
//			if (null != parent) {
//				parent.removeView(rootView);
//			}
//		} else {
//			rootView = View.inflate(this, R.layout.fragment_banner_customlyt,
//					null);
//		}
        View rootView = View.inflate(this, R.layout.fragment_banner_customlyt,
                null);
        setContentView(rootView);
        initFragment();

        BannerView bannerView = new BannerView(context, rootView,
                R.id.llyt_viewgroup, R.id.vp_viewpager);
        setBannerView(bannerView);
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
    }

    private void setBannerView(BannerView bannerView) {
        // 是否需要小圆点
        bannerView.setDot(true);
        // 设置小圆点的外边距参数 默认(3,0,3,0)
        bannerView.setImageParams(5, 10, 5, 10);
        // 设置小圆点的宽高，默认15,15
        bannerView.setImageWithHight(30, 40);
        // 设置默认小圆点的颜色
        bannerView.setmDotColor(Color.BLUE, Color.GRAY);

        // bannerView.setAutoPlay(true);
        // bannerView.setAutoPlayIntervalTime(5000);
        // 设置循环轮播
        bannerView.setLoop(true);

        /** 设置轮播页的图片列表集 */
        bannerView.setOnAddPageView(new IBViewSetPageView() {

            @Override
            public List<Object> setPageView() {
                List<Object> views = new ArrayList<Object>();
                for (int i = 0; i < 4; i++) {
                    views.add(new CustomLtFragmentItem(i + 1 + ""));
                }
                return views;
            }
        });

        /**
         * 设置滑动到当前轮播页所需要执行的事件<br>
         * 不需要更新数据点击事件，可以在CustomLtFragmentItem里面实现<br>
         * 而需要实时变动数据的话，比如tv_title值改变的话可以在这里实现<br>
         * 或者两者结合使用，总之用户可以灵活使用该接口.
         * */
        bannerView.setOnCurrentPageListener(new IBViewCurrentPageListener() {

            @Override
            public void setCurrentPageEvent(Object obj, final int positioncur) {
                CustomLtFragmentItem view = (CustomLtFragmentItem) obj;

                view.getTv_title().setText("自定义轮播的布局" + positioncur);
                /**
                 * obj属于Adapter.getItem方法返回的是一个新的Fragment实例，并不是当前的Fragment .<br>
                 * 所以在程序后台结束的时候，会出现view.getView() == null .<br>
                 * */
                if (view.getView() == null) {
                    return;
                }
                /**
                 * 如果没有特殊需求，该onClick监听可以在CustomLtFragmentItem实现。<br>
                 * （参照CustomLtFragmentItem 中listener()方法中的布局监听）
                 */
                view.getView().setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Log.i("123", "点击了" + positioncur);
                        Toast.makeText(context, positioncur + ":页被点击了", 0)
                                .show();
                    }
                });
            }
        });
        try {
            bannerView.displayViewPager();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
