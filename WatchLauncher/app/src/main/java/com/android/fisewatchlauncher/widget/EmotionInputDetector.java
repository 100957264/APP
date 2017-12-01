package com.android.fisewatchlauncher.widget;

import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.entity.dao.WetMessage;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.event.TalkingSendStatusEvent;
import com.android.fisewatchlauncher.function.talking.TalkingUtils;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.utils.AudioRecoderUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.PopupWindowFactory;
import com.android.fisewatchlauncher.utils.TimeUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：Rance on 2016/12/13 15:19
 * 邮箱：rance935@163.com
 * 输入框管理类
 */
public class EmotionInputDetector {

    private Activity mActivity;
    private TextView mVoiceText;
    private AudioRecoderUtils mAudioRecoderUtils;
    private PopupWindowFactory mVoicePop;
    private TextView mPopVoiceText;
    private long motionEventDownTimeMillis;

    private EmotionInputDetector() {
    }

    public static EmotionInputDetector with(Activity activity) {
        EmotionInputDetector emotionInputDetector = new EmotionInputDetector();
        emotionInputDetector.mActivity = activity;
        return emotionInputDetector;
    }

    public EmotionInputDetector bindToVoiceText(TextView voiceText , ViewGroup container) {
        mVoiceText = voiceText;
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 获得x轴坐标
                int x = (int) event.getX();
                // 获得y轴坐标
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        motionEventDownTimeMillis = System.currentTimeMillis();
                        mVoicePop.showAtLocation(v, Gravity.CENTER, 0, 0);
                        mVoiceText.setText("松开结束");
                        mPopVoiceText.setText("手指上滑，取消发送");
                        mVoiceText.setTag("1");
                        mAudioRecoderUtils.startRecord(mActivity);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (wantToCancle(x, y)) {
                            mVoiceText.setText("松开结束");
                            mPopVoiceText.setText("松开手指，取消发送");
                            mVoiceText.setTag("2");
                        } else {
                            mVoiceText.setText("松开结束");
                            mPopVoiceText.setText("手指上滑，取消发送");
                            mVoiceText.setTag("1");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - motionEventDownTimeMillis < 1000 &&
                                !wantToCancle(x, y)) {
                            ToastUtils.showShort("录音时间太短");
                            mVoiceText.setTag("2");
                        }
                        mVoicePop.dismiss();
                        if (mVoiceText.getTag().equals("2")) {
                            //取消录音（删除录音文件）
                            mAudioRecoderUtils.cancelRecord();
                        } else {
                            //结束录音（保存录音文件）
                            mAudioRecoderUtils.stopRecord();
                        }
                        mVoiceText.setText("按住说话");
                        mVoiceText.setTag("3");
                        //mVoiceText.setVisibility(View.GONE);
                        //mEditText.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return this;
    }

    private boolean wantToCancle(int x, int y) {
        // 超过按钮的宽度
        if (x < 0 || x > mVoiceText.getWidth()) {
            return true;
        }
        // 超过按钮的高度
        if (y < -50 || y > mVoiceText.getHeight() + 50) {
            return true;
        }
        return false;
    }

    public EmotionInputDetector build() {
        mAudioRecoderUtils = new AudioRecoderUtils();

        View view = View.inflate(mActivity, R.layout.layout_microphone, null);
        mVoicePop = new PopupWindowFactory(mActivity, view);

        //PopupWindow布局文件里面的控件
        final ImageView mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        final TextView mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mPopVoiceText = (TextView) view.findViewById(R.id.tv_recording_text);
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(long time, final String filePath) {
                mTextView.setText(TimeUtils.long2String(0));
                LogUtils.d("talking filePath " +filePath);
               final WetMessage messageChatInfo = new WetMessage();
                messageChatInfo.setConverstationType(WetMessage.ConverstationType.SINGLE);
                messageChatInfo.setMessageDirection(WetMessage.MessageDirection.SEND);
                messageChatInfo.setSendStatus(WetMessage.SendStatus.SENDING);
                messageChatInfo.setMessageContentType(WetMessage.MessageContentType.AUDIO);
                messageChatInfo.setTimestamp(System.currentTimeMillis());
                messageChatInfo.setId(System.currentTimeMillis());//设置id
                messageChatInfo.setAudioPath(filePath);
                messageChatInfo.setVoiceTime(time);
                EventBus.getDefault().post(messageChatInfo);
                TalkingUtils.uploadTalkingData2Server(messageChatInfo, new TcpMsg.SendCallBack() {
                    @Override
                    public void onSuccessSend(TcpMsg msg) {
                        StaticManager.instance().rmCachedTKSendingById(msg.getId());
                        EventBus.getDefault().post(new TalkingSendStatusEvent(filePath, WetMessage.SendStatus.SUCCESS));
                    }

                    @Override
                    public void onErrorSend(TcpMsg msg) {
                        StaticManager.instance().cacheTKSendingData(msg);//加入草稿箱
                        EventBus.getDefault().post(new TalkingSendStatusEvent(filePath, WetMessage.SendStatus.FAILURE));
                    }
                });
            }

            @Override
            public void onError() {
                mVoiceText.setVisibility(View.GONE);
            }
        });
        return this;
    }

    public boolean interceptBackPress() {
        return false;
    }
}
