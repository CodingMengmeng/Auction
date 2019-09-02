package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.auctionapp.dao.ActivityInfoMapper;
import com.example.auctionapp.entity.ActivityInfo;
import com.example.auctionapp.service.IActivityInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动信息表 服务实现类
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements IActivityInfoService {


    @Autowired
    @Qualifier("activityInfoMapper")
    private ActivityInfoMapper activityInfoMapper;

    /**
     * 查找所有的数据列表
     * @param activityInfo
     * @return
     */
    @Override
    public   List<ActivityInfo> getList(ActivityInfo activityInfo){
        return activityInfoMapper.getList(activityInfo);
    }

  /**
   * 查询分页信息
   *
   * @param startNum
   * @param pageSize
   * @param activityInfo
   * @return
   */
  @Override
  public Map<String, Object> pageList(int startNum, int pageSize,ActivityInfo activityInfo){
      Map<String, Object> resultMap = new HashMap<>();

      Page<ActivityInfo> page = new Page<ActivityInfo>();
      QueryWrapper<ActivityInfo> wrapper = new QueryWrapper<ActivityInfo>();
      // 当前页
      page.setCurrent(startNum);
      // 每页显示数量
      page.setSize(pageSize);

      if(StringUtils.isNotEmpty(activityInfo.getActivityName())){
          wrapper.like("activity_name", activityInfo.getActivityName());
      }
      wrapper.eq("status",1); //有效活动

      //分页查询
      IPage<ActivityInfo> pageList = activityInfoMapper.selectPage(page, wrapper);
      List<ActivityInfo> resultList= pageList.getRecords();
      resultMap.put("rows", resultList);
      resultMap.put("total", pageList.getTotal());
      return  resultMap;
    }

    /**
     * 根据编号查找活动
     * @param code
     * @return
     */
    @Override
    public  ActivityInfo getByCode(String code){
        QueryWrapper<ActivityInfo> wrapper = new QueryWrapper<ActivityInfo>();
        wrapper.eq("activity_code", code);
        ActivityInfo activityInfo=activityInfoMapper.selectOne(wrapper);
        return activityInfo;
    }

    /**
     * 营销活动 在当前时间 是否还有相同类型的 有效活动
     * @param activityType
     * @return
     */
    @Override
    public ActivityInfo getActivityInfoByTypeAndCurTime(Integer activityType){
        QueryWrapper<ActivityInfo> wrapper = new QueryWrapper<ActivityInfo>();
        //活动类型
        wrapper.eq("activity_type",activityType);
        wrapper.eq("activity_flag",1); //活动是否开启 0：否, 1：是 , 2:已开奖
        wrapper.apply(true," start_time <= now() and expire_time >= now()");
        List<ActivityInfo> list = activityInfoMapper.selectList(wrapper);
        if(list != null && list.size()>0){
            return list.get(0);
        }
        return null;
    }


    /**
     * 营销活动 在同一时间 是否还有相同类型的 有效活动
     * @param activityType
     * @param startTime
     * @return
     */
    @Override
    public ActivityInfo getActivityInfoByType(Integer activityType, LocalDateTime startTime){
        QueryWrapper<ActivityInfo> wrapper = new QueryWrapper<ActivityInfo>();
        //活动类型
        wrapper.eq("activity_type",activityType);
        wrapper.eq("activity_flag",1); //活动是否开启 0：否, 1：是 , 2:已开奖
        wrapper.lt("start_time",startTime);
        wrapper.gt("expire_time",startTime);
        List<ActivityInfo> list = activityInfoMapper.selectList(wrapper);
        if(list != null && list.size()>0){
            return list.get(0);
        }
        return null;
    }


    /**
     * 营销活动 最后一次已开奖的活动
     * @param activityType
     * @param endTime
     * @return
     */
    @Override
    public ActivityInfo getLastActivityInfoByType(Integer activityType, LocalDateTime endTime){
        QueryWrapper<ActivityInfo> wrapper = new QueryWrapper<ActivityInfo>();
        //活动类型
        wrapper.eq("activity_type",activityType);
        wrapper.eq("activity_flag",2); //活动是否开启 0：否, 1：是 , 2:已开奖
        wrapper.lt("expire_time",endTime);
        wrapper.last(true," ORDER BY expire_time desc limit 1");
        List<ActivityInfo> list = activityInfoMapper.selectList(wrapper);
        if(list != null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
