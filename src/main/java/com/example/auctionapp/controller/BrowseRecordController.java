package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.BrowseRecord;
import com.example.auctionapp.entity.Message;
import com.example.auctionapp.service.IBrowseRecordService;
import com.example.auctionapp.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
@RequestMapping("/browseRecord")
public class BrowseRecordController {

    @Resource
    private IBrowseRecordService iBrowseRecordService;

    /***
     * 查询客户的历史浏览
     * @param browseRecord
     * author 马会春
     * @return
     */
    @WebLog("查询客户的历史浏览")
    @RequestMapping(value = "/getBrowseRecord" ,method = RequestMethod.POST)
    public Result getBrowseRecord(@RequestBody BrowseRecord browseRecord){
        //log.info("查询客户的历史浏览接口，接收参数,param --> {}",browseRecord);
        if (browseRecord.getCustomerId()==null || browseRecord.getDay()==null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        //获取当前时间的年和月
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        //获取当前时间的日
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd"));
        //如果当前时间的日 小于传过来的时间，就把时间往前推一个月
        if (Integer.parseInt(now) < Integer.parseInt(browseRecord.getDay())){
            date = DateTimeUtil.dateBeforePostpone(new Date(),-1);
        }
        String day = date+"-"+browseRecord.getDay();
        browseRecord.setDay(day);
        List<Map<String, Object>> list = iBrowseRecordService.selectBrowseRecordByCustomerId(browseRecord);
        return ResultGenerator.genPagingResult(new Long(list.size()),list);
    }

    /**
     * 删除历史浏览记录
     * @param message
     * @return
     * @author 马会春
     */
    @WebLog("删除客户的历史浏览接口")
    @RequestMapping(value = "/removeHistory",method = RequestMethod.POST)
    public Result removeHistory(@RequestBody Message message){
        //log.info("删除客户的历史浏览接口，接收参数,param --> {}",message);
        if (message.getSubjectId()==null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        Integer integer = iBrowseRecordService.removeHistory(message);
        if (integer>0){
            return ResultGenerator.genSuccessResult("清除成功");
        }
        return ResultGenerator.genSuccessMEssageResult("失败");
    }
}
