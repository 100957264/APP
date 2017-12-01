package com.android.fisewatchlauncher.function.sms;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/12
 * @time 20:47
 */
/**
 * 为了减少用户的操作步骤，在获得短信验证码的时候，我们可以监听特殊手机号码的短信，
 * 截取信息当中的短信验证码（其实有很多应用都监听短信例如360短信，一些信用卡或者是记账类的应用）。
 *
 * 原理：可以使用一个自定义的BroadcastReceiver来监听短信，在监听结果当中过滤手机号，
 * 在需要回填的activity当中实现实例化广播并且实现其回调接口，在接口当中进行回填验证码，
 * 在销毁activity时销毁链接。但是这样操作会出现一些问题，
 * 由于一些其他的应用也会使用广播监听手机例如QQ通讯录或者是360通讯录等有的时候会被其拦截，
 * 即使修改优先级也会出现不能进行回填的问题。所有这里可以采用另外一种的解决方法：
 * 使用ContentProvider来监听短信数据库的变化，
 * 在自定义的ContentObserver当中实现onChange的方法进行监听特定手机号的短信，
 * 然后进行信息截取在填充到需要填充的位置。
 *
 * “ContentObserver，内容观察者，目的是观察(捕捉)特定Uri引起的数据库的变化，继而做一些相应的处理，
 * 它类似于数据库技术中的触发器(Trigger)，当ContentObserver所观察的Uri发生变化时，便会触发它。
 * 触发器分为表触发器、行触发器，相应地ContentObserver也分为“表“ContentObserver、“行”ContentObserver，
 * 当然这是与它所监听的Uri MIME Type有关的。”
 *
 * 摘自：自动填充短信验证码（使用ContentObserver）：http://www.tuicool.com/articles/bMVRru
 */
import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：liuyaowei ;日期：2016-07-16.
 * QQ:1054185214
 * 类作用：监听短信数据库, 获取短信 验证码
 */

public class SmsContentObserver extends ContentObserver {
    private Activity activity;
    private String address;
    private TextView text;

    public SmsContentObserver(Handler handler, Activity activity, String number, TextView text) {
        super(handler);
        this.activity=activity;
        this.address=number;
        this.text=text;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);


        // 读取收件箱中指定号码的短信
//        content://sms/inbox 收件箱
//        content://sms/sent 已发送
//        content://sms/draft 草稿
//        content://sms/outbox 发件箱
//        content://sms/failed 发送失败
//        content://sms/queued 待发送列表
        Cursor cursor= activity.managedQuery(Uri.parse("content://sms/inbox"),
                new String[]{"_id","address","body","read"},
                " address=? and read=?",
                new String[] { address, "0" }, "_id desc");

        // 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
        if (cursor!=null&&cursor.getCount()>0){
            ContentValues values=new ContentValues();
            if (Build.VERSION.SDK_INT < 21){
                values.put("type",1);//修改短信为已读短信   5.0后以不能修改
            }
            cursor.moveToNext();
            int smsbodyColumn=cursor.getColumnIndex("body");
            String smsbody=cursor.getString(smsbodyColumn);

            text.setText(getDynamicPassword(smsbody)); //调用下面的截取短信中六位数字验证码的方法
        }

        // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }

    /**
     * 从字符串中截取连续6位数字组合 ([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
     *
     * @param st 短信内容
     * @return 截取得到的6位动态密码
     */
    public String getDynamicPassword(String st){
        //  6是验证码的位数一般为六位   如果验证码的位数变化只要将6修改为想要的位数，
        // 过验证如果不止为数字，直接修改正则为想要的内容即可

        //Pattern是java.util.regex（一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包）中的一个类。
        // 一个Pattern是一个正则表达式经编译后的表现模式
        Pattern pattern=Pattern.compile("(?<![0-9])([0-9]{" + 6 + "})(?![0-9])") ;
        Matcher matcher=pattern.matcher(st);
        String dynamicPassword=null;
        while (matcher.find()){
            System.out.println(matcher.group());
            dynamicPassword=matcher.group();
        }
        return dynamicPassword;
    }
}