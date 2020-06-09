package com.elastic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网易云信群聊历史记录
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CloudHistory implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String body;

    /**
     * 发送人员accid
     */
    private String from;

    /**
     * //1：android、2:iOS、4：PC、16:WEB、32:REST、64:MAC
     */
    private String fromClientType;

    /**
     * 网易云信id
     */
    private String msgId;

    /**
     * 发送端的编码
     */
    private String msgIdClient;

    /**
     * 发送时间  时间戳 毫秒
     */
    private Long sendTime;

    /**
     * 群notifycation通知 1.普通文本 2.图片消息 3.语音消息 4.视频消息 5.地里位置消息 6.文件消息  7.第三方自定义消息 8.群内系统通知
     */
    private Boolean type;

    /**
     * 群tid
     */
    private String tid;

    /**
     * 发送时间
     */
    private LocalDateTime sendLocalTime;


}
