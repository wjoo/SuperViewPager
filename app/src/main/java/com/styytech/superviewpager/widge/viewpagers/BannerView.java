package com.styytech.superviewpager.widge.viewpagers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.styytech.superviewpager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播页控件的封装类<br>
 * 1.引导页<br>
 * 2.首页轮播<br>
 * 3.自定义布局轮播<br>
 * 注：演示DEMO:<br>
 * https://git.oschina.net/wjoo/SuperViewPager.git
 *
 * @author 鱼鱼科技
 */
public class BannerView {

    private Context mContext;
    private View mRootView;// 获取布局View对象
    private int mViewGroupId; // 小圆点布局id
    private int mViewPagerId; // VIEWPAGER布局id
    private boolean mIsDot = true; // 默认有圆点图
    private boolean mIsLoop = false; // 默认不可循环
    private ImageView[] mDot = null;// 存储小圆点的数组
    private boolean mIsAutoPlay = false;// 默认不自动轮播
    private int mIntervalTime = 3000;// 自动轮播间隔时间(默认3s)
    private List<Object> mPageViews;// 界面（图片）列表
    private ViewPager mViewPager;
    private ViewGroup mViewGroup;
    private int mViewSize = 0; // VIEWPAGER的页数
    private int mCurrentItem = 0;// 标识轮播页的真正位置
    private int mPositioncur = 0;// 标识轮播的当前位置以及小圆点下标
    private boolean mIsRunning = false;
    private boolean mIsAuto = false;// 默认没有设置自动
    private boolean mIsFinishUpdate = false;// 默认没有更新界面完毕

    /**
     * 标识适配哪种适配器,用于自动轮播时下标定位<br>
     * 0 - 适配 MyViewPagerAdapter<br>
     * 1 - 适配 MyFragmentstatePagerAdapter<br>
     */
    private int mMode = 0;

    /**
     * 接口变量
     */
    private IBViewSetPageView mIbAddPageView = null;
    private IBViewSetDotView mIbDotView = null;
    private IBViewCurrentPageListener mIbCurrentPageListener = null;
    /**
     * 小圆点的边距参数
     */
    private int mLeft = 3;
    private int mTop = 0;
    private int mRight = 3;
    private int mBottom = 0;
    /**
     * 轮播点（小圆点）大小
     */
    private int mWith = 15;
    private int mHeight = 15;
    /**
     * 轮播点（小圆点）颜色
     */
    private int mColorA = Color.RED;
    private int mColorB = Color.BLACK;

    /**
     * 实例化一个轮播视图实例 <br>
     * 其中viewGroupId,viewPagerId在此被初始化<br>
     * 注：fragment_banner.xml,fragment_guide.xml 用户需要在自己的布局中include即可<br>
     * &emsp;&emsp;也可以针对这两个布局做根据自己的需求做适当的修改
     *
     * @param context     应用上下文
     * @param viewGroupId 轮播点控件布局id
     * @param viewPagerId 轮播图控件布局id
     */
    public BannerView(Context context, View rootView, int viewGroupId,
                      int viewPagerId) {
        this.mContext = context;
        this.mRootView = rootView;
        this.mViewGroupId = viewGroupId;
        this.mViewPagerId = viewPagerId;
    }

    /**
     * 实例化一个轮播视图实例 <br>
     * 其中viewPagerId在此被初始化<br>
     * 注：fragment_banner.xml,fragment_guide.xml 用户需要在自己的布局中include即可<br>
     * &emsp;&emsp;也可以针对这两个布局做根据自己的需求做适当的修改
     *
     * @param context     应用上下文
     * @param viewPagerId 轮播图控件布局id
     */
    public BannerView(Context context, View rootView, int viewPagerId) {
        this.mContext = context;
        this.mRootView = rootView;
        this.mViewPagerId = viewPagerId;
    }

