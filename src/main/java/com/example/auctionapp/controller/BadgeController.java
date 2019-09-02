package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.service.IBadgeService;
import com.example.auctionapp.vo.BadgeValueVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 客户 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Api(value = "/badge/", tags = "徽章Api")
@RestController
@RequestMapping("/badge")
@Slf4j
public class BadgeController {

    @Resource
    private IBadgeService badgeService;

    @WebLog("徽章页面 - 等级查询")
    @PostMapping("getBadgeAndCustomer")
    @ApiOperation(value = "徽章页面 - 等级查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id",dataType = "Integer"),
            @ApiImplicitParam(name = "badgeId", value = "徽章id 1-好友徽章  2-贡献徽章",dataType = "Integer"),
    })
    public Result<BadgeValueVo> getBadgeAndCustomer(@RequestHeader("userId")Integer customerId,
                                                    @RequestParam("badgeId")Integer badgeId){
        if (customerId==null || badgeId==null){
            return Result.errorInfo("参数错误");
        }
        List<BadgeValueVo> badgeValueVos = badgeService.selectBadgeAndCustomer(badgeId, customerId);
        log.info("徽章页面返回参数-->{}",badgeValueVos);
        return Result.success(badgeValueVos);
    }
}
