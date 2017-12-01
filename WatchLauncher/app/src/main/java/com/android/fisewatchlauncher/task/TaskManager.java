package com.android.fisewatchlauncher.task;


import com.android.fisewatchlauncher.net.subscriber.TaskSubscriber;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/9
 * @time 11:38
 */
public class TaskManager {
    private TaskManager() {
    }

    private static class SingletonHolder {
        private static final TaskManager INSTANCE = new TaskManager();
    }

    public static TaskManager instance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 减轻主线程压力
     * @param task
     * @param subscriber
     */
    public void exeTask(final CommonTask task, TaskSubscriber subscriber) {
        Observable.just(task)
                .map(new Func1<CommonTask, CommonTask>() {
                    @Override
                    public CommonTask call(CommonTask commonTask) {
                        task.exeTask();
                        return commonTask;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