    /**
     * 实例化一个轮播视图实例 <br>
     * 其中viewGroup,viewPager在外面初始化<br>
     * 注：fragment_banner.xml,fragment_guide.xml 用户需要在自己的布局中include即可<br>
     * &emsp;&emsp;也可以针对这两个布局做根据自己的需求做适当的修改
     *
     * @param context   应用上下文
     * @param viewGroup 轮播点控件布局,可以为null.
     * @param viewPager 轮播图控件布局,不能为null.
     */
    public BannerView(Context context, ViewGroup viewGroup,
                      ViewPager viewPager) {
        this.mContext = context;
        this.mViewGroup = viewGroup;
        this.mViewPager = viewPager;
    }

    /**
     * 显示VIEWPAGER：轮播图片，轮播小圆点，轮播标题等。 <br>
     * 所以轮播页，轮播小点，轮播标题应在显示VIEWPAGER前进行自定义设置，不设置则默认系统设置<br>
     *
     * @throws Exception 抛出的异常
     * @see 强制用户抓取异常，确保代码健壮性<br>
     * 出现异常，注意系统日志 Tag = System.err
     */
    public void displayViewPager() throws Exception {

        if (mRootView != null) {
            mViewGroup = (ViewGroup) mRootView.findViewById(mViewGroupId);
            mViewPager = (ViewPager) mRootView.findViewById(mViewPagerId);
        }
        /*mViewGroup 可以为空*/
        if (mViewGroup != null) {
            mViewGroup.removeAllViews();
        }else{
            mIsDot = false;// mViewGroup 为空,则隐藏轮播点
        }
        mViewPager.removeAllViews();// 当mViewPager为空则抛出异常
        /**
         * VIEWPAGER页面的资源文件获取方式,由用户自定义资源文件
         * */
        mPageViews = getPageView();
        mViewSize = mPageViews.size();
        if (mViewSize == 1) {// 一张轮播图则默认不循环
            mIsLoop = false;
        } else if (mIsLoop && mViewSize < 4) {// 循环且少于4张则重复添加张数
            mPageViews.addAll(getPageView());
        }

        // 将小圆点加入到ViewGroup中
        mDot = new ImageView[mViewSize];
        for (int i = 0; i < mDot.length; i++) {
            // 设置了无圆点图，则直接跳过
            if (!mIsDot){
                break;
            }
            ImageView imageView = new ImageView(mContext);
            if (mIbDotView == null) {// 默认轮播小点
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        mWith, mHeight);
                lparams.setMargins(mLeft, mTop, mRight, mBottom);

                imageView.setLayoutParams(lparams);

                if (i == 0) {
                    imageView.setBackgroundColor(mColorA);
                } else {
                    imageView.setBackgroundColor(mColorB);
                }
            } else {// 用户自定义轮播点
                mIbDotView.setDotView(imageView, i, 0);
            }

            mDot[i] = imageView;
            mViewGroup.addView(imageView);
        }

        /** 设置pageChange监听 */
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        /** 绑定适配器 */
        if (mPageViews.get(0) instanceof View) {// 引导页或轮播图
            mMode = 0;
            mViewPager.setAdapter(new MyViewPagerAdapter());
        } else if (mPageViews.get(0) instanceof Fragment) {// 自定义布局
            mMode = 1;
            MyFragmentStatePagerAdapter fpAdapter = new MyFragmentStatePagerAdapter(
                    ((FragmentActivity) mContext).getSupportFragmentManager());
            mViewPager.setAdapter(fpAdapter);
        }

