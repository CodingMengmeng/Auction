package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.GoodsType;
import com.example.auctionapp.service.IGoodsTypeService;
import com.example.auctionapp.vo.ClassifyVo;
import com.example.auctionapp.vo.HomeGoodsVo;
import com.example.auctionapp.vo.re.HomeGoodsRe;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.protocol.ResponseDate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍品类型 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Api(value = "/goodsType/", tags = "分类Api")
@RestController
@RequestMapping("/goodsType")
public class GoodsTypeController {


    @Resource
    IGoodsTypeService goodsTypeService;


    /**
     * 分层查找出一级二级分类
     *
     * @return ResponseDate
     * @author 孔邹祥
     */
    @WebLog("分层查找出一级二级分类")
    @PostMapping("selectHierarchical")
    public Result selectHierarchical() {
        List<Map<String, Object>> maps = goodsTypeService.selectHierarchical();
        return Result.success(maps);
    }

    /**
     * 分层查找出一级分类
     *
     * @return Result
     * @author MaHC
     */
    @WebLog("查找一级分类")
    @PostMapping("getOneClassify")
    @ApiOperation(value = "查询一级分类")
    public Result<ClassifyVo> getOneClassify(){
        List<ClassifyVo> oneClassify = goodsTypeService.getOneClassify();
        return Result.success(oneClassify);
    }

    /**
     * 查询拍品，根据拍品名称和拍品分类
     *
     * @return Result
     * @author MaHC
     */
    @WebLog("查询拍品")
    @PostMapping("getAuctionGoodsWhereType")
    @ApiOperation(value = "根据分类查出拍品")
    public Result<HomeGoodsVo> getAuctionGoodsWhereType(@RequestBody HomeGoodsRe homeGoodsRe){
        if (homeGoodsRe.getPage() == null || homeGoodsRe.getRows() == null){
            return Result.errorInfo("分页参数错误");
        }
        IPage<HomeGoodsVo> auction = goodsTypeService.getAuctionGoodsWhereType(homeGoodsRe);
        return  Result.success(auction.getRecords());
    }



}
