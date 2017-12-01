package com.android.fisewatchlauncher.client.helper.stickpackage;


import com.android.fisewatchlauncher.utils.CharsetUtil;
import com.android.fisewatchlauncher.utils.ExceptionUtils;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 特定字符的粘包处理,首尾各一个Byte[],不可以同时为空，如果其中一个为空，那么以不为空的作为分割标记
 * 例：协议制定为  ^+数据+$，首就是^，尾是$
 */
public class SpecifiedStickPackageHelper implements AbsStickPackageHelper {
    private byte[] head;
    private byte[] tail;
    //    private List<Byte> bytes;
    private static final int THRESHOLD_CAPCITY = 1024/* * 1024 * 4*/; //4mb阀值
    private int headLen, tailLen;
    private boolean isDebug = false;

    public SpecifiedStickPackageHelper(byte[] head, byte[] tail) {
        this.head = head;
        this.tail = tail;
        if (head == null || tail == null) {
            ExceptionUtils.throwException(" head or tail ==null");
        }
        if (head.length == 0 && tail.length == 0) {
            ExceptionUtils.throwException(" head and tail length==0");
        }
        headLen = head.length;
        tailLen = tail.length;
        LogUtils.e(" headLen -- tailLen " + headLen + " -- " + tailLen);
//        bytes = new ArrayList<>();
    }

