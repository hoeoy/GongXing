package com.houoy.www.gongxing;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by andyzhao on 3/6/2018.
 */

public class ActivityPool {
    //    private List<MyAppCompatActivity> activitieList = new ArrayList();
    private Stack<Activity> activityStack = new Stack();

    private static ActivityPool pool = null;

    protected ActivityPool() {
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public static ActivityPool getInstant() {
        if (pool == null) {
            pool = new ActivityPool();
        }
        return pool;
    }

    public void push(Activity appCompatActivity) {
        activityStack.push(appCompatActivity);
    }

    public Activity pop() {
        return activityStack.pop();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        Activity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing() && !activity.isDestroyed()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
//        for (Activity activity : activityStack) {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

//    /**
//     * 退出应用程序
//     */
//    public void AppExit(Context context) {
//        try {
//            finishAllActivity();
//            ActivityManager manager = (ActivityManager) context
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            manager.killBackgroundProcesses(context.getPackageName());
//            System.exit(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
