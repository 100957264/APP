package com.android.fisewatchlauncher.acty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.TcpClient;
import com.android.fisewatchlauncher.client.TcpConnConfig;
import com.android.fisewatchlauncher.client.helper.stickpackage.AbsStickPackageHelper;
import com.android.fisewatchlauncher.client.msg.MsgInOut;
import com.android.fisewatchlauncher.entity.TargetInfo;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.event.IpUpdateEvent;
import com.android.fisewatchlauncher.event.PhotoSelectedEvent;
import com.android.fisewatchlauncher.event.PhotoTakeEvent;
import com.android.fisewatchlauncher.listener.OnTagItemClickListener;
import com.android.fisewatchlauncher.listener.TcpClientListener;
import com.android.fisewatchlauncher.net.subscriber.IPSubscribe;
import com.android.fisewatchlauncher.net.subscriber.TaskSubscriber;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.task.CommonTask;
import com.android.fisewatchlauncher.task.TaskBean;
import com.android.fisewatchlauncher.task.TaskManager;
import com.android.fisewatchlauncher.utils.ImageUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.NetworkUtil;
import com.android.fisewatchlauncher.utils.SnackbarUtils;
import com.android.fisewatchlauncher.utils.StringValidationUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;
import com.android.fisewatchlauncher.widget.ConsoleLayout;
import com.android.fisewatchlauncher.widget.StaticPackageLayout;
import com.android.fisewatchlauncher.widget.TagService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TcpclientActivity extends BaseActivity implements View.OnClickListener, TcpClientListener, IPSubscribe.IPCallBack, TaskBean.TaskCallback, OnTagItemClickListener {

    private TextView headerTxt, headContentTxt;
    private Button tcpclientBuConnect;
    private TextInputLayout contentTxt;
    private EditText tcpclientEditIp;
    private Button tcpclientBuSend;
    private StaticPackageLayout tcpclientStaticpackagelayout;
    private ConsoleLayout tcpclientConsole;
    private SwitchCompat tcpclientSwitchReconnect,debugModeSwitch;
    private TcpClient xTcpClient;
    TagService tagService;
    NestedScrollView scrolllView;
    View tagRoot;

    List<String> imagePaths = new ArrayList<>();

    public static void startLocationActivity(Context ctx) {
        Intent intent = new Intent(ctx, TcpclientActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        TaskManager.instance().exeTask(new CommonTask() {
            @Override
            public void exeTask() {
                super.exeTask();
                findId();
            }
        }, new TaskSubscriber(this, this));
        EventBus.getDefault().register(this);

    }

    private void findId() {
        scrolllView = (NestedScrollView) findViewById(R.id.root_scroll_view);
        tcpclientBuConnect = (Button) findViewById(R.id.tcpclient_bu_connect);
        headContentTxt = (TextView) findViewById(R.id.head_content_text);
        headerTxt = (TextView) findViewById(R.id.head_text);
        contentTxt = (TextInputLayout) findViewById(R.id.content_text);
        tcpclientBuSend = (Button) findViewById(R.id.tcpclient_bu_send);
        tcpclientStaticpackagelayout = (StaticPackageLayout) findViewById(R.id.tcpclient_staticpackagelayout);
        tcpclientEditIp = (EditText) findViewById(R.id.tcpclient_edit_ip);
        tcpclientConsole = (ConsoleLayout) findViewById(R.id.tcpclient_console);
        tcpclientSwitchReconnect = (SwitchCompat) findViewById(R.id.tcpclient_switch_reconnect);
        debugModeSwitch = (SwitchCompat) findViewById(R.id.tcpclient_switch_debugmode);

        tagRoot = findViewById(R.id.tagview);
        tcpclientBuConnect.setOnClickListener(this);
        tcpclientBuSend.setOnClickListener(this);
        EditText et_content = contentTxt.getEditText();
        headerTxt.setMovementMethod(ScrollingMovementMethod.getInstance());
        headContentTxt.setMovementMethod(ScrollingMovementMethod.getInstance());
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isDebugMode = debugModeSwitch.isChecked();
        if (TextUtils.isEmpty(GlobalSettings.IP) || isDebugMode ) {
            NetworkUtil.domain2IP(GlobalSettings.TEST_DOMAIN_NAME, new IPSubscribe<String>(this, this));
            GlobalSettings.instance().setPort(GlobalSettings.PORT);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tcpclient_bu_connect) {
            tcpclientConsole.clearConsole();
            if (xTcpClient != null && xTcpClient.isConnected()) {
                xTcpClient.disconnect();
            } else {
                AbsStickPackageHelper stickHelper = tcpclientStaticpackagelayout.getStickPackageHelper();
                if (stickHelper == null) {
                    addMsg("粘包参数设置错误");
                    return;
                }
                if (!NetworkUtil.isNetworkAvailable(this)) {
                    addMsg("网络异常");
                    return;
                }

                String temp = tcpclientEditIp.getText().toString().trim();
                String[] temp2 = temp.split(":");
                if (temp2.length == 2 && StringValidationUtils.validateRegex(temp2[0], StringValidationUtils.RegexIP)
                        && StringValidationUtils.validateRegex(temp2[1], StringValidationUtils.RegexPort)) {
                    TargetInfo targetInfo = new TargetInfo(temp2[0], Integer.parseInt(temp2[1]));
                    GlobalSettings.instance().setConfig(new TcpConnConfig.Builder()
                            .setStickPackageHelper(stickHelper)//粘包
                            .setIsReconnect(tcpclientSwitchReconnect.isChecked())
                            .create());
                    xTcpClient = TcpClient.instance();
                    xTcpClient.addTcpClientListener(this);
                    if (xTcpClient.isDisconnected()) {
                        xTcpClient.connect();
                    } else {
                        addMsg("已经存在该连接");
                    }
                } else {
                    addMsg("服务器地址必须是 ip:port 形式");
                }
            }
        } else {//send msg
            sendMsg();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (xTcpClient != null) {
            xTcpClient.removeTcpClientListener(this);
//            xTcpClient.disconnect();//activity销毁时断开tcp连接
        }
    }

    private void addMsg(String msg) {
        this.tcpclientConsole.addLog(msg);
    }

    @Override
    public void onConnected(TcpClient client) {
        addMsg(client.getTargetInfo().getIp() + "连接成功");
    }

    @Override
    public void onSended(TcpClient client, TcpMsg tcpMsg) {
        addMsg("我:" + tcpMsg.getSourceDataString());
    }

    @Override
    public void onDisconnected(TcpClient client, String msg, Exception e) {
        addMsg(client.getTargetInfo().getIp() + "断开连接 " + msg + e);
        boolean isNetAvailable = NetworkUtil.isNetworkAvailable(KApplication.sContext);
        SnackbarUtils.with(scrolllView).setMessage("网络异常").showError();
        if (!isNetAvailable) {
            SnackbarUtils.with(scrolllView).setMessage(getResources().getString(R.string.network_disavilable_text)).showError();
        }
    }

    @Override
    public void onReceive(TcpClient client, TcpMsg msg) {
//        byte[][] res = msg.getEndDecodeData();
//        byte[] bytes = new byte[0];
//        for (byte[] i : res) {
//            bytes = i;
//            break;
//        }
//        addMsg(client.getTargetInfo().getIp() + ":" + " len= " + bytes.length + ", "
//                + msg.getSourceDataString() + " bytes=" + Arrays.toString(bytes));
        addMsg("服务器: " + client.getTargetInfo().getIp() + ":" + client.getTargetInfo().getPort() + ", " + msg.getSourceDataString());
        String[] headerAndContent = MsgParser.instance().getHeaderAndContent(msg.getSourceDataString());
        LogUtils.e("ssss" + Arrays.toString(headerAndContent));
    }

    @Override
    public void onValidationFail(TcpClient client, TcpMsg tcpMsg) {

    }

    @Override
    public void onItemClick(String tag, int pos) {
        String header = MsgParser.instance().getHeaderByType(tag,0);
        String content = "";
        updateText(header, content);
    }

    @Override
    public void getIP(String ip) {
        if (TextUtils.isEmpty(ip)) return;
        tcpclientEditIp.setText(ip + ":" + GlobalSettings.PORT);
        GlobalSettings.instance().setIP(ip);
        tcpclientBuConnect.performClick();
    }

    @Override
    public void doTaskBack() {
        tagService = new TagService(this, tagRoot, this);
    }

    private void updateText() {
        String content = contentTxt.getEditText().getText().toString().trim();
        String header = headerTxt.getText().toString().trim();
        updateText(header, content);
    }

    private void updateText(String header, String content) {

        if (!TextUtils.isEmpty(header)) {
            headerTxt.setText(header);
        } else {
            return;
        }

        if (!TextUtils.isEmpty(content)) {
            contentTxt.getEditText().setText(content);
        }

        String msg = MsgParser.instance().composedHeaderContent(header, content);
        headContentTxt.setText(msg);
    }

    private void sendMsg() {
        String content = contentTxt.getEditText().getText().toString().trim();
        String header = headerTxt.getText().toString().trim();
        if (TextUtils.isEmpty(header)) {
            ToastUtils.showShort("头部不能为空~~");
            return;
        }
        String msg = headContentTxt.getText().toString().trim();
        if (xTcpClient != null) {
            TcpMsg tcpMsg = new TcpMsg(msg, MsgInOut.Send);
            tcpMsg.setSendCallBack(new TcpMsg.SendCallBack() {
                @Override
                public void onSuccessSend(TcpMsg msg) {

                }

                @Override
                public void onErrorSend(TcpMsg msg) {

                }
            });
            xTcpClient.sendMsg(msg);
        } else {
            addMsg("还没有连接到服务器");
        }
    }

    @Subscribe
    public void onEventMainThread(PhotoTakeEvent event) {
        Bitmap bmp = event.photoBmp;
        if (null == bmp) return;
        bmp = ImageUtils.scale(bmp, 150, 150);
    }

    @Subscribe
    public void onEventMainThread(PhotoSelectedEvent event) {

        Bitmap bmp = event.photoBmp;
        if (null == bmp) return;
        bmp = ImageUtils.scale(bmp, 150, 150);
    }

    @Subscribe
    public void onEventMainThread(IpUpdateEvent event) {
        LogUtils.e("IpUpdateEvent ip "  + event.ip);
        tcpclientEditIp.setText(event.ip + ":" + GlobalSettings.instance().getPort());
    }

}

