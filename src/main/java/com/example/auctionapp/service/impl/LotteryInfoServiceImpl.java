package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.dao.LotteryInfoMapper;
import com.example.auctionapp.entity.ActivityInfo;
import com.example.auctionapp.entity.CouponManage;
import com.example.auctionapp.entity.LotteryInfo;
import com.example.auctionapp.service.*;
import com.example.auctionapp.util.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 抽奖信息表 服务实现类
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Service
public class LotteryInfoServiceImpl extends ServiceImpl<LotteryInfoMapper, LotteryInfo> implements ILotteryInfoService {


    @Autowired
    private LotteryInfoMapper lotteryInfoMapper;

    @Autowired
    private IActivityInfoService activityInfoService;

    @Autowired
    private ICouponManageService couponManageService;

    @Autowired
    private IRedisService redisService;

    /**
     * 查询分页信息
     *
     * @param startNum
     * @param pageSize
     * @param lotteryInfo
     * @return
     */
    @Override
    public Map<String, Object> pageList(int startNum, int pageSize, LotteryInfo lotteryInfo){
        Map<String, Object> resultMap = new HashMap<>();

        Page<LotteryInfo> page = new Page<LotteryInfo>();
        QueryWrapper<LotteryInfo> wrapper = new QueryWrapper<LotteryInfo>();
        // 当前页
        page.setCurrent(startNum);
        // 每页显示数量
        page.setSize(pageSize);

        //活动主键
        if(lotteryInfo.getActivityId() !=null ){
            wrapper.eq("activity_id", lotteryInfo.getActivityId());
        }

        //客户主键
        if(lotteryInfo.getCustomerId() !=null ){
            wrapper.eq("customer_id", lotteryInfo.getCustomerId());
        }

        //分页查询
        IPage<LotteryInfo> pageList = lotteryInfoMapper.selectPage(page, wrapper);
        List<LotteryInfo> resultList= pageList.getRecords();

        resultMap.put("rows", resultList);
        resultMap.put("total", pageList.getTotal());

        return  resultMap;
    }

    /**
     * 营销活动活动 开宝箱抽奖
     * @param userId
     * @param activityCode
     * @return
     */
    @Override
    public Result doLottery(Integer userId, String activityCode){
        //step1 判断活动是否有效
        ActivityInfo activityInfo= activityInfoService.getByCode(activityCode);

        if(activityInfo==null){
            Result.errorInfo("活动不存在");
        }
        LocalDateTime nowTime=LocalDateTime.now();
        //如果活动已经开启，判断当前时间是否在活动的时间范围内
        if(ActivityInfo.activityFlag1.equals(activityInfo.getActivityFlag())){
            //如果当前时间不在开启时间之后 或者 当前时间不在失效时间之前 则认为当前活动没开启获取理解已经失效
            if(!nowTime.isAfter(activityInfo.getStartTime()) || ! nowTime.isBefore(activityInfo.getExpireTime())){
                activityInfo.setActivityFlag(ActivityInfo.activityFlag0);
            }
        }

        //step2 查询抽奖类型的优惠券
        List<CouponManage> couponManageList=couponManageService.getActivityCouponByUser(userId,ICouponsService.couponsType4);
        if(couponManageList ==null || couponManageList.size()<=0){
            return Result.errorInfo("优惠券不够，请集满优惠再来抽奖");
        }

        //使用优惠券
        CouponManage couponManage=couponManageList.get(0);
        boolean flag= couponManageService.useCoupons(couponManage.getId());

        //step3 保存抽奖信息
        //保存抽奖休息
        LotteryInfo lotteryInfo=null;
        if(flag){
            lotteryInfo=new LotteryInfo();
            lotteryInfo.setActivityId(activityInfo.getId()); //活动ID
            lotteryInfo.setCouponsId(couponManage.getId());
            lotteryInfo.setCustomerId(userId);
            lotteryInfo.setFlag("0");
            lotteryInfo.setType("0");
            lotteryInfo.setCreateTime(LocalDateTime.now());
            long seqStr=redisService.getIncrId(); // 获取redis 自增长ID
            int seqLen=String.valueOf(seqStr).length(); //获取ID长度
            lotteryInfo.setExpiryCode(seqStr+CodeUtils.getRandomNumbernStr(12-seqLen)); //自增长 + 6位随机数
            lotteryInfo.setVersion(0);
            flag=this.save(lotteryInfo);

            if(flag){
                //添加实时活动排行榜
                QueryWrapper<LotteryInfo> wrapper = new QueryWrapper<LotteryInfo>();
                wrapper.eq("customer_id",lotteryInfo.getCustomerId());
                wrapper.eq("activity_id",lotteryInfo.getActivityId());
                Integer userActivityCount=lotteryInfoMapper.selectCount(wrapper);

                //设置排行信息
                redisService.setActivityZadd(activityCode,lotteryInfo.getCustomerId(),userActivityCount);
            }
        }
        return Result.success(lotteryInfo.getExpiryCode());
    }
}
