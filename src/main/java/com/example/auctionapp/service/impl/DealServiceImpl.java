package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.DealMapper;
import com.example.auctionapp.service.IDealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class DealServiceImpl implements IDealService {

    @Resource private DealMapper dealMapper;
    /*
     * 根据拍品id查询排中相关参数
     * */
    public Map<String, Object> getGoodsDealParamById(String auctionGoodsId){
        System.out.println("service:"+auctionGoodsId);
        return dealMapper.selectAuctionMinParam(auctionGoodsId);
    }
    /*
     * 是否拍中函数
     * */
    public boolean isConclued(String auctionGoodsId){
        //1、参拍人数>=最低参拍人数（拍品表）
        int ActualPeopleNum =  dealMapper.selectActualPeopleNum(auctionGoodsId);
        Map<String, Object> auctionMinParam = dealMapper.selectAuctionMinParam(auctionGoodsId);
        if(auctionMinParam!=null){
            int minPeopleNum = (int)auctionMinParam.get("min_number_people");
            if(ActualPeopleNum<minPeopleNum){
                return false;
            }
           //2、出价在成交区间内


        }else{

        }
    }

}
