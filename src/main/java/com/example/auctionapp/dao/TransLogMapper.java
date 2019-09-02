package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.TransLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.service.ApiListing;

import java.util.List;

/**
 * <p>
 * 交易 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface TransLogMapper extends BaseMapper<TransLog> {



    /**
     *
     * @param customerId
     * @param type
     * @return
     */
    List<TransLog> selectByType(Page page, @Param("customerId") int customerId, @Param("type") int type);

    /**
     * 根据订单号获取交易信息
     * @param orderNumber
     * @return
     */
    TransLog selectByOrderNumber(String orderNumber);

    /**
     * 根据订单号更新交易转态为失败
     * @param translog
     * @return
     */
    Integer updateFailureByOrderNumber(TransLog translog);

    /**
     * 根据id更新交易转态为成功
     * @param translog
     * @return
     */
    Integer updateSucessById(TransLog translog);

    /**
     * 根据订单号更新第三方支付状态
     * @param transLog
     * @return
     */
    Integer updateTransStatusByOrderNumber(TransLog transLog);


    /**
     * 查询交易后的信息
     *
     * @param transLog
     * @return
     */
    TransLog getTransNotify(@Param("transLog") TransLog transLog);

}
