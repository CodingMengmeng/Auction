package com.example.auctionapp.dao;

import com.example.auctionapp.entity.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    List<Dictionary> selectAll();
}