    private boolean endWith(byte[] src, byte[] target) {
        if (src.length < target.length) {
            return false;
        }
        for (int i = 0; i < target.length; i++) {//逆序比较
            if (target[target.length - i - 1] != src[src.length - i - 1]) {
                return false;
            }
        }
        boolean isMatch = true;
        int indexTarget = target.length - 1, indexSrc = src.length - 1;
        StringBuffer sb = new StringBuffer();
        if (target.length == 1) {
            isMatch = target[indexTarget] == src[indexSrc];
            sb.append("isMatch " + isMatch + "  indexTarget " + indexTarget + " , indexSrc " + indexSrc)
                    .append("\n isMatch " + isMatch + "  target" + target[indexTarget] + " , src " + src[indexSrc]);
        } else {
            try {
                for (int i = 0; i < target.length; i++) {//逆序比较
                    indexTarget--;//逆序比较
                    indexSrc--;
                    isMatch = target[indexTarget] == src[indexSrc];
                    sb.append("isMatch " + isMatch + "  indexTarget " + indexTarget + " , indexSrc " + indexSrc)
                            .append("\n  isMatch " + isMatch + "  target" + target[indexTarget] + " , src " + src[indexSrc]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e("yyyy " + e);
            }
        }

        print(sb.toString());
        return true;
    }

//    private byte[] getRangeBytes(List<Byte> list, int start, int end) {
//        Byte[] temps = Arrays.copyOfRange(list.toArray(new Byte[0]), start, end);
//        byte[] result = new byte[temps.length];
//        for (int i = 0; i < result.length; i++) {
//            result[i] = temps[i];
//        }
//        return result;
//    }

    private byte[] getRangeBytes(ByteBuffer bytes, int start, int end) {
        int len = end - start + 1;
        byte[] result = new byte[len];
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes.get();
            bytes.clear();//一定要加上
        }
        //pos = limit
        return result;
    }

    @Override
    public byte[] execute(InputStream is) {
        ByteBuffer bytes = ByteBuffer.allocate(THRESHOLD_CAPCITY);
        bytes.clear();//pos =0; limit = capcity;
        int len = -1;
        byte temp;
        int startIndex = -1;
        byte[] result = null;
        boolean isFindStart = false, isFindEnd = false;
        int currentPosition = 0;
        int maxCap = THRESHOLD_CAPCITY;//当前最大容量
        print("init start!!!!");
        try {
            while ((len = is.read()) != -1) {
                temp = (byte) len;
//                bytes.add(temp);
                if (currentPosition + 1 >= maxCap) {//超过阈值4M
                    //先判断最后一个开始字符"[ " startIndex的位置
                    print("超过阈值啦啦啦啦啦啦 startIndex " + startIndex);
                    if (-1 == startIndex) {
                        // TODO do nothing
                    } else if (startIndex == 0) {
                        // TODO 收到的这条信息大于阈值 这版本暂时不做处理
                        maxCap += THRESHOLD_CAPCITY;
                        //int newCap = calculateNewCapacity(currentPosition);
                        ByteBuffer newBuffer = ByteBuffer.allocate(maxCap);
                        int tmp_len = currentPosition - 1 - startIndex + 1;
                        byte[] tmp_limit = new byte[tmp_len];
                        bytes.position(startIndex);
                        bytes.get(tmp_limit, 0, tmp_len);// pos = currentPosition;
                        print("==== ============" + bytes.toString());
                        bytes = newBuffer.put(tmp_limit, 0, tmp_len);
                        currentPosition = tmp_len;//重置limit
                    } else {
                        // TODO 将startIndex 到 currentPosition之间的内容(也就是最后一条消息)复制到0的位置
                        int tmp_len = currentPosition - 1 - startIndex + 1;
                        print("cccc startIndex " + startIndex + " ,currentPosition " + currentPosition + " , tmp_len " + tmp_len);
                        byte[] tmp_limit = new byte[tmp_len];
                        print("dddd ============" + bytes.toString());
//                        get(byte[] dst, int offset, int length)
//                        从position位置开始相对读，读length个byte，并写入dst下标从offset到offset+length的区域
                        bytes.position(startIndex);
                        try {
                            bytes.get(tmp_limit, 0, tmp_len);// pos = currentPosition;
                        } catch (Exception e) {
                            LogUtils.e("xxxxxxx " + e);
                        }
                        print("eeee ============" + bytes.toString());
                        bytes.position(0);
                        bytes.limit(tmp_len);
                        print("ffff ============");
                        bytes.put(tmp_limit, 0, tmp_len);

                        startIndex = 0;
                        bytes.position(tmp_len);//移到要写的位置
                        currentPosition = tmp_len;//重置limit
                    }
                }
                bytes.limit(currentPosition + 1);
                bytes.position(currentPosition);
                bytes.put(temp);//pos = lastLimit;
                currentPosition = bytes.position();
                bytes.flip();// limit = currentPosition;pos = 0;
                byte[] byteArray = new byte[bytes.limit()];//bytes.remaining()
                //将ByteBuffer转化为byte[]
                bytes.get(byteArray, 0, byteArray.length);// pos = limit;
//                LogUtils.e(bytes.toString());
                if (!isFindStart) {
                    if (endWith(byteArray, head)) {
                        startIndex = byteArray.length - headLen;
                        isFindStart = true;
                        isFindEnd = false;
                        String data = CharsetUtil.dataToString(result, CharsetUtil.UTF_8);
                        LogUtils.d("开始找包..... " + data);
                    }
                } else if (!isFindEnd) {
                    if (endWith(byteArray, tail)) {
//                            if (startIndex + headLen <= bytes.size() - tailLen) {
                        isFindEnd = true;
                        isFindStart = false;
                        result = new byte[bytes.limit() - startIndex];
                        bytes.position(startIndex);
                        bytes.get(result, 0, result.length);
//                            result = getRangeBytes(bytes, startIndex, bytes.limit());
                        String data = CharsetUtil.dataToString(result, CharsetUtil.UTF_8);
                        LogUtils.d("完整包已经找到了..... " + data);
                        LogUtils.d("完整包已经找到了..... " + Arrays.toString(result));
                        break;
                    }
//                        }
                }
            }
            if (len == -1) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    int maxCapacity = THRESHOLD_CAPCITY * 10;//40m

    private int calculateNewCapacity(int minNewCapacity) {
        int maxCapacity = this.maxCapacity;
        if (minNewCapacity == THRESHOLD_CAPCITY) {//如果新容量为阀值，直接返回
            return THRESHOLD_CAPCITY;
        } else {
            int newCapacity;
            if (minNewCapacity > THRESHOLD_CAPCITY) {//如果传入的新容量大于阀值，进行计算
                newCapacity = minNewCapacity / THRESHOLD_CAPCITY * THRESHOLD_CAPCITY;
                if (newCapacity > maxCapacity - THRESHOLD_CAPCITY) {//如果大于最大容量，新容量为最大容量
                    newCapacity = maxCapacity;
                } else {//否则新容量 + 阀值 4mb，按照阀值扩容
                    newCapacity += THRESHOLD_CAPCITY;
                }
                return newCapacity;
            } else {//如果小于阀值，则以64为计数倍增，知道倍增的结果>=需要的容量值
                for (newCapacity = 64; newCapacity < minNewCapacity; newCapacity <<= 1) {

                }
                return Math.min(newCapacity, maxCapacity);
            }
        }
    }

    private void print(Object o) {
        if (isDebug) {
            LogUtils.e(o);
        }
    }
}
