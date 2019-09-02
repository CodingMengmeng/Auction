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
 * 数据字典
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 字典类型
     */
    @TableField("type")
    private String type;

    /**
     * 字典key
     */
    @TableField("dic_key")
    private String dicKey;

    /**
     * 字典value
     */
    @TableField("dic_value")
    private String dicValue;

    /**
     * 备注
     */
    @TableField("memo")
    private String memo;


}
