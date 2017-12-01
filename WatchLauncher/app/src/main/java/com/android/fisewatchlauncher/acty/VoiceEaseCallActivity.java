/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.fisewatchlauncher.acty;

import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.entity.dao.EaseFriend;
import com.android.fisewatchlauncher.function.wetchat.EaseMobManager;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.UUID;

/**
 * 语音通话页面
 * 
 */
public class VoiceEaseCallActivity extends EaseCallActivity implements OnClickListener {
	private ImageButton hangupBtn;
	private ImageButton      refuseBtn;
	private ImageButton      answerBtn;

	private boolean isMuteState;
	private boolean isHandsfreeState;
	
	private boolean endCallTriggerByMe = false;
	private Chronometer chronometer;
	String st1;
    private boolean monitor = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
        	finish();
        	return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.em_activity_voice_call);

        EaseMobManager.instance().isVoiceCalling = true;
		callType = 0;

		refuseBtn = (ImageButton) findViewById(R.id.btn_refuse_call);
		answerBtn = (ImageButton) findViewById(R.id.btn_answer_call);
		hangupBtn = (ImageButton) findViewById(R.id.btn_hangup_call);
        TextView nickTextView = (TextView) findViewById(R.id.tv_nick);
//        TextView durationTextView = (TextView) findViewById(R.id.tv_calling_duration);
		chronometer = (Chronometer) findViewById(R.id.chronometer);

		refuseBtn.setOnClickListener(this);
		answerBtn.setOnClickListener(this);
		hangupBtn.setOnClickListener(this);

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		addCallStateListener();
		msgid = UUID.randomUUID().toString();
        EaseFriend easeFriend = getIntent().getParcelableExtra(EaseContactListActivity.EXTRA_KEY_EASE_FRIEND);
        username = easeFriend.getFriendId();
		isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
		nickTextView.setText(easeFriend.getNick());
		if (!isInComingCall) {// outgoing call
			soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
			outgoing = soundPool.load(this, R.raw.em_outgoing, 1);

			hangupBtn.setVisibility(View.VISIBLE);
			answerBtn.setVisibility(View.GONE);
			refuseBtn.setVisibility(View.GONE);
			st1 = getResources().getString(R.string.Are_connected_to_each_other);
			handler.sendEmptyMessage(MSG_CALL_MAKE_VOICE);
            handler.postDelayed(new Runnable() {
                public void run() {
                    streamID = playMakeCallSounds();
                }
            }, 300);
        } else { // incoming call
			Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(true);
			ringtone = RingtoneManager.getRingtone(this, ringUri);
			ringtone.play();
		}
        final int MAKE_CALL_TIMEOUT = 50 * 1000;
        handler.removeCallbacks(timeoutHangup);
        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);
	}

	/**
	 * set call state listener
	 */
	void addCallStateListener() {
	    callStateListener = new EMCallStateChangeListener() {
            
            @Override
            public void onCallStateChanged(CallState callState, final CallError error) {
                // Message msg = handler.obtainMessage();
                EMLog.d("EMCallManager", "onCallStateChanged:" + callState);
                switch (callState) {
                
                case CONNECTING:
                    break;
                case CONNECTED:
                    break;

                case ACCEPTED:
                    handler.removeCallbacks(timeoutHangup);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (soundPool != null)
                                    soundPool.stop(streamID);
                            } catch (Exception e) {
                            }
                            if(!isHandsfreeState)
                                closeSpeakerOn();
                            //show relay or direct call, for testing purpose
                            ((TextView)findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                    ? R.string.direct_call : R.string.relay_call);
                            chronometer.setVisibility(View.VISIBLE);
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            // duration start
                            chronometer.start();
                            String str4 = getResources().getString(R.string.In_the_call);
                            callingState = CallingState.NORMAL;
                            startMonitor();
                        }
                    });
                    break;
                case NETWORK_UNSTABLE:
                    break;
                case NETWORK_NORMAL:
                    break;
                case VOICE_PAUSE:
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "VOICE_PAUSE", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case VOICE_RESUME:
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "VOICE_RESUME", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case DISCONNECTED:
                    handler.removeCallbacks(timeoutHangup);
                    @SuppressWarnings("UnnecessaryLocalVariable") final CallError fError = error;
                    runOnUiThread(new Runnable() {
                        private void postDelayedCloseMsg() {
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("AAA", "CALL DISCONNETED");
                                            removeCallStateListener();
                                            saveCallRecord();
                                            Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                            animation.setDuration(800);
                                            findViewById(R.id.root_layout).startAnimation(animation);
                                            finish();
                                        }
                                    });
                                }
                            }, 200);
                        }

                        @Override
                        public void run() {
                            chronometer.stop();
                            callDruationText = chronometer.getText().toString();
                            String st1 = getResources().getString(R.string.Refused);
                            String st2 = getResources().getString(R.string.The_other_party_refused_to_accept);
                            String st3 = getResources().getString(R.string.Connection_failure);
                            String st4 = getResources().getString(R.string.The_other_party_is_not_online);
                            String st5 = getResources().getString(R.string.The_other_is_on_the_phone_please);
                            
                            String st6 = getResources().getString(R.string.The_other_party_did_not_answer_new);
                            String st7 = getResources().getString(R.string.hang_up);
                            String st8 = getResources().getString(R.string.The_other_is_hang_up);
                            
                            String st9 = getResources().getString(R.string.did_not_answer);
                            String st10 = getResources().getString(R.string.Has_been_cancelled);
                            String st11 = getResources().getString(R.string.hang_up);
                            
                            if (fError == CallError.REJECTED) {
                                callingState = CallingState.BEREFUSED;
                            } else if (fError == CallError.ERROR_UNAVAILABLE) {
                                callingState = CallingState.OFFLINE;
                            } else if (fError == CallError.ERROR_BUSY) {
                                callingState = CallingState.BUSY;
                            } else if (fError == CallError.ERROR_NORESPONSE) {
                                callingState = CallingState.NO_RESPONSE;
                            } else if (fError == CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED){
                                callingState = CallingState.VERSION_NOT_SAME;
                            } else {
                                if (isRefused) {
                                    callingState = CallingState.REFUSED;
                                }
                                else if (isAnswered) {
                                    callingState = CallingState.NORMAL;
                                } else {
                                    if (isInComingCall) {
                                        callingState = CallingState.UNANSWERED;
                                    } else {
                                        if (callingState != CallingState.NORMAL) {
                                            callingState = CallingState.CANCELLED;
                                        }
                                    }
                                }
                            }
                            postDelayedCloseMsg();
                        }

                    });

                    break;

                default:
                    break;
                }

            }
        };
		EMClient.getInstance().callManager().addCallStateChangeListener(callStateListener);
	}
	
    void removeCallStateListener() {
        EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refuse_call:
		    isRefused = true;
		    refuseBtn.setEnabled(false);
		    handler.sendEmptyMessage(MSG_CALL_REJECT);
			break;

		case R.id.btn_answer_call:
		    answerBtn.setEnabled(false);
		    closeSpeakerOn();
            answerBtn.setVisibility(View.INVISIBLE);
            refuseBtn.setVisibility(View.INVISIBLE);
            hangupBtn.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(MSG_CALL_ANSWER);
            chronometer.start();
			break;

		case R.id.btn_hangup_call:
		    hangupBtn.setEnabled(false);
			chronometer.stop();
			endCallTriggerByMe = true;
            handler.sendEmptyMessage(MSG_CALL_END);
			break;

		case R.id.iv_mute:
			if (isMuteState) {
                try {
                    EMClient.getInstance().callManager().resumeVoiceTransfer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
				isMuteState = false;
			} else {
                try {
                    EMClient.getInstance().callManager().pauseVoiceTransfer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
				isMuteState = true;
			}
			break;
		case R.id.iv_handsfree:
			if (isHandsfreeState) {
				closeSpeakerOn();
				isHandsfreeState = false;
			} else {
				openSpeakerOn();
				isHandsfreeState = true;
			}
			break;
		default:
			break;
		}
	}
	
    @Override
    protected void onDestroy() {
        EaseMobManager.instance().isVoiceCalling = false;
        stopMonitor();
        super.onDestroy();
    }

	@Override
	public void onBackPressed() {
		callDruationText = chronometer.getText().toString();
	}

    /**
     * for debug & testing, you can remove this when release
     */
    void startMonitor(){
        monitor = true;
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ((TextView)findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                ? R.string.direct_call : R.string.relay_call);
                    }
                });
                while(monitor){
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "CallMonitor").start();
    }

    void stopMonitor() {

    }

}
