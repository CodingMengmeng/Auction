package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.service.IDailySelectionService;
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
@RequestMapping("/dailySelection")
public class DailySelectionController {
    @Resource
    private IDailySelectionService iDailySelectionService;

    /**
     * 查询每日精选
     * @author 马会春
     * @return
     */
    @WebLog("查询每日精选")
    @RequestMapping(value = "getDailySelection",method = RequestMethod.POST)
    public Result getDailySelection(){
        List<Map<String, Object>> list = iDailySelectionService.selectDailySelection();
        return ResultGenerator.genPagingResult(new Long(list.size()),list);
    }
}
