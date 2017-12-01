package com.android.fisewatchlauncher.client.helper.decode;

import com.android.fisewatchlauncher.client.TcpConnConfig;
import com.android.fisewatchlauncher.entity.TargetInfo;

public class BaseDecodeHelper implements AbsDecodeHelper {
    @Override
    public byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig) {
        return new byte[][]{data};
    }
}
