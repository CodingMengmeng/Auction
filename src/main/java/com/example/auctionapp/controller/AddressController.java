package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.ObjectNotNull;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.Address;
import com.example.auctionapp.service.IAddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 地址 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-14
 */
@RestController
@RequestMapping("/address")
public class AddressController {


    @Resource
    IAddressService addressService;

    /**
     * 增加
     *
     * @param address
     * @return
     */
    @WebLog("新增收货地址")
    @PostMapping("insert")
    public Result insert(@RequestHeader("userId") Integer userId, @RequestBody Address address) {

        address.setSubjectId(userId);
        if (address.getSubjectId() == null) {
            return Result.errorInfo("主体不能为空!");
        }
        if (address.getType() == null) {
            return Result.errorInfo("类型不能为空!");
        }
        if (address.getConsignee() == null) {
            return Result.errorInfo("收获人不能为空!");
        }
        if (address.getPhone() == null) {
            return Result.errorInfo("手机号不能为空!");
        }
        if (address.getDefaultFlag() == null) {
            return Result.errorInfo("默认标志不能为空!");
        }

        return addressService.insert(address);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @WebLog("删除收货地址")
    @PostMapping("delete")
    public Result delete(int id) {

        return Result.isSuccess(addressService.delete(id));
    }

    /**
     * 修改
     *
     * @param address
     * @return
     */
    @WebLog("修改收货地址")
    @PostMapping("update")
    public Result update(@RequestHeader("userId") Integer customerId, @RequestBody Address address) {

        if (address.getId() == null) {
            return Result.errorInfo("id不能为空!");
        }
        address.setSubjectId(customerId);
        if (address.getSubjectId() == null) {
            return Result.errorInfo("主体不能为空!");
        }
        if (address.getType() == null) {
            return Result.errorInfo("类型不能为空!");
        }
        if (address.getConsignee() == null) {
            return Result.errorInfo("收获人不能为空!");
        }
        if (address.getPhone() == null) {
            return Result.errorInfo("手机号不能为空!");
        }
        if (address.getDefaultFlag() == null) {
            return Result.errorInfo("默认标志不能为空!");
        }
        return addressService.update(address);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @PostMapping("select")
    @WebLog("根据id查询收货地址")
    public Result select(@ObjectNotNull("id") @RequestBody Address address) {

        return Result.success(addressService.select(address.getId()));
    }


    /**
     * 根据客户id查询出所有的地址
     *
     * @param customerId
     * @param map
     * @return
     */
    @WebLog("根据客户id查询出所有的地址")
    @PostMapping("selectList")
    public Result selectList(@RequestHeader("userId") int customerId, @RequestBody Map<String, Integer> map) {

        int type = Optional.ofNullable(map.get("type")).orElse(0);

        return Result.success(addressService.selectList(customerId, type));
    }
}
