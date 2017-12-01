package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author mare
 * @Description:TODO 通用微聊消息(微聊和对讲的消息)
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/17
 * @time 20:23
 */
@Entity
public class WetMessage {
    @Id
    public Long id;

    private long talkingId;
    private String senderId;
    private String targetId;
    private String senderName;
    private String targetUserName;
    private Long timestamp;//用于时间线布局

    @Convert(converter = MessageDirectionConverter.class, columnType = Integer.class)
    public MessageDirection messageDirection;

    @Convert(converter = MessageContentTypeConverter.class, columnType = Integer.class)
    public MessageContentType messageContentType;

    @Convert(converter = ConverstationTypeConverter.class, columnType = Integer.class)
    private ConverstationType converstationType;

    @Convert(converter = RecvStatusConverter.class, columnType = Integer.class)
    private RecvStatus recvStatus;

    @Convert(converter = SendStatusConverter.class, columnType = Integer.class)
    private SendStatus sendStatus;

    private String msgContent;//文本消息内容
    private String imgUrl;//网络图片消息链接
    private String imgPath;//本地图片消息的路径
    private String headerUrl;//网络头像链接
    private String headerPath;//本地头像路径
    private String audioPath;//网络/本地音频文件链接(mediaplayer 都可以播放)
    private long voiceTime;//录音时长
    private String videoUrl;//网络视频文件链接
    private String videoPath;//本地视频文件路径

    private boolean isShowTimeLine = false;//是否显示时间线布局

    @Generated(hash = 1378425516)
    public WetMessage() {
    }

    @Generated(hash = 935929950)
    public WetMessage(Long id, long talkingId, String senderId, String targetId, String senderName,
            String targetUserName, Long timestamp, MessageDirection messageDirection,
            MessageContentType messageContentType, ConverstationType converstationType, RecvStatus recvStatus,
            SendStatus sendStatus, String msgContent, String imgUrl, String imgPath, String headerUrl,
            String headerPath, String audioPath, long voiceTime, String videoUrl, String videoPath,
            boolean isShowTimeLine) {
        this.id = id;
        this.talkingId = talkingId;
        this.senderId = senderId;
        this.targetId = targetId;
        this.senderName = senderName;
        this.targetUserName = targetUserName;
        this.timestamp = timestamp;
        this.messageDirection = messageDirection;
        this.messageContentType = messageContentType;
        this.converstationType = converstationType;
        this.recvStatus = recvStatus;
        this.sendStatus = sendStatus;
        this.msgContent = msgContent;
        this.imgUrl = imgUrl;
        this.imgPath = imgPath;
        this.headerUrl = headerUrl;
        this.headerPath = headerPath;
        this.audioPath = audioPath;
        this.voiceTime = voiceTime;
        this.videoUrl = videoUrl;
        this.videoPath = videoPath;
        this.isShowTimeLine = isShowTimeLine;
    }

    public enum MessageDirection {
        REC(0), SEND(1);
        final int id;

        MessageDirection(int id) {
            this.id = id;
        }
    }

    public static class MessageDirectionConverter implements PropertyConverter<MessageDirection, Integer> {
        //将Integer值转换成Role值
        @Override
        public MessageDirection convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (MessageDirection direction : MessageDirection.values()) {
                if (direction.id == databaseValue) {
                    return direction;
                }
            }
            return MessageDirection.SEND;
        }

