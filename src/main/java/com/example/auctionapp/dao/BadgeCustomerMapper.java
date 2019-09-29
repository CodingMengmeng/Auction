package com.example.auctionapp.dao;

import com.example.auctionapp.entity.BadgeCustomer;
import com.example.auctionapp.vo.BadgeCustomerVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @description 用户徽章信息接口
 * @author mengjia
 * @date 2019/9/1
 * @version 1.0
 * @see 
 */
public interface BadgeCustomerMapper extends BaseMapper<BadgeCustomer>{
    /**
     * @description 查询用户的徽章信息：客户编号，徽章等级编号，徽章等级，徽章名称和拍豆消耗值
     * @author mengjia
     * @date 2019/9/1
     * @param id
     * @return
     * @throws
     **/
    List<BadgeCustomerVo> selectBadgeInfosById(@Param("id")Integer id);

    /**
     * 修改用户的贡献徽章对应的拍豆消耗值
     * @param badgeCustomerVo
     * @return
     */
    Integer updateCustomerctrbBadgeBeans( BadgeCustomerVo badgeCustomerVo);
    /**
     * 根据徽章类型和等级查询徽章的等级ID
     * @param badgeId 徽章类型
     * @Param level 徽章等级
     * @return
     */
    Map<String,Object> selectBadgeLevelId(@Param("badgeId")Integer badgeId, @Param("level")Integer level);
}
