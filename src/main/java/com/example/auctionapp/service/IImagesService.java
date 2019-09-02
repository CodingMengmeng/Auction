package com.example.auctionapp.service;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.Images;

import java.util.List;

/**
 * <p>
 * 图片 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IImagesService {

    /**
     * 根据图片主体id和图片类型获取图片列表
     *
     * @return
     */
    List<Images> selectBySubjectIdAndTpye(Images images);


    /**
     * 根据图片主体id和图片类型获取图片列表
     *
     * @param images images
     * @return
     */
    Result updateBySubjectIdAndTpye(Images images);
}
