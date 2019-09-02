package com.example.auctionapp.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.dao.AppConfigMapper;
import com.example.auctionapp.entity.AppConfig;
import com.example.auctionapp.service.IAppConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  App版本配置 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Service
public class AppConfigServiceImpl implements IAppConfigService {

    @Resource
    private AppConfigMapper appConfigMapper;

    @Override
    public AppConfig getAppConfig(AppConfig appConfig) {
        AppConfig result = null;
        if (null == appConfig || StringUtils.isEmpty(appConfig.getPlatformType().toString()) || StringUtils.isEmpty(appConfig.getVersion())) {
            return  result;
        }
        List<AppConfig> configs =  appConfigMapper.getAppConfig(appConfig);
        if (null != configs && configs.size() > 0 ){
            result = configs.get(0);

            // 是否是强制更新

            if (appConfig.getPlatformType() == ProjectConstant.PLATFORM_ANDROID) {
                // 获得强制更新的版本列表
                List<AppConfig> filterConfigs = configs.stream()
                        .filter(config -> config.getIsRape() == ProjectConstant.PLATFORM_RAPE)
                        .collect(Collectors.toList());
                if (null != filterConfigs && filterConfigs.size() > 0) {
                    result = filterConfigs.get(0);
                }
            }
        }
        return result;
    }

}
