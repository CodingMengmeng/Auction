package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.service.IRecommendService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Resource
    private IRecommendService iRecommendService;

    /**
     * 查询热门推荐
     * @author 马会春
     * @return
     */
    @WebLog("查询热门推荐")
    @RequestMapping(value = "/getRecommend",method = RequestMethod.POST)
    public Result getRecommend(){
        List<Map<String, Object>> list = iRecommendService.selectRecommend();
        return ResultGenerator.genPagingResult(new Long(list.size()),list);
    }
}
