package com.jz.pdf.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/***
 * 抽取提取出来的类【后面的Fragment只需要继承BaseFragment实现基础的抽象方法,就可以实现加载的过程】
 */
public abstract class BaseFragment extends Fragment {

    // 注意修饰符不能是private
    protected LoadingPage loadingPage;

    // 定义方法
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 因为LoadingPage每一次都要创建.在这里我们只需要判断当前的LoadingPage是否是空值即可
        // 判断当前的不是空值的情况下,进行相关优化的操作
        if (loadingPage == null) {

            // 定义需要返回的视图操作
            loadingPage = new LoadingPage(getActivity()) {

                // 抽取当前的视图
                public View createSuccessView() {
                    // 返回当前的视图
                    return getSuccessView();
                }

                // 抽取当前的数据
                protected Object loadData() {
                    // 返回当前的数据
                    return getLoadData();
                }
            };
        } else {
            // 移除掉当前的子视图操作
            if (loadingPage != null) {
                //得到子视图的父亲
                ViewParent parent = loadingPage.getParent();
                //判断当前的父视图,是否是ViewGroup的类型
                if (parent != null && parent instanceof ViewGroup) {
                    //移除当前的子视图
                    ViewGroup viewGroup = (ViewGroup) parent;
                    //移除掉当前的子视图
                    viewGroup.removeView(loadingPage);
                }
            }
            //CommonUtils.removeSelfFromParent(loadingPage);
            // 注意事项:在Android5.0以后不会在 FragmentManager的外面包裹一层.
            // 这里意味着我们不需要移除当前的视图
        }
        // 返回当前的视图
        return loadingPage;
    }

    /***
     * 定义抽象方法.设置当前的视图操作
     */
    public abstract View getSuccessView();

    /***
     * 定义抽象方法.设置当前加载的数据
     */
    public abstract Object getLoadData();

}
