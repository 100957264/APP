package com.android.fisewatchlauncher.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author mare
 * @Description: java中char[]和byte[]的转换
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/14
 * @time 9:29
 */
public class CharBytesConvert {

    /***
     * char[]转byte[]
     *
     * @param chars
     * @return
     */
    public static byte[] chars2Bytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        return bb.array();
    }

    /**
     * byte[]转char[]
     *
     * @param bytes
     * @return
     */
    public static char[] bytes2Chars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }
}
