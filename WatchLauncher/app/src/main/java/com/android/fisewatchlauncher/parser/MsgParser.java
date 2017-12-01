package com.android.fisewatchlauncher.parser;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.TcpClient;
import com.android.fisewatchlauncher.client.msg.MsgInOut;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.utils.ArrayUtils;
import com.android.fisewatchlauncher.utils.DigitalConvert;
import com.android.fisewatchlauncher.utils.FileUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.Md5;
import com.android.fisewatchlauncher.utils.ToastUtils;
import com.android.fisewatchlauncher.utils.TypeConvert;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析拼接头部信息 以及加密要发送的信息
 * Created by fanyang on 2017/8/4.
 */
public class MsgParser {
    //[product*id*len*content]
    private MsgParser() {

    }

    private static class SingletonHolder {
        private static final MsgParser INSTANCE = new MsgParser();
    }

    public static MsgParser instance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 解析出收到的header信息
     *
     * @param msg [bca78c3ac6437502a11d9b4b844950a3FISE*123456789123459*000F*Time, 1502181849]
     * @return product*id*len*content
     */
    public String[] getHeaderAndContent(String msg) {
        String[] segemnts = null;
        String reg = "";
        if (!msg.contains(GlobalSettings.MSG_CONTENT_SEPERATOR)) {
            segemnts = new String[1];
            reg = "\\[(.*?)\\]";
        } else if (msg.contains(GlobalSettings.MSG_CONTENT_SEPERATOR)) {
            segemnts = new String[2];
            reg = "\\[(.*?),";
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(msg);
        while (m.find()) {
            segemnts[0] = m.group(1);
            break;
        }
        if (segemnts.length < 2) {
            return segemnts;
        }
        segemnts[1] = msg.substring(msg.indexOf(',') + 1, msg.length() - 1);
        return segemnts;
    }

    /**
     * 解析出收到的content信息
     *
     * @param msg
     * @return
     */
    public String getNoneHeaderContent(String msg) {
        return msg.substring(msg.indexOf(',') + 1, msg.indexOf(']'));
    }

    /**
     * 将header解析成字符串数组格式
     *
     * @param header product*id*len*content
     * @return String[] [product , id , len , content]数组
     */
    public String[] getRecvHeaderSegments(String header) {
        LogUtils.e("getRecvHeaderSegments " + header);
        if (TextUtils.isEmpty(header)) return null;
        String[] segemnt = TextUtils.split(header, GlobalSettings.MSG_HEADER_SEPERATOR_PARSER);
        printArray(segemnt);
        return segemnt;
    }

    private void printArray(String[] segemnt) {
        int len = segemnt.length;
        for (int i = 0; i < len; i++) {
            LogUtils.e("segment " + segemnt[i]);
        }
    }

    /**
     * 获取完整的未加密的header
     *
     * @param header product*id*len*content
     * @return msgType 消息头部标记位
     */
    public String parseHeaderTypeByHeader(String header) {
        String[] segment = getRecvHeaderSegments(header);
        if (ArrayUtils.isEmpty(segment)) return null;
        LogUtils.e("segment " + Arrays.toString(segment));
        String msgType = segment[segment.length - 1];
        LogUtils.e(msgType);
        return msgType;
    }

    /**
     * 获取完整的未加密的header
     *
     * @param msg [product*id*len*content]
     * @return
     */
    public String getHeaderSegmentsString(String msg) {
        String[] segemnts = getHeaderAndContent(msg)[0].split(GlobalSettings.MSG_HEADER_SEPERATOR);
        String content = segemnts[3];
        byte[] srcBytes = content.getBytes();
        int len = srcBytes.length;
        byte[] byte4 = TypeConvert.int2byte(len);
        segemnts[2] = DigitalConvert.byte2String(byte4);
        segemnts[0] = GlobalSettings.instance().getProduct();
        segemnts[1] = GlobalSettings.instance().getImei();
        String result = array2String(segemnts);
        LogUtils.e(result);
        return result;
    }

    /**
     * 将msgType 转成 product*id*len*content格式
     *
     * @param msgType content
     * @return product*id*len*content
     */
    public String getHeaderByType(String msgType, int byteLen) {
        if (!MsgType.instance().verifyMsgType(msgType)) {//校验msgType
            ToastUtils.showShort("msgType 格式不对");
            LogUtils.e("msgType 格式不对 : " + msgType);
            return null;
        }
        String[] segemnts = new String[4];
        segemnts[3] = msgType;
        if (byteLen == 0) {
            byteLen = getUTF8Len(segemnts[3]);
        }
        segemnts[2] = get4ByteHexString(byteLen).toUpperCase();//16进制字母转成大写
        segemnts[0] = GlobalSettings.instance().getProduct();
        segemnts[1] = GlobalSettings.instance().getImei();

        String result = array2String(segemnts);
        LogUtils.e(result);
        return result;
    }

    /**
     * 将数组转为带*的字符串
     *
     * @param segemnts [product,id,len,content]
     * @return product*id*len*content
     */
    private String array2String(String[] segemnts) {
        int len = segemnts.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(segemnts[i]);
            if (i != len - 1) {
                sb.append(GlobalSettings.MSG_HEADER_SEPERATOR);
            }
        }
        LogUtils.i(sb.toString());
        return sb.toString();
    }

