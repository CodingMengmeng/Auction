package com.example.auctionapp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色and权限
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * permission_id
     */
    @TableField("pid")
    private Integer pid;

    /**
     * role_id
     */
    @TableField("rid")
    private Integer rid;

    @TableField("version")
    private Integer version;


}
