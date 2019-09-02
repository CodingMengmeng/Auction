package com.example.auctionapp.configurer;

import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.dao.DictionaryMapper;
import com.example.auctionapp.entity.Dictionary;
import com.example.auctionapp.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zll
 * @date 2018/8/28.
 */
@Configuration
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Resource
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private IRedisService iRedisService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {  //项目启动后，执行启动消费者方法
        //initDictionary();
    }


    /**
     * 数据字典存入redis
     */
    private void initDictionary() {
        List<Dictionary> dictionaries = dictionaryMapper.selectAll();
        Map<String, Dictionary> dictionaryMap = new HashMap<>();

        if (CollectionUtils.isEmpty(dictionaries)) {
            return;
        }

        dictionaries.forEach(dictionary -> dictionaryMap.put(dictionary.getType() + ":" + dictionary.getDicKey(), dictionary));
        iRedisService.set(ProjectConstant.DICTIONARYS_KEY, dictionaryMap);
    }

}
