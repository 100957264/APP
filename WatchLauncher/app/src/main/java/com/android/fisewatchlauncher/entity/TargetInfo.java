package com.android.fisewatchlauncher.entity;


import com.android.fisewatchlauncher.utils.ExceptionUtils;
import com.android.fisewatchlauncher.utils.StringValidationUtils;


/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/1 0001
 * @time 03:37
 */
public class TargetInfo {
    private String ip;
    private int port;

    public TargetInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
        check();
    }

    private void check() {
        if (!StringValidationUtils.validateRegex(port + "", StringValidationUtils.RegexPort)) {
            ExceptionUtils.throwException("port 格式不合法");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetInfo that = (TargetInfo) o;

        if (port != that.port) return false;
        return ip != null ? ip.equals(that.ip) : that.ip == null;

    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "TargetInfo{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
