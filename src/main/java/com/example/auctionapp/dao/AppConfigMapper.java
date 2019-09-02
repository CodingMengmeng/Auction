package com.example.auctionapp.dao;

import com.example.auctionapp.entity.AppConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  App版本配置类 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
public interface AppConfigMapper extends BaseMapper<AppConfig> {

    /**
     * 查询APP配置信息
     * @param appConfig
     * @return
     */
    List<AppConfig> getAppConfig(AppConfig appConfig);

    /**
     *
     * @param userId
     * @return
     */
    int updateLastLoginTimeById(@Param(value = "userId") String userId);

}
