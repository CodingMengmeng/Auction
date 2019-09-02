package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.Message;
import com.example.auctionapp.entity.ext.MessageRet;
import com.example.auctionapp.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-08
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IMessageService messageService;

    /**
     * 查询消息列表
     * @param message
     * @author 马会春
     * @return
     */
    @WebLog("查询消息列表接口")
    @RequestMapping(value = "/getMessage",method = RequestMethod.POST)
    public Result getMessage(@RequestBody Message message, Integer page,Integer rows){
        if (message.getSubjectId() == null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        if (page == null || rows == null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        Paging paging = new Paging(page,rows);
        IPage<Map<String, Object>> messageObj = messageService.getMessageObj(message,paging);
        return ResultGenerator.genPagingResult(messageObj.getTotal(), messageObj.getRecords());
    }

    /**
     * 获取当前和前七天的日期
     * 获取每天的浏览数量
     * @param message
     * @Author 马会春
     * @return
     */
    @WebLog("获取当前加前七天的日期和每天的浏览数量")
    @RequestMapping(value = "/getDate",method = RequestMethod.POST)
    public Result getDate(@RequestBody Message message){
        if (message.getSubjectId()==null){
            return  ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        List<MessageRet> list = messageService.getDate(message);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 查看系统消息
     * @return
     * @author 马会春
     */
    @WebLog("查询系统消息列表接口")
    @RequestMapping(value = "/getSystemMessageList",method = RequestMethod.POST)
    public Result getSystemMessageList(@RequestBody Paging paging){
        if (paging.getPage() == null || paging.getRows() == null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        IPage<Message> systemMessageList = messageService.getSystemMessageList(paging);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Message m:systemMessageList.getRecords()) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("messageInfo",m.getMessageInfo());
            list.add(map);
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * @author 马会春
     * @return
     */
    @WebLog("拍品结束后获取出价详情")
    @RequestMapping(value = "/getPriceDetail",method = RequestMethod.POST)
    public Result getPriceDetail(@RequestBody Message message,Integer page,Integer rows){
        if (message.getSubjectId() == null){
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        if (page == null || rows==null){
            return ResultGenerator.genSuccessMEssageResult("分页错误");
        }
        /**
         * 加价历史表中获取当前拍品的参拍者 Id
         * 客户表中获取信息和最大出价
         */
        IPage iPage = new Page(page,rows);
        IPage<Message> priceDetail = messageService.getPriceDetail(iPage, message);
        return ResultGenerator.genSuccessResult(priceDetail.getRecords());
    }
}
