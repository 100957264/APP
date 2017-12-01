package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/10
 * @time 16:47
 */
@Entity
public class CenterSettings extends CenterSettingBase {
    @Id(autoincrement = false)
    private long id;

    public String imei;//主键

    private String centerPhoneNum;

    private long upload_interval;//上传时间间隔

    private String centerPwd;//中心密码

    //=========3个SOS号码同时设置=====
    private String sosPhone;

    private String sosPhone2;

    private String sosPhone3;
    //=========3个SOS号码同时设置=====

    //=========IP端口设置=====
//    [CS*YYYYYYYYYY*LEN*IP,IP或域名,端口]
    @Property(nameInDb = "ip")
    private String center_ip;

    private String domain;

    @Property(nameInDb = "port")
    private int centerPort;

    /***SOS短信报警开关***/
    private int sosSMSSwitch;

    /***低电量短信报警开关***/
    private int lowBatterySwitch;

    //取下手环报警开关
    private int removeAlertSwitch;// 0 或者1 (1打开，0关闭)

    //取下手表短信报警开关(和楼上的不属于依赖关系)
    private int removeSmsAlertSswitch;// 0 或者1 (1打开，0关闭)

    //跌倒报警开关
    private int fallDownSwitch;// 0 或者1 (1打开，0关闭)

    @Generated(hash = 461948431)
    public CenterSettings() {
    }

    public CenterSettings(String imei) {
        this.imei = imei;
    }

    @Generated(hash = 484417054)
    public CenterSettings(long id, String imei, String centerPhoneNum,
            long upload_interval, String centerPwd, String sosPhone,
            String sosPhone2, String sosPhone3, String center_ip, String domain,
            int centerPort, int sosSMSSwitch, int lowBatterySwitch,
            int removeAlertSwitch, int removeSmsAlertSswitch, int fallDownSwitch) {
        this.id = id;
        this.imei = imei;
        this.centerPhoneNum = centerPhoneNum;
        this.upload_interval = upload_interval;
        this.centerPwd = centerPwd;
        this.sosPhone = sosPhone;
        this.sosPhone2 = sosPhone2;
        this.sosPhone3 = sosPhone3;
        this.center_ip = center_ip;
        this.domain = domain;
        this.centerPort = centerPort;
        this.sosSMSSwitch = sosSMSSwitch;
        this.lowBatterySwitch = lowBatterySwitch;
        this.removeAlertSwitch = removeAlertSwitch;
        this.removeSmsAlertSswitch = removeSmsAlertSswitch;
        this.fallDownSwitch = fallDownSwitch;
    }

    public String getCenterPhoneNum() {
        return this.centerPhoneNum;
    }

    public void setCenterPhoneNum(String centerPhoneNum) {
        this.centerPhoneNum = centerPhoneNum;
    }

    public long getUpload_interval() {
        return upload_interval;
    }

    public void setUpload_interval(long upload_interval) {
        this.upload_interval = upload_interval;
    }

    public String getCenterPwd() {
        return this.centerPwd;
    }

    public void setCenterPwd(String centerPwd) {
        this.centerPwd = centerPwd;
    }

    public String getSosPhone() {
        return this.sosPhone;
    }

    public void setSosPhone(String sosPhone) {
        this.sosPhone = sosPhone;
    }

    public String getSosPhone2() {
        return this.sosPhone2;
    }

    public void setSosPhone2(String sosPhone2) {
        this.sosPhone2 = sosPhone2;
    }

    public String getCenter_ip() {
        return this.center_ip;
    }

    public void setCenter_ip(String center_ip) {
        this.center_ip = center_ip;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSosPhone3() {
        return this.sosPhone3;
    }

    public void setSosPhone3(String sosPhone3) {
        this.sosPhone3 = sosPhone3;
    }

    public int getSosSMSSwitch() {
        return this.sosSMSSwitch;
    }

    public void setSosSMSSwitch(int sosSMSSwitch) {
        this.sosSMSSwitch = sosSMSSwitch;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getRemoveAlertSwitch() {
        return this.removeAlertSwitch;
    }

    public void setRemoveAlertSwitch(int removeAlertSwitch) {
        this.removeAlertSwitch = removeAlertSwitch;
    }

    public int getRemoveSmsAlertSswitch() {
        return this.removeSmsAlertSswitch;
    }

    public void setRemoveSmsAlertSswitch(int removeSmsAlertSswitch) {
        this.removeSmsAlertSswitch = removeSmsAlertSswitch;
    }

    public int getLowBatterySwitch() {
        return this.lowBatterySwitch;
    }

    public void setLowBatterySwitch(int lowBatterySwitch) {
        this.lowBatterySwitch = lowBatterySwitch;
    }

    public int getCenterPort() {
        return centerPort;
    }

    public void setCenterPort(int centerPort) {
        this.centerPort = centerPort;
    }

    public int getFallDownSwitch() {
        return fallDownSwitch;
    }

    public void setFallDownSwitch(int fallDownSwitch) {
        this.fallDownSwitch = fallDownSwitch;
    }

    @Override
    public String toString() {
        return "CenterSettings{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", centerPhoneNum='" + centerPhoneNum + '\'' +
                ", upload_interval=" + upload_interval +
                ", centerPwd='" + centerPwd + '\'' +
                ", sosPhone='" + sosPhone + '\'' +
                ", sosPhone2='" + sosPhone2 + '\'' +
                ", sosPhone3='" + sosPhone3 + '\'' +
                ", center_ip='" + center_ip + '\'' +
                ", domain='" + domain + '\'' +
                ", centerPort=" + centerPort +
                ", sosSMSSwitch=" + sosSMSSwitch +
                ", lowBatterySwitch=" + lowBatterySwitch +
                ", removeAlertSwitch=" + removeAlertSwitch +
                ", removeSmsAlertSswitch=" + removeSmsAlertSswitch +
                '}';
    }
}