    /**
     * 将product*id*len*content转成[product*id*len*content]
     *
     * @param unComposeHedader
     * @return
     */
    public String composeHeader(String unComposeHedader) {
        StringBuilder sb = new StringBuilder();
        String result = sb.append(GlobalSettings.MSG_PREFIX)
                .append(unComposeHedader)
                .append(GlobalSettings.MSG_SUFFIX).toString();
        LogUtils.i(result);
        return result;
    }

    /**
     * 进行加密算法后的header
     * eg :9fa56f7e41aaf7273f30faff536619e6SG*8800000015*0002*LK
     *
     * @param msgType LK
     * @return 9fa56f7e41aaf7273f30faff536619e6SG*8800000015*0002*LK
     */
    public String encryptHeaderByType(String msgType, int len) {
        String originalHeader = getHeaderByType(msgType, len);//product*id*len*content
        return encryptHeader(originalHeader);
    }

    /**
     * @param originalHeader SG*8800000015*0002*LK
     * @return 9fa56f7e41aaf7273f30faff536619e6SG*8800000015*0002*LK
     */
    @NonNull
    private String encryptHeader(String originalHeader) {
        if (TextUtils.isEmpty(originalHeader)) return null;//avoid NPE
        StringBuilder sb = new StringBuilder(originalHeader);
        sb.append(GlobalSettings.instance().getPrivateKey());//用于签名的数据
        String md5 = "";
        try {
            md5 = Md5.getMD5Lower32(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e(e);
        } finally {
            sb.setLength(0);
            return sb.append(md5).append(originalHeader).toString();
        }
    }

    /**
     * TODO 根据消息类型 和消息字符串 组装完整的消息
     *
     * @param msgType LK
     * @param content [9fa56f7e41aaf7273f30faff536619e6SG*8800000015*0002*LK,content]
     * @return
     */
    public String composedTypeContent(String msgType, String content) {
        int byteLen;
        if (TextUtils.isEmpty(content)) {
            byteLen = getUTF8Len(msgType);
        } else {
            byteLen = getUTF8Len(msgType + GlobalSettings.MSG_CONTENT_SEPERATOR + content);
        }
        String header = encryptHeaderByType(msgType, byteLen);
        if (TextUtils.isEmpty(header)) return null;//avoid NPE
        StringBuffer sb = new StringBuffer(header);
        if (!TextUtils.isEmpty(content)) {
            sb.append(GlobalSettings.MSG_CONTENT_SEPERATOR + content);//没有前后缀的信息
        }
        return composeHeader(sb.toString());
    }

    /**
     * TODO 根据消息类型 和字节流数组 组装完整的消息
     *
     * @param msgType LK
     * @param content [9fa56f7e41aaf7273f30faff536619e6SG*8800000015*0002*LK,[!#amr,1,2,3,4]]
     * @return
     */
    public byte[] composedTypeContentBytes(String msgType, byte[] content) {
        int byteLen;//msgType之后的所有字节数(包含type和逗号)
        int contentBytesLen = getBytesLen(content);
        boolean isContentNull = null == content;
        if (isContentNull) {
            byteLen = getUTF8Len(msgType);
        } else {
            byteLen = getUTF8Len(msgType + GlobalSettings.MSG_CONTENT_SEPERATOR) + contentBytesLen;
        }
        LogUtils.e("消息头,消息体总长度 " + byteLen);
        String header = encryptHeaderByType(msgType, byteLen);
        if (TextUtils.isEmpty(header)) return null;//avoid NPE
        StringBuffer sb = new StringBuffer(header);
        if (!isContentNull) {
            sb.append(GlobalSettings.MSG_CONTENT_SEPERATOR);//没有前后缀的信息
        }
        header = sb.toString();
        LogUtils.e("" + header);
        byte[] headerBytes = getUtf8Bytes(header);
        byte[] preffixBytes = getUtf8Bytes(GlobalSettings.MSG_PREFIX);
        byte[] suffixBytes = getUtf8Bytes(GlobalSettings.MSG_SUFFIX);
        int headerLen = getBytesLen(headerBytes);
        int preffixBytesLen = getBytesLen(preffixBytes);
        int suffixBytesLen = getBytesLen(suffixBytes);
        int totalLen = preffixBytesLen + headerLen + contentBytesLen + suffixBytesLen;
        ByteBuffer buffer = ByteBuffer.allocate(totalLen);// 预留10个空闲位置
        LogUtils.e("拼装消息开始 " + buffer.toString());
        buffer.put(preffixBytes);//添加'['字节数组
        LogUtils.e("组装前缀 " + buffer.toString());
        buffer.put(headerBytes);//添加消息头部(包含第一个逗号)
        LogUtils.e("组装头部 " + buffer.toString());
        if (!isContentNull){
            buffer.put(content);//添加消息体
        }
        LogUtils.e("组装前缀 " + buffer.toString());
        buffer.put(suffixBytes);//添加']'
        LogUtils.e("组装后缀 " + buffer.toString());
        byte[] completeBytes = new byte[totalLen];
        buffer.flip();//pos =0;
        buffer.get(completeBytes);
        LogUtils.i("拼装消息完成 " + Arrays.toString(completeBytes));
        return completeBytes;
    }

    /**
     * TODO 实际传输的完整数据
     *
     * @param header  未加密的header  product*id*len*content
     * @param content [9fa56f7e41aaf7273f30faff536619e6SG*8800000015*0002*LK,content]
     * @return
     */
    public String composedHeaderContent(String header, String content) {
        String encryptedHeader = encryptHeader(header);
        if (TextUtils.isEmpty(encryptedHeader)) return null;//avoid NPE
        StringBuffer sb = new StringBuffer(encryptedHeader);
        if (!TextUtils.isEmpty(content)) {
            sb.append(GlobalSettings.MSG_CONTENT_SEPERATOR + content);//没有前后缀的信息
        }
        return composeHeader(sb.toString());
    }

    /**
     * TODO 根据类型 发送字符消息
     *
     * @param msgType
     */
    public void sendMsgByType(String msgType) {
        sendMsgByType(msgType, null);
    }

    /**
     * TODO 根据类型内容 发送字符消息
     *
     * @param msgType
     * @param content
     */
    public void sendMsgByType(String msgType, String content) {
        sendMsgByType(msgType, content, null);
    }

    /**
     * TODO 根据类型内容 发送字节流数据
     *
     * @param msgType
     */
    public void sendMsgBytesByType(String msgType) {
        sendMsgBytesByType(msgType, null);
    }

    /**
     * TODO 根据类型和内容发送字节流数据
     *
     * @param msgType
     * @param content
     * @see #sendMsgBytesByType
     */
    public void sendMsgBytesByType(String msgType, byte[] content) {
        sendMsgBytesByType(msgType, content, null);
    }

    /**
     * TODO 发送消息内容 (字符 + 字节)格式数据
     *
     * @param msgType
     * @param msgPart
     * @param content
     */
    public void sendMsgStringBytesContent(String msgType, String msgPart, byte[] content) {
        sendMsgStringBytesContent(msgType, msgPart, content, null);
    }

    /**
     * TODO 发送消息内容 (字符 + 字节)格式数据
     *
     * @param msgType
     * @param msgPart
     * @param content
     * @param sendCallBack
     * @see #sendMsgStringBytesContent
     */
    public void sendMsgStringBytesContent(String msgType, String msgPart, byte[] content, TcpMsg.SendCallBack sendCallBack) {
        byte[] msgPartBytes = getUtf8Bytes(msgPart);
        LogUtils.e("msgPartBytes: " + Arrays.toString(msgPartBytes));//5,1510112201270,
        LogUtils.e("content: " + Arrays.toString(msgPartBytes));// [-1, -40, -1, -32, 0, 16,....
        int partLen = getBytesLen(msgPartBytes);
        int totalLen = partLen + getBytesLen(content);
        ByteBuffer buffer = ByteBuffer.allocate(totalLen);
        if (null != msgPartBytes) {
            buffer.put(msgPartBytes);
        }
        if (null != content) {
            buffer.put(content);
        }
        buffer.flip();
        byte[] completeContent = new byte[totalLen];
        buffer.get(completeContent);
        LogUtils.e("msgType: " + msgType + "\t msgPart:" + msgPart + " \t content: " + Arrays.toString(content));
        LogUtils.e("completeContent: " + Arrays.toString(completeContent));
        String imgCompleteUploadPath = FileConstant.PHOTO_CAPTURE_OUTPUT_PATH + "imgCompleteUploadPath" + ".rd";
        FileUtils.writeFile(imgCompleteUploadPath, Arrays.toString(completeContent));//写入完整的上传信息(参数+图片)到文件
        sendMsgBytesByType(msgType, completeContent, sendCallBack);
    }

    /**
     * TODO 根据类型内容回调 发送字节流数据消息
     *
     * @param msgType
     * @param content
     * @param sendCallBack 消息成功与否回调
     * @see #sendMsgBytesByType
     */
    public void sendMsgBytesByType(String msgType, byte[] content, TcpMsg.SendCallBack sendCallBack) {
        byte[] msgBytes = composedTypeContentBytes(msgType, content);
        TcpMsg msg = new TcpMsg(msgBytes, MsgInOut.Send);
        msg.setTextMsg(false);
        msg.setSendCallBack(sendCallBack);
        TcpClient.instance().sendMsg(msg);
    }

    /**
     * TODO 根据类型内容回调 发送字符消息
     *
     * @param msgType
     * @param content
     * @param sendCallBack
     */
    public void sendMsgByType(String msgType, String content, TcpMsg.SendCallBack sendCallBack) {
        String meesage = composedTypeContent(msgType, content);
        sendCompletedMsg(meesage, sendCallBack);
    }

    public void sendMsgByHeader(String header) {
        sendMsgByHeader(header, null);
    }

    private void sendMsgByHeader(String header, String content) {

        sendMsgByHeader(header, content, null);
    }

    public void sendMsgByHeader(String header, String content, TcpMsg.SendCallBack callBack) {
        String meesage = composedHeaderContent(header, content);
        sendCompletedMsg(meesage, callBack);
    }

    private void sendCompletedMsg(String message, TcpMsg.SendCallBack callBack) {
        TcpMsg msg = new TcpMsg(message, MsgInOut.Send);
        msg.setSendCallBack(callBack);
        TcpClient.instance().sendMsg(msg);
    }

    public static int getUTF8Len(String content) {
        try {
            return content.getBytes("utf-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * long 转成4位的16进制字符串
     *
     * @param num
     * @return
     */
    public static String get4ByteHexString(long num) {
        char[] src = Long.toHexString(num).toCharArray();
        String target = "0000";
        char[] targetChars = target.toCharArray();
        int pos = 4 - src.length;
        System.arraycopy(src, 0, targetChars, pos, src.length);
        return String.valueOf(targetChars);
    }

    /**
     * 从原生数组中解析出要查找的字符串第一次出现的下标索引
     *
     * @param sourceData [FISE*201700444400005*0002*LK]
     * @return
     */
    public int indexOfStrInBytes(byte[] sourceData, String str) {
        byte[] target = str.getBytes(Charset.forName(GlobalSettings.instance().getConfig().getCharsetName()));
        LogUtils.d("sourceData " + Arrays.toString(sourceData));
        LogUtils.d("target " + Arrays.toString(target));
        int positon = -1;
        boolean isFirstOccure = false;
        int searchEnd = -1;
        int sourceLen = sourceData.length;
        int targetLen = target.length;
        for (int i = 0; i < sourceLen; i++) {
            if (!isFirstOccure) {
                if (target[0] == sourceData[i]) {
                    positon = i;
                    LogUtils.e("indexOfStrInBytes start " + positon);
                    if (targetLen == 1) {
                        return positon;
                    }
                    searchEnd = positon + targetLen;
                    if (searchEnd > sourceLen) return -1;//要查询的长度大于原数组长度
                    isFirstOccure = true;
                    continue;
                }
            }
            if (positon != -1) {//找到起始位置
                if (i < searchEnd) {
                    if (sourceData[i] != target[i - positon]) {
                        return -1;//没找到
                    }
                    if (i - positon == targetLen - 1) {//最后一位也匹配
                        LogUtils.e("找到了.... ");
                        return positon;//找到了
                    }
                }
            }
        }
        LogUtils.e("indexOfStrInBytes end ");//找遍了也没找到
        return positon;
    }

    public byte[] getUtf8Bytes(String data) {
        if (TextUtils.isEmpty(data)) return null;
        return data.getBytes(GlobalSettings.instance().getChaset());
    }

    /**
     * 获取字节数组的实际长度
     *
     * @param data
     * @return
     */
    public int getBytesLen(byte[] data) {
        if (null == data) return 0;
        int len = data.length;
        LogUtils.e("消息字节长度 " + len);
        return len;
    }
}
