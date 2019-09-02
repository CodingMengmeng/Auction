package com.example.auctionapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 图片
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片类型 1:拍品图片 2：拍场图片
     */
    @TableField("type")
    private Integer type;

    /**
     * 主体ID
     */
    @TableField("subject_id")
    private Integer subjectId;

    /**
     * 图片URL
     */
    @TableField("url")
    private String url;

    /**
     * 乐观锁标志
     */
    @TableField("version")
    private Integer version;


}
