package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.annotation.ObjectNotNull;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.*;
import com.example.auctionapp.dao.AuctionGoodsMapper;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.AuctionValue;
import com.example.auctionapp.entity.Customer;
import com.example.auctionapp.entity.ext.CustomerExt;
import com.example.auctionapp.service.IAuctionGoodsService;
import com.example.auctionapp.service.ICouponsService;
import com.example.auctionapp.service.ICustomerService;
import com.example.auctionapp.service.IRedisService;
import com.example.auctionapp.util.CodeUtils;
import com.example.auctionapp.util.ShortMessageUtil;
import com.example.auctionapp.vo.BadgeLevelVo;
import com.example.auctionapp.vo.BidInfoVo;
import com.example.auctionapp.vo.CustomerDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 客户 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Api(value = "/customer/", tags = "客户Api")
@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {


    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IRedisService redisService;

    @Resource
    IAuctionGoodsService auctionGoodsService;

    @Resource
    AuctionGoodsMapper auctionGoodsMapper;

    @Resource
    ICouponsService iCouponsService;

    /**
     * 根据id查询出用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    @WebLog("根据id查询出用户")
    @PostMapping("selectCustomerAndAccount")
    public Result selectCustomerAndAccount(@RequestHeader("userId") Integer id) {

        Map map = customerService.selectCustomerAndAccount(id);
        return Result.success(map);
    }

    /**
     * 根据id查询出用户
     *
     * @param id 用户id
     * @return 用户信息
     * @author MaHC
     */
    @WebLog("根据id查询出用户")
    @PostMapping("getCustomerDataAndAccount")
    @ApiOperation(value = "根据客户id查询客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    public Result<CustomerDataVo> getCustomerDataAndAccount(@RequestHeader("userId") Integer id) {

        CustomerDataVo customerDataVo = customerService.selectCustomerDataAndAccount(id);
        return Result.success(customerDataVo);
    }

    /**
     * 查询出客户的 {参拍总和,关注总和,历史浏览总和,优惠券总和}
     *
     * @param id 客户id
     * @return {参拍总和,关注总和,历史浏览总和,优惠券总和}
     * @author 孔邹祥
     */
    @WebLog("查询出客户的 {参拍总和,关注总和,历史浏览总和,优惠券总和}")
    @PostMapping("selectCustomerGoodsSketch")
    public Result selectCustomerGoodsSketch(@RequestHeader("userId") int id) {
        return Result.success(customerService.selectCustomerGoodsSketch(id));
    }

    /**
     * 根据用户id查询出订单的状态条数
     *
     * @param id 客户id
     * @return 订单的状态条数
     */
    @WebLog("根据用户id查询出订单的状态")
    @PostMapping("selectOrderInfo")
    public Result selectOrderInfo(@RequestHeader("userId") int id) {
        return Result.success(customerService.selectOrderInfo(id));
    }

    /**
     * 规则:根据用户浏览记录的商品分组获取最多的分类,根据父分类查询出商品
     *
     * @param p  size
     * @param id 用户id
     * @return
     */
    @WebLog("查询出用户猜你喜欢商品")
    @PostMapping("selectGuessYouLike")
    public Result selectGuessYouLike(@RequestBody Page p, @RequestHeader("userId") int id) {

        Page page = customerService.selectGuessYouLike(p, id);
        if (page.getRecords().size() == 0) {

            List<AuctionGoods> auctionGoods = auctionGoodsMapper.selectProceedAuctionGoods(p);
            p.setRecords(auctionGoods);
            return Result.success(p);
        } else {
            return Result.success(page);
        }

    }


    /**
     * 修改
     *
     * @param customer 客户实体类
     * @return 成功标识
     */
    @WebLog("修改客户信息")
    @PostMapping("update")
    public Result update(@RequestHeader("userId") int userId, @RequestBody Customer customer) {

        customer.setId(userId);
        return customerService.update(customer);
    }


    /**
     * 实名认证
     *
     * @param customer 客户实体类
     * @return 成功标识
     */
    @PostMapping("updateRealNameAuthenNo")
    public Result updateRealNameAuthen(@RequestHeader("userId") int customerId, Customer customer) {

        customer.setId(customerId);
        return customerService.updateRealNameAuthen(customer);
    }

    /**
     * 实名认证
     *
     * @param customer 客户实体类
     * @return 成功标识
     */
    @WebLog("实名认证")
    @PostMapping("updateRealNameAuthen")
    public Result updateRealNameAuthen(@RequestHeader("userId") int customerId, String verifyCode, Customer customer) {

        boolean existsRemove = redisService.existsRemove(verifyCode);
        if (!existsRemove) {
            return Result.errorInfo("验证码错误!");
        }
        customer.setId(customerId);
        return customerService.updateRealNameAuthen(customer);
    }


    /**
     * 验证实名
     *
     * @return
     */
    @WebLog("验证实名")
    @PostMapping("selectVerifyAuthen")
    public Result selectVerifyAuthen(@RequestHeader("userId") Integer customerId, @RequestBody Map map) {
        String realName = Optional.ofNullable(map.get("realName")).orElse("").toString();
        String identityCard = Optional.ofNullable(map.get("identityCard")).orElse("").toString();

        return customerService.selectVerifyAuthen(realName, customerId, identityCard);
    }

    /**
     * 验证验证码
     *
     * @param
     * @param
     * @return
     */
    @PostMapping("selectVerifyCode")
    public Result selectVerifyCode(@RequestBody  Map map) {

        String phone = Optional.ofNullable(map.get("phone")).orElse("").toString();
        String code = Optional.ofNullable(map.get("code")).orElse("").toString();
        return customerService.selectVerifyCode(ProjectConstant.OTHERSET_SMS + phone, code);
    }

    /**
     * 忘记支付密码
     *
     * @param customerId
     * @param
     * @param
     * @return
     */
    @WebLog("忘记支付密码")
    @PostMapping("updatePayPassword")
    public Result updatePayPassword(@RequestHeader("userId") int customerId, @RequestBody Map map) {

        String password = Optional.ofNullable(map.get("password")).orElse("").toString();

        return customerService.updatePayPassword(customerId, false, password);
    }

    /**
     * @param customerId
     * @param map
     * @return
     */
    @WebLog("验证支付密码")
    @PostMapping("selectVerifyPayPass")
    public Result selectVerifyPayPass(@RequestHeader("userId") int customerId, @RequestBody Map map) {

        String password = Optional.ofNullable(map.get("password")).orElse("").toString();

        return customerService.selectVerifyPayPass(customerId, password);
    }

    /**
     * 邀请注册
     *
     * @param map map.type:{1:用户邀请,2:代理邀请,3:抽奖邀请}
     * @return
     */
    @WebLog("邀请注册")
    @PostMapping("inviteRegisterByType")
    public Result inviteRegisterByType(@RequestBody Map<String, Object> map) {
        log.info("#邀请注册 params ={} ,={}", map.toString(), "111");
        String type = Optional.ofNullable(map.get("type")).orElse("0").toString();
        Integer customerId = Integer.parseInt(Optional.ofNullable(map.get("customerId")).orElse("0").toString());
        String phone = Optional.ofNullable(map.get("phone")).orElse("").toString();
        Integer agentId = Integer.parseInt(Optional.ofNullable(map.get("agentId")).orElse("0").toString());

        Customer customer = new Customer().setPhone(phone).setAgentId(agentId);
        boolean pattern = type.matches("[1-3]");
        if (!pattern) {
            return Result.errorInfo("页面错误,缺少正确的类型!");
        }
        Customer newCustomer = customerService.inviteRegister(customer);
        if (newCustomer == null) {
            return Result.errorInfo("该手机号已注册!");
        }
        switch (type) {
            case "1":
                iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_CUSTORMERINVITE, customerId);
                iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_BEINVITEDBYCUSTOMER, newCustomer.getId());
                break;
            case "2":

                break;
            case "3":
                iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_LOTTERYINVITE, customerId);
                iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_BEINVITELOTTERY, newCustomer.getId());
                break;
            default:

        }
        return Result.success();
    }

    /**
     * 获取验证码
     *
     * @param map
     * @return
     */
    @WebLog("获取验证码")
    @PostMapping("realNameAuthenMessage")
    public Result realNameAuthenMessage(@RequestBody Map map) {
        String phone = Optional.ofNullable(map.get("phone")).orElse("").toString();
        String verifyCode = CodeUtils.getNumber4FromRandom();
        String realNameCode = ShortMessageUtil.getRequest(phone, verifyCode);
        log.info(phone + "***********" + verifyCode);
        redisService.set(ProjectConstant.OTHERSET_SMS + phone, verifyCode, 5000L * 60L,
                TimeUnit.MILLISECONDS);
        JSONObject jsonObject = JSONObject.fromObject(realNameCode);
        int errorCode = jsonObject.getInt("error_code");
        if (errorCode == 0) {
            return Result.success();
        } else {
            return ResultGenerator.genSuccessMEssageResult("发送失败,请重新发送!");
        }
    }

    /**
     * 登录
     *
     * @param customer
     * @return
     * @author 马会春
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Result login(@RequestBody Customer customer) {
        //判断参数是否为空
        if (StringUtils.isEmpty(customer.getPhone()) && StringUtils.isEmpty(customer.getPassword())) {
            return ResultGenerator.genSuccessMEssageResult("参数错误！");
        }
        //进行登录
        SuperControl superControl = customerService.loginCustomer(customer);
        if (superControl.getNumber().equals(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0)) {
            //取出token，返回给前端
            return ResultGenerator.genSuccessMEssageResult(superControl.getObj());
        }
        return ResultGenerator.genSuccessResult(new TokenModel(superControl.getNumber(), superControl.getObj()));
    }

    /**
     * 短信登录
     *
     * @return
     * @author 马会春
     */
    @WebLog("短信登录")
    @RequestMapping(value = "/user/loginSMS", method = RequestMethod.POST)
    public Result loginSMS(@RequestBody CustomerExt customer) {

        //log.info("客户短信登录接口，接收参数,param --> {}",customer);
        //判断参数是否为空
        if (customer.getPhone() == null || customer.getSms() == null) {
            return ResultGenerator.genSuccessMEssageResult("参数错误！");
        }
        SuperControl c = customerService.loginSMS(customer);
        if (c.getNumber() < 0) {
            return ResultGenerator.genSuccessMEssageResult(c.getObj());
        }
        TokenModel tokenModel = new TokenModel(c.getNumber(), c.getObj());
        return ResultGenerator.genSuccessResult(tokenModel);
    }


    /**
     * 注册
     *
     * @param customer
     * @return
     * @author 马会春
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public Result register(@RequestBody CustomerExt customer) {
        //首先判定参数不能为空
        if (StringUtils.isEmpty(customer.getPhone()) && StringUtils.isEmpty(customer.getPassword())) {
            return ResultGenerator.genSuccessMEssageResult("参数错误！");
        }
        if (customer.getPassword().length() != ProjectConstant.PASSWORD_LENGHT) {
            return ResultGenerator.genSuccessMEssageResult("密码格式不正确！");
        }
        //所有业务交由业务逻辑层，便于事物管理
        SuperControl superControl = customerService.registerCustomer(customer);
        if (!superControl.getNumber().equals(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0)) {
            String token = redisService.doUserToken(superControl.getNumber());
            return ResultGenerator.genSuccessResult(new TokenModel(superControl.getNumber(), token));
        }
        return ResultGenerator.genSuccessMEssageResult(superControl.getObj());
    }

    /**
     * 发送验证码
     *
     * @param customer
     * @return
     * @author 马会春
     */
    @WebLog("发送短信验证码")
    @RequestMapping(value = "/user/sendSMS")
    public Result sendVerificationCode(@RequestBody CustomerExt customer) {
        if (customer.getPhone() != null && (customer.getType() == ProjectConstant.SENDSMS_TYPE_1 || customer.getType() == ProjectConstant.SENDSMS_TYPE_2)) {
            //生成验证码
            String verificationCode = CodeUtils.getNumber4FromRandom();
            log.info("生成验证码为 verificationCode --> {}", verificationCode);
            //调用短信API，发送手机短信
            String result = ShortMessageUtil.getRequest(customer.getPhone(), verificationCode);
            if (customer.getType() == ProjectConstant.SENDSMS_TYPE_1) {
                redisService.set(ProjectConstant.REGISTER_SMS + customer.getPhone(), verificationCode, new Long(5), TimeUnit.MINUTES);
            } else {
                redisService.set(ProjectConstant.PASSWORD_SMS + customer.getPhone(), verificationCode, new Long(5), TimeUnit.MINUTES);
            }
            JSONObject obj = JSONObject.fromObject(result);
            log.info("短信API返回param --> {}", obj);
            if (obj.getInt(ProjectConstant.SMS_ERROR_CODE) == ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0) {
                return Result.success();
            } else {
                return ResultGenerator.genSuccessMEssageResult("发送验证码失败！");
            }
        } else {
            return ResultGenerator.genSuccessMEssageResult("参数错误！");
        }
    }

    /**
     * 忘记密码
     *
     * @param customer
     * @return
     * @author 马会春
     */
    @RequestMapping(value = "/user/forgetPwd", method = RequestMethod.POST)
    public Result forgetPwd(@RequestBody CustomerExt customer) {
        //参数不可为空
        if (StringUtils.isEmpty(customer.getPhone()) || StringUtils.isEmpty(customer.getSms()) || StringUtils.isEmpty(customer.getPassword())) {
            return ResultGenerator.genSuccessMEssageResult("参数错误！");
        }
        if (customer.getPassword().length() != ProjectConstant.PASSWORD_LENGHT) {
            return ResultGenerator.genSuccessMEssageResult("密码格式不正确！");
        }
        //把业务交给业务逻辑层
        SuperControl superControl = customerService.forgetPwd(customer);
        if (!superControl.getNumber().equals(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_200)) {
            String token = redisService.doUserToken(superControl.getNumber());
            return ResultGenerator.genSuccessResult(new TokenModel(superControl.getNumber(), token));
        }
        return ResultGenerator.genSuccessMEssageResult(superControl.getObj());
    }

    @WebLog("查询是否有支付密码")
    @PostMapping("isPayPassword")
    public Result isPayPassword(@RequestHeader("userId") int customerId) {
        return customerService.isPayPassword(customerId);
    }


    /**
     * 根据微信openId查询出客户
     *
     * @param map 微信openId
     * @return
     */
    @WebLog("根据微信openId查询出客户")
    @PostMapping("selectByOpenIdAndWXLogin")
    public Result selectByOpenIdAndWXLogin(@RequestBody Map<String, String> map) {
        log.info("方法接收参数，param-->{}", map);
        if (StringUtils.isEmpty(map.get("unionId"))) {
            return Result.errorInfo("无unionId");
        }
        return customerService.selectByOpenId(map.get("unionId"));
    }


    /**
     * 微信注册
     *
     * @param customer
     * @return
     */
    @WebLog("微信注册接口")
    @PostMapping("insertWXRegister")
    public Result insertWXRegister(@RequestBody Customer customer) {
        log.info("进入微信注册");
        if (
                StringUtils.isEmpty(customer.getOpenId()) ||
                        StringUtils.isEmpty(customer.getPhone()) ||
                        StringUtils.isEmpty(customer.getSex()) ||
                        StringUtils.isEmpty(customer.getName()) ||
                        StringUtils.isEmpty(customer.getUnionId()) ||
                        StringUtils.isEmpty(customer.getWxChannel())
        ) {
            return Result.errorInfo("参数错误");
        }
        return customerService.insertWXRegister(customer);
    }

    /**
     * @description 临时的判断出价是否成功接口
     * @author mengjia
     * @date 2019/9/1
     * @param id userId
     * @return com.example.auctionapp.core.Result
     * @throws
     **/
    @WebLog("出价是否成功接口")
    @PostMapping("isBidSuccessProxy")
    public Result isBidSuccessProxy(@RequestHeader("userId") Integer id){
        BigDecimal askedPrice = new BigDecimal("300");
        BigDecimal proportion = new BigDecimal("0.05");
        if(customerService.isBidSuccessProxy(id,askedPrice,proportion) == true){
            log.info("出价成功");
            return ResultGenerator.genSuccessResult("出价成功");
        }else{
            log.info("出价失败");
            return ResultGenerator.genSuccessResult("出价失败");
        }
    }

    /**
     * @description 临时的获取用户徽章信息接口
     * @author mengjia
     * @date 2019/9/1
     * @param id userId
     * @return com.example.auctionapp.core.Result
     * @throws
     **/
    @WebLog("获取用户的徽章信息")
    @PostMapping("getCustomerBadgeInfos")
    public Result getCustomerBadgeInfos(@RequestHeader("userId") Integer id){
        Map map = customerService.getCustomerBadgeInfosProxy(id);
        return Result.success(map);
    }

    /**
     * @description 临时的获取用户徽章信息接口
     * @author mengjia
     * @date 2019/9/1
     * @param id userId
     * @return com.example.auctionapp.core.Result
     * @throws
     **/
    @WebLog("获取用户的徽章信息")
    @PostMapping("updateCustomerctrbBadgeBeans")
    public Result updateCustomerctrbBadgeBeans(@RequestHeader("userId") Integer id){
        int affectNum = customerService.updateCustomerBadgeBeansProxy(id);
        return Result.success(affectNum);
    }

    /**
     * @description 临时新增拍卖值记录接口
     * @author mengjia
     * @date 2019/9/7
     * @param id 用户Id
     * @return com.example.auctionapp.core.Result<com.example.auctionapp.entity.AuctionValue>
     * @throws
     **/
    @WebLog("拍卖值表增加记录")
    @PostMapping("insertRecordToAuctionValue")
    public Result<AuctionValue> insertRecordToAuctionValueController(@RequestHeader("userId") Integer id){
        AuctionValue auctionValue = customerService.insertRecordToAuctionValue();
        return Result.success(auctionValue);
    }

    @WebLog("拍卖值表增加或更新记录")
    @PostMapping("updateOrInsertAuctionValueData")
    public Result updateOrInsertAuctionValueDataController(@RequestHeader("userId") Integer subjectId, @RequestBody BidInfoVo bidInfoVo){
        log.info(bidInfoVo.toString());
        String resultInfo = customerService.updateOrInsertAuctionValueDataProxy(subjectId,bidInfoVo);
        return Result.success(resultInfo);
    }
}
