package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author mare
 * @Description:TODO 单个群组成员
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/16
 * @time 10:05
 */
@Entity
public class GroupMember {
    @Id
    private Long id;
    private String memberId;
    private String displayName;
    private String nameSpelling;
    private String displayNameSpelling;
    private String groupName;
    private String groupNameSpelling;
    private String groupPortraitUri;
    @Generated(hash = 125441139)
    public GroupMember(Long id, String memberId, String displayName,
            String nameSpelling, String displayNameSpelling, String groupName,
            String groupNameSpelling, String groupPortraitUri) {
        this.id = id;
        this.memberId = memberId;
        this.displayName = displayName;
        this.nameSpelling = nameSpelling;
        this.displayNameSpelling = displayNameSpelling;
        this.groupName = groupName;
        this.groupNameSpelling = groupNameSpelling;
        this.groupPortraitUri = groupPortraitUri;
    }
    @Generated(hash = 1668463032)
    public GroupMember() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMemberId() {
        return this.memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupNameSpelling() {
        return this.groupNameSpelling;
    }
    public void setGroupNameSpelling(String groupNameSpelling) {
        this.groupNameSpelling = groupNameSpelling;
    }
    public String getGroupPortraitUri() {
        return this.groupPortraitUri;
    }
    public void setGroupPortraitUri(String groupPortraitUri) {
        this.groupPortraitUri = groupPortraitUri;
    }

}
