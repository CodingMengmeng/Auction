package com.example.auctionapp.dao;

import com.example.auctionapp.entity.Images;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

/**
 * <p>
 * 图片 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface ImagesMapper extends BaseMapper<Images> {

    List<Images> selectBySubjectIdAndTpye(Images images);
}
