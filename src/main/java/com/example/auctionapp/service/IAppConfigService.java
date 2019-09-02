package com.example.auctionapp.service;

import com.example.auctionapp.entity.AppConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * App版本配置 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
public interface IAppConfigService {

    /**
     * 获得app配置信息
     *
     * @param appConfig
     * @return
     */
    AppConfig getAppConfig(AppConfig appConfig);

}
