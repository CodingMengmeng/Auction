package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.Images;
import com.example.auctionapp.service.IImagesService;
import com.example.auctionapp.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import static com.example.auctionapp.util.Base64.getBytesBASE64;

/**
 * <p>
 * 图片 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@RestController
@RequestMapping("/images")
public class ImagesController {


    @Value("${image.path}")
    private String uploadPath;

    @Resource
    IImagesService iImagesService;

    /**
     * 根据主体id和图片类型查询图片列表
     *
     * @param images
     * @return
     */
    @PostMapping("/getImagesBySubjectIdAndType")
    public Result getImagesBySubjectIdAndType(@RequestBody Images images) {
        //参数校验
        if (images == null || images.getSubjectId() == null || images.getType() == null) {
            log.error("参数错误，请正确传递参数");
            return ResultGenerator.genSuccessMEssageResult("参数错误，请正确传递参数");
        }
        //获取图片列表
        List<Images> iList = iImagesService.selectBySubjectIdAndTpye(images);
        //组包返回
        return ResultGenerator.genSuccessResult(iList);
    }


    /**
     * 根据图片主体id和图片类型获取图片列表
     *
     * @param images images
     * @return
     */
    @WebLog("根据图片主体id和图片类型获取图片列表")
    @PostMapping("updateBySubjectIdAndType")
    public Result updateBySubjectIdAndType(@RequestHeader("userId") int cutomerId, Images images) {
        System.out.println(cutomerId);
        System.out.println(images);
        images.setSubjectId(cutomerId);
        return iImagesService.updateBySubjectIdAndTpye(images);
    }


    /**
     * 上传图片，单个上传
     *
     * @param map
     * @return
     */
    @PostMapping("/appUpload")
    public Result upload(@RequestBody Map<String, String> map) throws Exception {

        if (map.get("file") == null) {
            return Result.errorInfo("图片为空");
        }
        Integer random = (int) ((Math.random() * 9 + 1) * 1000);
        String fileName = System.currentTimeMillis() + random.toString() + ".png";

        byte[] bytesBASE64 = getBytesBASE64(map.get("file"));

        FileUtil.uploadFile(bytesBASE64, uploadPath, fileName);

        return Result.success(fileName);
    }

}
