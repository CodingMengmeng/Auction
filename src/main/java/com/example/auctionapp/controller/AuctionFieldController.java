package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.AuctionField;
import com.example.auctionapp.service.IAuctionFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专场 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@RestController
@RequestMapping("/auctionField")
public class AuctionFieldController {

    @Autowired
    private IAuctionFieldService iAuctionFieldService;

    /**
     * 查询所有拍场
     * @param paging
     * @return
     */
    @PostMapping("/getAuctionField")
    public Result getAuctionField(@RequestBody Paging paging){
        IPage<AuctionField> auctionField = iAuctionFieldService.getAuctionField(paging);
        return ResultGenerator.genPagingResult(auctionField.getTotal(),auctionField.getRecords());
    }

    /**
     * 查询某个拍场中的拍品
     * @param paging
     * @return
     */
    @PostMapping("/getAuctionFieldOfGoods")
    public Result getAuctionFieldOfGoods(@RequestBody Paging paging){
        if (paging.getFieldId()==null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        IPage<Map<String,Object>> auctionField = iAuctionFieldService.getAuctionFieldOfGoods(paging);
        return ResultGenerator.genPagingResult(auctionField.getTotal(),auctionField.getRecords());
    }


}
