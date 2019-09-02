package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.FollowRecord;
import com.example.auctionapp.service.IFollowRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
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
@Slf4j
@RestController
@RequestMapping("/followRecord")
public class FollowRecordController {
    @Resource
    private IFollowRecordService iFollowRecordService;

    /**
     * 我的关注
     * @param followRecord
     * @return
     * @author 马会春
     */
    @WebLog("我的关注")
    @RequestMapping(value = "/getFollowRecord", method = RequestMethod.POST)
    public Result getFollowRecord(@RequestBody FollowRecord followRecord){
        log.info("获取我的关注接口，接收参数,param --> {}",followRecord);
        if (followRecord.getCustomerId()==null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        List<Map<String, Object>> list = iFollowRecordService.selectFollowRecordByCustomerId(followRecord);
        return ResultGenerator.genPagingResult(new Long(list.size()), list);
    }
}
