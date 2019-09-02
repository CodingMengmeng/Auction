package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.dao.ImagesMapper;
import com.example.auctionapp.entity.Images;
import com.example.auctionapp.service.IImagesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 图片 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class ImagesServiceImpl implements IImagesService {

    @Resource
    ImagesMapper imagesMapper;

    /**
     * 根据图片主体id和图片类型获取图片列表
     *
     * @return
     */
    @Override
    public List<Images> selectBySubjectIdAndTpye(Images images) {
        return imagesMapper.selectBySubjectIdAndTpye(images);
    }

    @Override
    public Result updateBySubjectIdAndTpye(Images images) {
        return Result.success(imagesMapper.update(images, new UpdateWrapper<Images>().eq("subject_id",
                images.getSubjectId()).eq(
                "type", 1)));
    }
}
