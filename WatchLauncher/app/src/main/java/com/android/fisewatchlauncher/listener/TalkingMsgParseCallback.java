package com.android.fisewatchlauncher.listener;

/**
 * @author mare
 * @Description:TODO 语音对讲消息解析结果回调
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/24
 * @time 19:40
 */
public interface TalkingMsgParseCallback {

    /**
     * TODO 语音对讲消息解析结果回调
     * @param isSuccess 1—成功 0- 失败
     * @param audioPath 保存的音频路径
     */
    public void onParseResult(boolean isSuccess,String audioPath);
}