        /** 如果自动轮播，则需循环轮播（如果一開始要求不向左循环，则该代码块放在设置适配器之前） */
        if (mIsAutoPlay && (mViewSize > 1)) {
            // 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
            mCurrentItem = mPageViews.size() * 100;
            mViewPager.setCurrentItem(mCurrentItem);
            startPlay();
        } else if (mIsLoop) {
            // 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
            mCurrentItem = mPageViews.size() * 100;
            mViewPager.setCurrentItem(mCurrentItem);
        }

    }

    /**
     * 获取轮播图的图片集<br>
     * 如果用户未实现IBViewSetPageView接口则执行默认轮播图片集
     *
     * @return
     * @throws ServiceException
     */
    @SuppressLint("Recycle")
    private List<Object> getPageView() {
        List<Object> pageViews = new ArrayList<Object>();
        if (mIbAddPageView == null) {// 默认的图片集
            for (int i = 0; i < 4; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.drawable.ic_launcher);
                pageViews.add(imageView);
            }
            return pageViews;
        }
        // 用户自定的图片集
        pageViews = mIbAddPageView.setPageView();
        return pageViews;
    }

    /**
     * 轮播页面的适配器
     *
     * @author 鱼鱼科技
     */
    private class MyViewPagerAdapter extends PagerAdapter {
        /**
         * 实例化一个VIEWPAGER适配器
         */
        public MyViewPagerAdapter() {
        }

        @Override
        public void finishUpdate(View container) {
            mIsFinishUpdate = true;// 已经执行过 startUpdate() 并且完成了 实例一个viewpager 项
            // Log.i("123", "finishUpdate");
        }

        // 获得当前界面数
        @Override
        public int getCount() {
            // VIEWPAGER循环则返回界面可尽量多点,否则返回正常面数
            if (mIsLoop) {
                return Integer.MAX_VALUE;
            } else {
                return mPageViews.size();
            }
        }

        /**
         * 实例化每一页VIEWPAGER.<br>
         * 每次滑动都会调换用该方法，包括实例化VIEWPAGER适配器时也会调用该方法
         */
        @Override
        public Object instantiateItem(View container, int position) {
            // Log.i("123", "instantiateItem_arg0:" + position + "");
            Object view = mPageViews.get(position % mPageViews.size());
            try {
                ((ViewPager) container).addView((View) view, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;

        }

        // 销毁position位置的界面
        @Override
        public void destroyItem(View container, int position, Object object) {
            Object view = mPageViews.get(position % mPageViews.size());
            ((ViewPager) container).removeView((View) view);
            // Log.i("123", "destroyItem");
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            /**
             * 首次适配时，及时监听 这里可以不需要
             */
            // if (ibCurrentPageListener == null) {
            // // 默认执行的事件
            // } else {// 用户自定义执行的事件
            // ibCurrentPageListener.setCurrentPageEvent(
            // pageViews.get(positioncur), positioncur % vSize);
            // }

            /** 如果设置了自动轮播并且线程处于关闭状态，执行开启 */
            if (mIsAutoPlay && !mIsRunning) {
                startPlay();
            }
            return (arg0 == arg1);
        }

        // 如果item位置没有发生改变则返回POSITION_UNCHANGED如果发生了改变则返回POSITION_NONE
        @Override
        public int getItemPosition(Object object) {
            // Log.i("123", "getItemPosition");
            return super.getItemPosition(object);
        }

        // 恢复与adapter关联的所有页面的状态实例，并且该页面是通过saveState()方法保存的。
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // Log.i("123", "restoreState");
        }

        // 保存于adapter关联的所有的页面，直到调用restoreState方法时被恢复
        @Override
        public Parcelable saveState() {
            // Log.i("123", "111saveState");
            stopPlay();// 停止轮播
            return null;
        }

        // 开始调用显示页面
        @Override
        public void startUpdate(View arg0) {
            // Log.i("123", "startUpdate");
        }

    }

    /**
     * 自定义布局轮播的适配器
     *
     * @author 鱼鱼科技
     */
    private class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // VIEWPAGER循环则返回界面可尽量多点,否则返回正常面数
            if (mIsLoop) {
                return Integer.MAX_VALUE;
            } else {
                return mPageViews.size();
            }
        }

        @Override
        public Fragment getItem(int position) {
            // Log.i("123", "getItem_arg0:" + position + "");
            return (Fragment) mPageViews.get(position % mPageViews.size());
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            mIsFinishUpdate = true;// 已经执行过 startUpdate() 并且完成了 实例一个viewpager 项
            // Log.i("123", "finishUpdate");
            super.finishUpdate(container);
        }

        // 销毁position位置的界面
        @Override
        public void destroyItem(View container, int position, Object object) {
            Object view = mPageViews.get(position % mPageViews.size());
            ((ViewPager) container).removeView((View) view);

        }

        /**
         * 实例化适配器之后会调用该方法判断是否与instantiateItem方法返回的object有关联<br>
         * 总之实例化之后会调用该方法
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            /** 首次适配时，及时监听 */
            if (mIbCurrentPageListener == null) {
                // 默认执行的事件
            } else {// 用户自定义执行的事件
                // Fragment fragment = (Fragment)object;
                // if(fragment.getView() != null){
                // mIbCurrentPageListener.setCurrentPageEvent(object,
                // mPositioncur
                // % mViewSize);
                // }
                mIbCurrentPageListener.setCurrentPageEvent(object, mPositioncur
                        % mViewSize);
            }
            /** 如果设置了自动轮播并且线程处于关闭状态，执行开启 */
            if (mIsAutoPlay && !mIsRunning) {
                // Log.i("123", "isViewFromObject:"+super.isViewFromObject(view,
                // object));
                startPlay();
            }
            return super.isViewFromObject(view, object);
        }

        // 保存于adapter关联的所有的页面，直到调用restoreState方法时被恢复
        @Override
        public Parcelable saveState() {
            // Log.i("123", "222saveState");
            stopPlay();// 停止轮播
            return null;
        }

        // 开始调用显示页面
        @Override
        public void startUpdate(View arg0) {
            // Log.i("123", "startUpdate");
        }

    }

    /**
     * 实现页面切换监听
     *
     * @author 鱼鱼科技
     */
    private class MyOnPageChangeListener implements OnPageChangeListener {
        /**
         * // 当滑动状态改变时调用<br>
         * 此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。 <br>
         * arg0==1的时辰默示正在滑动<br>
         * arg0==2的时辰默示滑动完毕了<br>
         * arg0==0的时辰默示什么都没做<br>
         * 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
         */
        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:
                    mIsAutoPlay = false;
                    break;
                case 2:// 如果设置了自动轮播，则开启轮播
                    mIsAutoPlay = mIsAuto;
                    break;
                case 0:
                    mCurrentItem = mViewPager.getCurrentItem();
                    // Log.i("123", "onPageScrollStateChanged_mCurrentItem:"
                    // + mCurrentItem);
                    break;
            }
        }

        /**
         * // 当新的页面被选中时调用<br>
         * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。<br>
         * 其中三个参数的含义分别为：<br>
         * arg0 :当前页面，及你点击滑动的页面 <br>
         * arg1:当前页面偏移的百分比<br>
         * arg2:当前页面偏移的像素位置
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            mIsFinishUpdate = false;// 还未执行 startUpdate();
            // Log.i("123", "onPageSelected_arg0:" + arg0 + "");
            mPositioncur = arg0 % mPageViews.size();// 记录当前页下标
            setImageBackground(mPositioncur % mViewSize);// 设置小圆点下标
            if (mIbCurrentPageListener == null) {
                // 默认执行的事件
            } else {// 用户自定义执行的事件
                Object object = mPageViews.get(mPositioncur);
                // Fragment fragment = (Fragment)object;
                // if(fragment.getView() != null){
                // mIbCurrentPageListener.setCurrentPageEvent(object,
                // mPositioncur
                // % mViewSize);
                // }
                mIbCurrentPageListener.setCurrentPageEvent(object, mPositioncur
                        % mViewSize);
            }
        }
    }

    /**
     * 设置选中的mdot的背景
     *
     * @param selectItems 当前页下标
     */
    private void setImageBackground(int selectItems) {
        if (!mIsDot || (mDot == null)) {// 如果设置了没有小圆点则跳过设置
            return;
        }

        if (mIbDotView == null) {// 如果用户没有设置轮播小圆点则默认小圆点设置

            for (int i = 0; i < mDot.length; i++) {
                if (i == selectItems) {
                    mDot[i].setBackgroundColor(mColorA);
                } else {
                    mDot[i].setBackgroundColor(mColorB);
                }
            }
        } else {// 用户自定义的小圆点设置
            for (int i = 0; i < mDot.length; i++) {
                mIbDotView.setDotView(mDot[i], i, selectItems);
            }
        }
    }

    /**
     * 启动自动轮播
     */

    private void startPlay() {
        mIsRunning = true;
        new Thread(new SlideShowTask()).start();
    }

    /**
     * 停止自动轮播<br>
     * 确保避免残存的线程，最好执行
     */
    public void stopPlay() {
        mIsRunning = false;
    }

    /**
     * 开启新任务，实现Runnable接口
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            /** 由于适配器机制不同, 首次轮播需处理下标定位 */
            if (mMode == 0) {
                mCurrentItem--;
            } else if (mMode == 1) {
                mCurrentItem--;
                mCurrentItem--;
            }
            boolean isFirst = true;
            while (mIsRunning) {
                // Log.i("123", "isRuning");
                if (!mIsFinishUpdate) {// 如果没有更新界面完毕，或者后台进程运行则结束进程
                    mIsRunning = false;
                    break;
                }
                // 轮播间隔时间最少500毫秒
                if (mIntervalTime < 1000) {
                    mIsRunning = false;
                    break;
                }
                if (mIsAutoPlay) {
                    mCurrentItem = mCurrentItem + 1;
                    handler.sendEmptyMessage(1);
                }

                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                // 這是延時，注意要放在while語句最後。
                try {
                    Thread.sleep(mIntervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 刷新手段
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mViewPager.setCurrentItem(mCurrentItem);
                    break;
                default:
                    break;
            }

        }

        ;

    };

    /** --- 以下均是对外提供的方法 --- */

    /**
     * 设置默认小圆点的外边距参数 默认(3,0,3,0)
     *
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     */
    public void setImageParams(int left, int top, int right, int bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    /**
     * 设置默认小圆点的宽高，默认15,15
     *
     * @param with   轮播点宽
     * @param height 轮播点高
     */
    public void setImageWithHight(int with, int height) {
        this.mWith = with;
        this.mHeight = height;
    }

    /**
     * 设置默认小圆点的颜色
     *
     * @param mColorA 轮播到当前页小圆点颜色
     * @param mColorB 其他小圆点的颜色
     */
    public void setmDotColor(int mColorA, int mColorB) {
        this.mColorA = mColorA;
        this.mColorB = mColorB;
    }

    /**
     * 判断是否有小圆点
     *
     * @return true - 有，false - 无
     */
    public boolean isDot() {
        return mIsDot;
    }

    /**
     * 设置是否需要小圆点
     *
     * @param isDot 默认true
     */
    public void setDot(boolean isDot) {
        this.mIsDot = isDot;
    }

    public boolean isLoop() {
        return mIsLoop;
    }

    /**
     * 设置是否循环轮播<br>
     * 如果设置了自动轮播，则该设置将被改成true
     *
     * @param isLoop 默认false
     */
    public void setLoop(boolean isLoop) {
        this.mIsLoop = isLoop;
    }

    /**
     * 判断是否自动轮播
     *
     * @return
     */
    public boolean isAutoPlay() {
        return mIsAutoPlay;
    }

    /**
     * 设置是否自动轮播，默认false.<br>
     * 自动轮播模式，遵循循环轮播
     *
     * @param isAutoPlay
     */
    public void setAutoPlay(boolean isAutoPlay) {
        this.mIsAutoPlay = isAutoPlay;

        if (isAutoPlay) {
            mIsAuto = true;
            mIsLoop = true;
        }
    }

    /**
     * 获得设置自动轮播的间隔时间
     *
     * @return
     */
    public int getAutoPlayIntervalTime() {
        return mIntervalTime;
    }

    /**
     * 设置自动轮播的间隔时间， intervalTime >= 1000 毫秒；
     *
     * @param intervalTime
     */
    public void setAutoPlayIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }

    /**
     * 获得轮播图页数
     *
     * @return
     */
    public int getCount() {
        if (mDot == null) {
            return 0;
        } else {
            return mDot.length;
        }
    }

    /**
     * 设置轮播图的宽高比；
     * 注：默认宽度,高度为用户布局文件设置的宽高度
     *
     * @param flyt_container 轮播布局容器
     * @param scale          轮播图的宽高比
     */
    public void setAspectRatio(View flyt_container, int scale) {
        //获取屏幕宽度
        int widthPx = getWindowsWidth();
        //实例布局参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, widthPx / scale);
        //设置布局参数
        flyt_container.setLayoutParams(params);
    }

    /**
     * 设置轮播图的宽高比；
     * 注：margindp，是轮播图布局的左右外边距（或者内边距）的和，且默认是左右对称的.
     *
     * @param flyt_container 轮播布局容器
     * @param scale          轮播图的宽高比
     * @param margindp       轮播图布局容器的横向外/内边距总和
     */
    public void setAspectRatio(View flyt_container, int scale, int margindp) {
        int marginPx = dip2px(margindp);
        //轮播图布局的实际宽度
        int widthPx = getWindowsWidth() - marginPx;
        //实例布局参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, widthPx / scale);
        params.setMargins(marginPx / 2, 0, marginPx / 2, 0);
        //设置布局参数
        flyt_container.setLayoutParams(params);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float scale = metrics.density;
        return (int) (dpValue * scale + 0.5f);
//		return dpValue * (metrics.densityDpi / 160f);
    }

    /**
     * 获取屏幕宽度
     */
    private int getWindowsWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 设置轮播页的图片列表集
     *
     * @param ibAddPageView
     * @see 通过实现{@link IBViewSetPageView}接口来完成设置
     */
    public void setOnAddPageView(IBViewSetPageView ibAddPageView) {
        this.mIbAddPageView = ibAddPageView;
    }

    /**
     * 设置轮播页的轮播点(小圆点图片)列表集接口<br>
     * 如果参数设置null，不实现接口则调用系统设置
     *
     * @param ibDotView
     * @see 通过实现{@link IBViewSetDotView}接口来完成设置
     */
    public void setOnAddDotView(IBViewSetDotView ibDotView) {
        this.mIbDotView = ibDotView;
    }

    /**
     * 设置滑动到当前轮播页所需要执行的事件
     *
     * @param ibCurrentPageListener
     * @see 通过实现{@link IBViewCurrentPageListener}接口来完成设置
     */
    public void setOnCurrentPageListener(
            IBViewCurrentPageListener ibCurrentPageListener) {
        this.mIbCurrentPageListener = ibCurrentPageListener;
    }

    /**
     * 轮播页的图片列表集接口
     */
    public interface IBViewSetPageView {
        /**
         * 用户自定义设置轮播页的图片列表集<br>
         * 目前支持 Object 的实例包含 View、Fragment
         *
         * @return
         */
        public List<Object> setPageView();
    }

    /**
     * 轮播页的轮播点(小圆点图片)列表集接口
     */
    public interface IBViewSetDotView {
        /**
         * 设置轮播页的轮播点(小圆点图片)列表集
         *
         * @param imageView 小圆点对象
         * @param positon   for循环遍历的下标值
         * @param selected  当前页的小圆点
         */
        public void setDotView(ImageView imageView, int positon, int selected);
    }

    /**
     * 监听当前轮播页的接口
     */
    public interface IBViewCurrentPageListener {
        /**
         * 设置轮播页当前页所需要执行的事件方法
         *
         * @param view
         *            当前页视图
         * @param positioncur
         *            当前下页下标
         */
        // public void setCurrentPageEvent(View view, int positioncur);

        /**
         * 设置自定义布局轮播页当前页所需要执行的事件方法
         *
         * @param fragment
         *            当前页视图
         * @param positioncur
         *            当前下页下标
         */
        // public void setCurrentPageEvent(Fragment fragment, int positioncur);

        /**
         * 当前页所需要执行的事件方法<br>
         * 1. obj 为 View 实例时，属于轮播图<br>
         * 2. obj 为 Fragment 实例时，属于自定义布局轮播<br>
         * 注：出现界面空白，注意查看打印System.err的日志
         *
         * @param obj         当前页视图
         * @param positioncur 当前下页下标
         */
        public void setCurrentPageEvent(Object obj, int positioncur);

    }

    /**
     * 自定义异常处理类 重新printStackTrace()方法，Toast显示错误消息
     *
     * @author 鱼鱼科技
     */
    public class ServiceException extends Exception {
        /**
         * 序列化时保持版本的兼容性,即在版本升级时反序列化仍保持对象的唯一性
         */
        private static final long serialVersionUID = -5871928645674045209L;
        String msg = null;

        public ServiceException(String msg) {
            this.msg = msg;
        }

        @Override
        public void printStackTrace() {
            super.printStackTrace();
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
