package com.android.fisewatchlauncher.function.media;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.utils.CloseUtils;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author mare
 * @Description:TODO 媒体文件上传格式转换工具类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/22
 * @time 19:50
 */
public class MediaTransformUtils {

    /**
     * TODO 转换成要上传的数据
     *
     * @param data
     * @return
     */
    public static byte[] getDataUpload(byte[] data) {
        if (null == data || data.length <= 0) return null;
        int len = data.length;
        LogUtils.e("data的长度是  " + len);
        ByteBuffer buffer = ByteBuffer.allocate(len + 1000);//预留一千个字节供特殊字符转义
        for (byte temp : data) {
            switch (temp) {//当前temp的值
                case 0X7D:
                    buffer.put((byte) 0X7D);
                    buffer.put((byte) 0X01);
                    break;
                case 0X5B:
                    buffer.put((byte) 0X7D);
                    buffer.put((byte) 0X02);
                    break;
                case 0X5D:
                    buffer.put((byte) 0X7D);
                    buffer.put((byte) 0X03);
                    break;
                case 0X2C:
                    buffer.put((byte) 0X7D);
                    buffer.put((byte) 0X04);
                    break;
                case 0X2A:
                    buffer.put((byte) 0X7D);
                    buffer.put((byte) 0X05);
                    break;
                default:
                    buffer.put(temp);
                    break;
            }
        }
        buffer.flip();
        LogUtils.e("buffer的限制长度是 " + buffer.limit());
        int bufferLen = buffer.limit();
        byte[] uploadData = new byte[bufferLen];
        buffer.get(uploadData);
        return uploadData;
    }

    /**
     * TODO 转换成要显示的字节数组数据
     *
     * @param data
     * @return
     */
    public static byte[] getDataDisplay(byte[] data) {
        if (null == data || data.length <= 0) return null;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        boolean isPrevious0X7D = false;
        int curPos;
        for (byte temp : data) {
            curPos = buffer.position();
            if (isPrevious0X7D) {//上个temp的判断
                switch (temp) {//当前temp的值
                    case 0X01:
                        buffer.position(curPos - 1);
                        buffer.put((byte) 0X7D);//覆盖上个值
                        break;
                    case 0X02:
                        buffer.position(curPos - 1);
                        buffer.put((byte) 0X5B);//覆盖上个值
                        break;
                    case 0X03:
                        buffer.position(curPos - 1);
                        buffer.put((byte) 0X5D);//覆盖上个值
                        break;
                    case 0X04:
                        buffer.position(curPos - 1);
                        buffer.put((byte) 0X2C);//覆盖上个值
                        break;
                    case 0X05:
                        buffer.position(curPos - 1);
                        buffer.put((byte) 0X2A);//覆盖上个值
                        break;
                    default:
                        buffer.put(temp);//不用覆盖
                        break;
                }
            } else {
                buffer.put(temp);
            }
            if (temp == 0X7D) {
                isPrevious0X7D = true;
            } else {
                isPrevious0X7D = false;
            }
        }
        buffer.flip();
        int bufferLen = buffer.limit();
        byte[] displayData = new byte[bufferLen];
        buffer.get(displayData);
        return displayData;
    }

    /**
     * 上传数据格式得大写
     *
     * @param byteData
     * @return
     */
    public static String int2HexString(int byteData) {
        return "0X" + Integer.toHexString(byteData).toUpperCase();
    }

    public static int hexString2Int(String hexData) {
        if (hexData.contains("0X")) {
            hexData = hexData.substring(2);
        }
        return Integer.parseInt(hexData, 16);
    }

    public static byte[] getBytesRaw(int res) {
        InputStream is = KApplication.sContext.getResources().openRawResource(res);
        ByteArrayOutputStream os = null;
        byte[] bytesRaw = null;
        try {
            os = new ByteArrayOutputStream();
            int sBufferSize = is.available();//获取本地文件字节数
            byte[] b = new byte[sBufferSize];
            int len;
            while ((len = is.read(b, 0, sBufferSize)) != -1) {
                os.write(b, 0, len);
            }
            bytesRaw = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(is, os);
            return bytesRaw;
        }
    }

    public static byte[] file2bytes(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(fis, bos);
        }
        return buffer;
    }


}
