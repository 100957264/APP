package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "WetFriend")
public class Friend {
    @Id
    private String friendId;
    private String displayName;
    private String region;
    private String phoneNumber;
    private String status;
    private Long timestamp;
    private String letters;
    private String nameSpelling;
    private String displayNameSpelling;
    @Generated(hash = 624946911)
    public Friend(String friendId, String displayName, String region,
            String phoneNumber, String status, Long timestamp, String letters,
            String nameSpelling, String displayNameSpelling) {
        this.friendId = friendId;
        this.displayName = displayName;
        this.region = region;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.timestamp = timestamp;
        this.letters = letters;
        this.nameSpelling = nameSpelling;
        this.displayNameSpelling = displayNameSpelling;
    }
    @Generated(hash = 287143722)
    public Friend() {
    }
    public String getFriendId() {
        return this.friendId;
    }
    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getRegion() {
        return this.region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getLetters() {
        return this.letters;
    }
    public void setLetters(String letters) {
        this.letters = letters;
    }
    public String getNameSpelling() {
        return this.nameSpelling;
    }
    public void setNameSpelling(String nameSpelling) {
        this.nameSpelling = nameSpelling;
    }
    public String getDisplayNameSpelling() {
        return this.displayNameSpelling;
    }
    public void setDisplayNameSpelling(String displayNameSpelling) {
        this.displayNameSpelling = displayNameSpelling;
    }

}
