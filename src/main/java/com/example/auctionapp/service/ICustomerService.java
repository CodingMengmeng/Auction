package com.example.auctionapp.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.SuperControl;
import com.example.auctionapp.core.TokenModel;
import com.example.auctionapp.entity.AuctionValue;
import com.example.auctionapp.entity.Customer;
import com.example.auctionapp.entity.ext.CustomerExt;
import com.example.auctionapp.vo.BadgeLevelVo;
import com.example.auctionapp.vo.BidInfoVo;
import com.example.auctionapp.vo.CustomerDataVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * 登录
     *
     * @param customer
     * @return
     */
    SuperControl loginCustomer(Customer customer);

    SuperControl loginSMS(CustomerExt customer);

    /**
     * 注册
     *
     * @param customer
     * @return
     */
    SuperControl registerCustomer(CustomerExt customer);

    /**
     * 忘记密码
     *
     * @param customer
     * @return
     */
    SuperControl forgetPwd(CustomerExt customer);

    /**
     * 获取用户和用户账户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    Map selectCustomerAndAccount(int id);

    /**
     * 获取用户和用户账户信息
     *
     * @param id 用户id
     * @return 用户信息
     * @author MaHC
     */
    CustomerDataVo selectCustomerDataAndAccount(Integer id);

    /**
     * 查询出客户的 {参拍总和,关注总和,历史浏览总和,优惠券总和}
     *
     * @param id 客户id
     * @return {参拍总和,关注总和,历史浏览总和,优惠券总和}
     * @author 孔邹祥
     */
    Map selectCustomerGoodsSketch(int id);

    /**
     * 根据用户id查询出订单的状态条数
     *
     * @param id 客户id
     * @return 订单的状态条数
     */
    Map selectOrderInfo(int id);


    /**
     * 规则:根据用户浏览记录的商品分组获取最多的分类,根据父分类查询出商品
     *
     * @param page page
     * @param id   用户id
     * @return
     */
    Page selectGuessYouLike(Page page, int id);


    /**
     * 修改
     *
     * @param customer
     * @return
     */
    Result update(Customer customer);

    /**
     * 实名认证
     *
     * @param customer 客户实体类
     * @return 成功标识
     */
    Result updateRealNameAuthen(Customer customer);


    /**
     * 验证实名
     *
     * @param realName
     * @param identityCard
     * @param
     * @return
     */
    Result selectVerifyAuthen(String realName, Integer customerId, String identityCard);

    /**
     * 验证验证码
     *
     * @param
     * @param code
     * @return
     */
    Result selectVerifyCode(String key,String code);

    /**
     * 忘记支付密码
     *
     * @param customerId
     * @param isPayPassword
     * @param password
     * @return
     */
    Result updatePayPassword(int customerId, boolean isPayPassword, String password);

    /**
     * 查询是否已实名
     *
     * @param customerId
     * @return
     */
    Result isPayPassword(int customerId);

    /**
     * 验证支付密码输入是否正确
     * @param customerId
     * @param password
     * @return
     */
    Result selectVerifyPayPass(int customerId, String password);

    /**
     * 根据微信openId查询出客户,如果没有绑定直接登录
     * @param openId
     * @return
     */
    Result selectByOpenId(String openId);

    /**
     * 公众号授权登录
      * @return
     */
    SuperControl loginByUnionId(JSONObject json);

    /**
     * 微信注册
     * @param customer
     * @return
     */
    Result insertWXRegister(Customer customer);

    /**
     * 邀请注册
     *
     * @param customer
     * @return
     */
    Customer inviteRegister(Customer customer);

    /**
     * 获取用户列表
     * @param ids
     * @return
     */
    List<Customer> getCustomerListByIds(List<Integer> ids);

    boolean isBidSuccessProxy(int subjectId, BigDecimal askedPrice, BigDecimal proportion);
    Map<Integer,List<Object>> getCustomerBadgeInfosProxy(int subjectId);
    int updateCustomerBadgeBeansProxy(int subjectId);
    AuctionValue insertRecordToAuctionValue();
    String updateOrInsertAuctionValueDataProxy(Integer subjectId,BidInfoVo bidInfoVo);
}
