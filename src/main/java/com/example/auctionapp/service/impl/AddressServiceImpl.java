package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.dao.AddressMapper;
import com.example.auctionapp.entity.Address;
import com.example.auctionapp.service.IAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 地址 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-14
 */
@Service
public class AddressServiceImpl implements IAddressService {


    @Resource
    AddressMapper addressMapper;

    @Override
    public Result insert(Address address) {

        Integer defaultFlag = address.getDefaultFlag();

        if (defaultFlag == 1) {

            addressMapper.update(new Address().setDefaultFlag(0), new QueryWrapper<Address>()
                    .eq("subject_id", address.getSubjectId())
                    .eq("type", address.getType()));
        }
        return Result.success(addressMapper.insert(address));
    }

    @Override
    public boolean delete(int id) {
        return addressMapper.deleteById(id) > 0;
    }

    @Override
    public Result update(Address address) {

        Integer defaultFlag = address.getDefaultFlag();
        if (defaultFlag == 1) {
            addressMapper.update(new Address().setDefaultFlag(0), new QueryWrapper<Address>()
                    .eq("subject_id", address.getSubjectId())
                    .eq("type", address.getType()));
        }
        return Result.success(addressMapper.updateById(address));
    }

    @Override
    public Address select(int id) {
        return addressMapper.selectById(id);
    }

    @Override
    public List<Address> selectList(int customerId, int type) {
        return addressMapper.selectList(new QueryWrapper<Address>()
                .eq("subject_id", customerId)
                .eq("type", type)
                .eq("status", 1)
                .orderByDesc("default_flag"));
    }

}
