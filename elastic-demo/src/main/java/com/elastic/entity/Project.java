package com.elastic.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-14
 */

@Document(indexName="project")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Project implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    /**
     * 项目名称
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String projectName;

    /**
     * 项目经理id
     */
    private Integer projectManagerId;

    /**
     * 项目干系人
     */
    private String projectRelatedUser;

    /**
     * 项目状态   1：正常    2:质保中     3：完成
     */
    private Boolean projectStatus;

    /**
     * 开始日期
     */
    @Field(type = FieldType.Date,
        format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束日期
     */
//    @Field(type = FieldType.Date,
//        format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime endTime;

    /**
     * 创建时间
     */
//    @Field(type = FieldType.Date,
//        format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 发电时间
     */
//    @Field(type = FieldType.Date,
//        format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime powerGeneration;

    /**
     * 地址
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String address;

    /**
     * 经度
     */

    private BigDecimal longitude;

    /**
     * 维度
     */
    private BigDecimal latitude;

    /**
     * 甲方公司名称
     */
    private String firstPartyName;

    /**
     * 甲方联系电话
     */
    private String firstPartyTel;

    /**
     * 项目描述
     */
    private String projectDescription;

    private Integer count;

    /**
     * 群id
     */
    private String tid;

    /**
     * 是否汇报 0：不汇报 1：汇报
     */
    private String isReport;

    /**
     * 汇报是否发群聊：0.不群聊 1.发群聊
     */
    private String isGroupChat;


}