        //将MessageDirection值转换成Integer值
        @Override
        public Integer convertToDatabaseValue(MessageDirection entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    /**
     * 文本 图片 音频 视频 时间线
     */
    public enum MessageContentType {
        TXT(0), IMG(1), AUDIO(2), VIDEO(3);
        final int id;

        MessageContentType(int id) {
            this.id = id;
        }
    }

    public MessageDirection getMessageDirection() {
        return this.messageDirection;
    }

    public void setMessageDirection(MessageDirection messageDirection) {
        this.messageDirection = messageDirection;
    }

    public MessageContentType getMessageContentType() {
        return this.messageContentType;
    }

    public void setMessageContentType(MessageContentType messageContentType) {
        this.messageContentType = messageContentType;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTargetUserName() {
        return this.targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public RecvStatus getRecvStatus() {
        return this.recvStatus;
    }

    public void setRecvStatus(RecvStatus recvStatus) {
        this.recvStatus = recvStatus;
    }

    public SendStatus getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTalkingId() {
        return this.talkingId;
    }

    public void setTalkingId(long talkingId) {
        this.talkingId = talkingId;
    }

    public ConverstationType getConverstationType() {
        return this.converstationType;
    }

    public void setConverstationType(ConverstationType converstationType) {
        this.converstationType = converstationType;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static class MessageContentTypeConverter implements PropertyConverter<MessageContentType, Integer> {
        //将Integer值转换成Role值
        @Override
        public MessageContentType convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (MessageContentType direction : MessageContentType.values()) {
                if (direction.id == databaseValue) {
                    return direction;
                }
            }
            return MessageContentType.TXT;
        }

        //将MessageContentType值转换成Integer值
        @Override
        public Integer convertToDatabaseValue(MessageContentType entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    public enum RecvStatus {
        READ(0), UNREAD(1);
        final int id;

        RecvStatus(int id) {
            this.id = id;
        }
    }

    public static class RecvStatusConverter implements PropertyConverter<RecvStatus, Integer> {
        //将Integer值转换成Role值
        @Override
        public RecvStatus convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (RecvStatus recvStatus : RecvStatus.values()) {
                if (recvStatus.id == databaseValue) {
                    return recvStatus;
                }
            }
            return RecvStatus.UNREAD;
        }

        //将RecvStatus值转换成Integer值
        @Override
        public Integer convertToDatabaseValue(RecvStatus entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    public enum SendStatus {
        SENDING(0), FAILURE(1), SUCCESS(2);
        final int id;

        SendStatus(int id) {
            this.id = id;
        }
    }

    public static class SendStatusConverter implements PropertyConverter<SendStatus, Integer> {
        //将Integer值转换成Role值
        @Override
        public SendStatus convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (SendStatus sendStatus : SendStatus.values()) {
                if (sendStatus.id == databaseValue) {
                    return sendStatus;
                }
            }
            return SendStatus.SENDING;
        }

        //将SendStatus值转换成Integer值
        @Override
        public Integer convertToDatabaseValue(SendStatus entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    /**
     * 单聊 群聊 聊天室
     */
    public enum ConverstationType {
        SINGLE(0), GROUP(1), CHATROOM(2);
        final int id;

        ConverstationType(int id) {
            this.id = id;
        }
    }

    public static class ConverstationTypeConverter implements PropertyConverter<ConverstationType, Integer> {
        //将Integer值转换成Role值
        @Override
        public ConverstationType convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (ConverstationType converstationType : ConverstationType.values()) {
                if (converstationType.id == databaseValue) {
                    return converstationType;
                }
            }
            return ConverstationType.SINGLE;
        }

        //将ConverstationType值转换成Integer值
        @Override
        public Integer convertToDatabaseValue(ConverstationType entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("WetMessage{" +
                "id=" + id +
                ", talkingId=" + talkingId +
                ", senderId='" + senderId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", targetUserName='" + targetUserName + '\'' +
                ", timestamp=" + timestamp +
                ", msgContent=" + msgContent);
        if (null != messageDirection) {
            sb.append(", messageDirection=" + messageDirection.toString());
            sb.append(messageDirection == MessageDirection.REC ? (", recvStatus=" + recvStatus.toString())
                    : (", sendStatus=" + sendStatus.toString()));
        }
        if (null != messageContentType) {
            sb.append(", messageContentType=" + messageContentType.toString());
        }
        return sb.toString();
    }

    public String getMsgContent() {
        return this.msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getHeaderUrl() {
        return this.headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getHeaderPath() {
        return this.headerPath;
    }

    public void setHeaderPath(String headerPath) {
        this.headerPath = headerPath;
    }

    public String getAudioPath() {
        return this.audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public long getVoiceTime() {
        return this.voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    public boolean isShowTimeLine() {
        return isShowTimeLine;
    }

    public void setShowTimeLine(boolean showTimeLine) {
        isShowTimeLine = showTimeLine;
    }

    public boolean getIsShowTimeLine() {
        return this.isShowTimeLine;
    }

    public void setIsShowTimeLine(boolean isShowTimeLine) {
        this.isShowTimeLine = isShowTimeLine;
    }
}
