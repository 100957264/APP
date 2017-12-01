package com.android.fisewatchlauncher.entity.dao;

import com.android.fisewatchlauncher.client.GlobalSettings;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/10
 * @time 17:56
 */
@Entity
public class PhoneContractor extends CenterSettingBase {

    @Id(autoincrement = true)
    @OrderBy
    private Long id;
    public String imei;
    private String name;
    private String num;//电话号码
    private String avatar;//头像文件路径
    private String sign;//签名
    private String mood;//心情

    private String nick;//昵称

    public PhoneContractor() {
    }

    public PhoneContractor(String imei) {
        this.imei = imei;
    }

    public PhoneContractor(String name, String num) {
        this.name = name;
        this.num = num;
        setImei(GlobalSettings.instance().getImei());
    }

    @Generated(hash = 653703633)
    public PhoneContractor(Long id, String imei, String name, String num,
            String avatar, String sign, String mood, String nick) {
        this.id = id;
        this.imei = imei;
        this.name = name;
        this.num = num;
        this.avatar = avatar;
        this.sign = sign;
        this.mood = mood;
        this.nick = nick;
    }

    public Long  getId() {
        return id;
    }

    public void setId(Long  id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "PhoneContractor{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sign='" + sign + '\'' +
                ", mood='" + mood + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
