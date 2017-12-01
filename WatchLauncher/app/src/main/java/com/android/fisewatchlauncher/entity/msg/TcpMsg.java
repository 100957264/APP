package com.android.fisewatchlauncher.entity.msg;

import com.android.fisewatchlauncher.client.msg.MsgInOut;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class TcpMsg {

    protected static final AtomicInteger IDAtomic = new AtomicInteger();
    protected byte[] sourceDataBytes;//数据源
    protected String sourceDataString;//数据源
    protected int id;
    protected long time;//发送、接受消息的时间戳
    protected MsgInOut msgInOut = MsgInOut.Send;
    protected byte[][] endDecodeData;
    private boolean isTextMsg = true;//默认是文本信息

    public SendCallBack sendCallBack;

    private long msgId;

    public interface SendCallBack {
        public void onSuccessSend(TcpMsg msg);

        public void onErrorSend(TcpMsg msg);

    }

    //[厂商*设备ID*内容长度*内容]


    public TcpMsg() {
    }

    public TcpMsg(int id) {
        this.id = id;
    }

    public TcpMsg(byte[] data, MsgInOut inOut) {
        this.sourceDataBytes = data;
        this.msgInOut = inOut;
        init();
    }

    public TcpMsg(String data, MsgInOut inOut) {
        this.sourceDataString = format(data);
        this.msgInOut = inOut;
        init();
    }

    public void setTime() {
        time = System.currentTimeMillis();//接收发送短信的时间
    }

    private void init() {
        id = IDAtomic.getAndIncrement();
    }

    public long getTime() {
        return time;
    }

    public byte[][] getEndDecodeData() {
        return endDecodeData;
    }

    public void setEndDecodeData(byte[][] endDecodeData) {
        this.endDecodeData = endDecodeData;
    }

    public MsgInOut getMsgInOut() {
        return msgInOut;
    }

    public void setMsgInOut(MsgInOut msgInOut) {
        this.msgInOut = msgInOut;
    }

    @Override
    public int hashCode() {
        return id;
    }


    public byte[] getSourceDataBytes() {
        return sourceDataBytes;
    }

    public void setSourceDataBytes(byte[] sourceDataBytes) {
        this.sourceDataBytes = sourceDataBytes;
    }

    public String getSourceDataString() {
        return sourceDataString;
    }

    public void setSourceDataString(String sourceDataString) {
        this.sourceDataString = format(sourceDataString);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static AtomicInteger getIDAtomic() {
        return IDAtomic;
    }

    public String format(String source) {
        return source;
    }

    public SendCallBack getSendCallBack() {
        return sendCallBack;
    }

    public void setSendCallBack(SendCallBack sendCallBack) {
        this.sendCallBack = sendCallBack;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public boolean isTextMsg() {
        return isTextMsg;
    }

    public void setTextMsg(boolean textMsg) {
        isTextMsg = textMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcpMsg tcpMsg = (TcpMsg) o;
        return id == tcpMsg.id;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (endDecodeData != null) {
            for (byte[] bs : endDecodeData) {
                sb.append(Arrays.toString(bs));
            }
        }
        return "TcpMsg{" +
                ", sourceDataString=" + sourceDataString +
                ", id=" + id +
                ", time=" + time +
                ", msgInOut=" + msgInOut +
                ", isTextMsg=" + isTextMsg +
                ", msgId=" + msgId +
                ", endDecodeData=" + sb.toString() +
                "sourceDataBytes=" + Arrays.toString(sourceDataBytes) +
                '}';
    }
}
