package com.example.auctionapp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auctionapp.core.*;
import com.example.auctionapp.dao.AccountMapper;
import com.example.auctionapp.dao.BadgeCustomerMapper;
import com.example.auctionapp.dao.CustomerMapper;
import com.example.auctionapp.entity.Account;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.BadgeCustomer;
import com.example.auctionapp.entity.Customer;
import com.example.auctionapp.entity.ext.CustomerExt;
import com.example.auctionapp.service.ICouponsService;
import com.example.auctionapp.service.ICustomerService;
import com.example.auctionapp.service.IRedisService;
import com.example.auctionapp.util.*;
import com.example.auctionapp.vo.BadgeCustomerVo;
import com.example.auctionapp.vo.BadgeLevelVo;
import com.example.auctionapp.vo.CustomerDataVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {
    @Value("${dgt.user.userId.time}")
    private String userIdTime;
    @Value("${dgt.user.token.time}")
    private String tokenTime;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private AccountMapper accountMapper;

    @Resource
    private ICouponsService iCouponsService;

    @Resource
    private IRedisService redisService;

    @Resource
    private BadgeCustomerMapper badgeCustomerMapper;

    private List<String> list = new ArrayList<String>();

    /**
     * 登录
     *
     * @param customer
     * @return
     * @author 马会春
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public SuperControl loginCustomer(Customer customer) {
        //获取前端登录的密码，经过md5加密后与数据库匹配
        Customer c = customerMapper.selectOne(new QueryWrapper<>(new Customer()).eq("phone", customer.getPhone()));
        if(c == null || StringUtils.isEmpty(c.getPassword()) ){
            return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0,"请重置密码");
        }
        if(!Md5SaltTool.verifyPwd(customer.getPassword(), c.getPassword())){
            return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0,"密码输入错误");
        }
        String s = redisService.doUserToken(c.getId());
        return SuperControl.getObject(c.getId(),s);
    }

    public static List<String> removeDuplicate(List<String> list) {
        HashSet<? extends String> h = new HashSet<String>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 短信登录
     *
     * @param customer
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public SuperControl loginSMS(CustomerExt customer) {
        if (customer.getPhone() == null) {
            return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "参数错误");
        }
        customer.setType(ProjectConstant.SENDSMS_TYPE_1);
        SuperControl superControl = checkVerificationCode(customer);
        if (superControl.getNumber() != ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_200) {
            return superControl;
        }
        //根据手机号查询，判断该手机号是否注册
        Customer c = customerMapper.selectOne(new QueryWrapper<>(new Customer()).eq("phone", customer.getPhone()));
        if (c != null) {
            //手机号已经注册，直接登录成功


            String token = redisService.doUserToken(c.getId());
            return SuperControl.getObject(c.getId(), token);
        } else {
            Customer cc = new Customer();
            cc.setPhone(customer.getPhone());
            cc.setName(com.example.auctionapp.util.StringUtils.getHeadPhoto());
            customerMapper.insert(cc);
            Account ac = new Account();
            ac.setType(1);
            ac.setSubjectId(cc.getId());
            ac.setBalance(new BigDecimal(0));
            ac.setFrozen(new BigDecimal(0));
            ac.setStatus(1);
            //添加账户
            accountMapper.insert(ac);
            //手机号已经注册，直接登录成功
            if (null == cc.getId()) {

                log.info("新增用户Id为空此时出错!,{}", cc);
                throw new RuntimeException("新增用户出错!");
            }
            String token = redisService.doUserToken(cc.getId());
            return SuperControl.getObject(cc.getId(), token);
        }
    }

    /**
     * 注册
     *
     * @param customer
     * @return
     * @author 马会春
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public SuperControl registerCustomer(CustomerExt customer) {
        Customer cOne = new Customer();
        cOne.setPhone(customer.getPhone());
        //根据手机号查询，判断该手机号是否注册
        Customer c = customerMapper.selectOne(new QueryWrapper<>(cOne).eq("phone", customer.getPhone()));
        if (c != null) {
            return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "该手机号已注册");
        }
        //检查验证码
        customer.setType(ProjectConstant.SENDSMS_TYPE_1);
        SuperControl superControl = checkVerificationCode(customer);
        //如果不等于200，则表示验证码没有通过验证
        if (!superControl.getNumber().equals(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_200)) {
            return superControl;
        }
        //注册
        Customer cc = new Customer();
        cc.setPhone(customer.getPhone());
        cc.setName(com.example.auctionapp.util.StringUtils.getHeadPhoto());
        cc.setPassword(Md5SaltTool.getCustomerPwdMd5(customer.getPassword()));
        int num = customerMapper.insert(cc);
        Account ac = new Account();
        ac.setType(1);
        ac.setSubjectId(cc.getId());
        ac.setBalance(new BigDecimal(0));
        ac.setFrozen(new BigDecimal(0));
        ac.setStatus(1);
        //添加账户
        accountMapper.insert(ac);
        if (num > 0) {
            //优惠券发放
            iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_NEWREGISTER, cc.getId());
            return SuperControl.getObject(cc.getId(), "注册成功");
        }
        return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "注册失败");
    }

    /**
     * 忘记密码
     *
     * @param customer
     * @return
     * @author 马会春
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public SuperControl forgetPwd(CustomerExt customer) {
        //检查验证码是否正确
        customer.setType(ProjectConstant.SENDSMS_TYPE_2);
        SuperControl superControl = checkVerificationCode(customer);
        //不等于200，则没有通过验证码验证，直接返回错误信息
        if (!superControl.getNumber().equals(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_200)) {
            return superControl;
        }
        //通过后，把新的密码MD5加密后，调用修改语句
        customer.setPassword(Md5SaltTool.getCustomerPwdMd5(customer.getPassword()));
        int c = customerMapper.update(new Customer().setPassword(customer.getPassword()), new QueryWrapper<>(new Customer()).eq("phone", customer.getPhone()));
        if (c > 0) {
            //修改成功后把所有信息查出来更新redis缓存
            Customer cc = customerMapper.selectOne(new QueryWrapper<>(new Customer()).eq("phone", customer.getPhone()));
            redisService.remove(ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + cc.getId());
            redisService.set(ProjectConstant.AUCTION_LOGIN_USER_ID_KEY + cc.getId(), cc, new Long(userIdTime), TimeUnit.DAYS);
            return SuperControl.getObject(cc.getId(), "重置密码成功");
        }
        return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "失败");
    }

    @Override
    public Map selectCustomerAndAccount(int id) {

        return customerMapper.selectCustomerAndAccount(id);
    }

    @Override
    public CustomerDataVo selectCustomerDataAndAccount(Integer id) {
        /**
         * 这个地方用结果映射就可以了，有点low，但是不想改了
         */
        CustomerDataVo customerDataVos = customerMapper.selectCustomerDataAndAccount(id);
        List<BadgeLevelVo> badgeLevelVos = customerMapper.selectBadgeLevelById(id);
        if (badgeLevelVos.size()==0){
            BadgeLevelVo b = new BadgeLevelVo();
            b.setLevel(0);
            badgeLevelVos.add(b);
            badgeLevelVos.add(b);
        }
        customerDataVos.setBadgeLevelVos(badgeLevelVos);
        return customerDataVos;
    }

    @Override
    public Map selectCustomerGoodsSketch(int id) {
        return customerMapper.selectCustomerGoodsSketch(id);
    }

    @Override
    public Map selectOrderInfo(int id) {

        List<Map<String, Integer>> list = customerMapper.selectOrderInfo(id);
        System.out.println(list);
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (Map<String, Integer> map : list) {

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                switch (map.get("k")) {
                    case 0:
                        resultMap.put("unpaid", map.get("v"));
                        break;
                    case 3:
                        resultMap.put("waitGoods", map.get("v"));
                        break;
                    case 4:
                        resultMap.put("waitTakeGoods", map.get("v"));
                        break;
                    case 6:
                        resultMap.put("afterBooking", map.get("v"));
                        break;
                    default:
                }
            }
        }
        return resultMap;
    }

    @Override
    public Page selectGuessYouLike(Page page, int id) {

        List<AuctionGoods> auctionGoods = customerMapper.selectGuessYouLike(page, id);
        page.setRecords(auctionGoods);
        return page;
    }

    @Override
    public Result update(Customer customer) {
        if (customer.getPhone() != null) {
            Customer phone = customerMapper.selectOne(new QueryWrapper<Customer>().eq("phone", customer.getPhone()));
            if (phone != null) {
                return Result.errorInfo("当前手机号已存在!");
            }
        }
        customerMapper.updateById(customer);
        return Result.success();
    }

    @Override
    public Result updateRealNameAuthen(Customer customer) {

        boolean b = IDCardCheckUtil.IDCardValidate(customer.getIdentityCard());
        if (!b) {
            return Result.errorInfo("身份证号错误");
        }
        customer.setVerifyStatus(1);
        return Result.success(customerMapper.updateById(customer));
    }

    @Override
    public Result selectVerifyAuthen(String realName, Integer customerId, String identityCard) {


        boolean b = IDCardCheckUtil.IDCardValidate(identityCard);
        if (!b) {
            return Result.errorInfo("身份证号错误");
        }
        return Result.success(customerMapper.updateById(new Customer()

                .setId(customerId)
                .setIdentityCard(identityCard)
                .setRealName(realName)));
    }

    @Override
    public Result selectVerifyCode(String key, String code) {
        boolean exists = redisService.exists(key);
        if (exists) {
            Object o = redisService.get(key);
            if (o.equals(code)) {
                redisService.remove(key);
                return Result.success();
            } else {
                return ResultGenerator.genSuccessMEssageResult("验证码错误!");
            }
        } else {
            return ResultGenerator.genSuccessMEssageResult("验证码以过期!");
        }
    }

    @Override
    public Result updatePayPassword(int customerId, boolean isPayPassword, String password) {

        String encryptedPwd =  Md5SaltTool.getCustomerPwdMd5(password);
        int update = accountMapper.update(new Account().setPassword(encryptedPwd), new UpdateWrapper<Account>().eq("subject_id", customerId).eq(
                "type", 1));
        return Result.success();
    }

    @Override
    public Result isPayPassword(int customerId) {

        Map map = customerMapper.selectVerifyAuthen(customerId);
        boolean isPayPassword = false;
        if (map.get("password") != null) {
            if (!"".equals(map.get("password"))) {
                isPayPassword = true;
                Map<String, Boolean> hashMap = new HashMap<>();
                hashMap.put("isPayPassword", isPayPassword);
                return Result.success(hashMap);
            }

        }
        Map<String, Boolean> hashMap = new HashMap<>();
        hashMap.put("isPayPassword", isPayPassword);
        return Result.success(hashMap);
    }

    @Override
    public Result selectVerifyPayPass(int customerId, String password) {
        String encryptedPwd =  Md5SaltTool.getCustomerPwdMd5(password);
        System.out.println(encryptedPwd);
        Map<? extends Object, ? extends Object> map = customerMapper.selectVerifyAuthen(customerId);
        if (map.get("password") != null) {
            if (map.get("password").equals(encryptedPwd)) {
                return Result.success();
            }else{
                return Result.errorInfo(Result.ResultCode.FAIL,"密码不正确!");
            }
        }
        return Result.errorInfo(Result.ResultCode.FAIL,"输入的密码不能为空!");
    }

    @Override
    public Result selectByOpenId(String unionId) {
        Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("union_id", unionId));
        if (customer != null) {
            if (customer.getOpenId() != null) {

                Customer wxLogin = selectWXLogin(customer);

                if (wxLogin != null) {
                    //取出token，返回给前端
                    String token = (String) redisService.get(ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + wxLogin.getId());
                    TokenModel t = new TokenModel(wxLogin.getId(), token);
                    Map<String, Object> map = BeanUtil.objectToMap(t);
                    map.put("isBank", true);
                    return Result.success(map);
                }
            }
        }
        HashMap<Object, Boolean> hashMap = new HashMap<>();
        hashMap.put("isBank", false);
        return Result.success(hashMap);
    }

    /**
     * 公众号授权登录
     * @return
     */
    @Override
    public SuperControl loginByUnionId(JSONObject json) {
        Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("union_id", json.getString("unionid")));
        if (customer != null){
            if(!customer.getOpenId().equals(json.getString("openid"))){
                customerMapper.updateById(new Customer()
                        .setId(customer.getId())
                        .setOpenId(json.getString("openid")));
            }
            //取出token，返回给前端
            String token = (String) redisService.get(ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + customer.getId());
            SuperControl t = new SuperControl(customer.getId(), token);
            return t;
        }
        return null;
    }

    private Customer selectWXLogin(Customer customer) {
        //根据手机号查询，判断该手机号是否注册
        Customer c = customerMapper.selectOne(new QueryWrapper<>(new Customer()).eq("phone", customer.getPhone()));
        if (c != null) {
            String token = redisService.doUserToken(c.getId());
            return c;
        }
        return null;
    }

    /**
     * 微信注册
     *
     * @param customer
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Result insertWXRegister(Customer customer) {

        //根据手机号查询，判断该手机号是否注册
        Customer c = customerMapper.selectOne(new QueryWrapper<Customer>().eq("phone", customer.getPhone()));
        if (c != null) {
            customerMapper.update(new Customer()
                            .setUnionId(customer.getUnionId())
                            .setOpenId(customer.getOpenId())
                            .setWxChannel(customer.getWxChannel())
                            .setName(customer.getName())
                            .setHeadPortrait(customer.getHeadPortrait()),
                    new QueryWrapper<Customer>().eq("id", c.getId()));
            return selectByOpenId(customer.getUnionId());
        }

        customer.setPassword(MD5Util.MD5(customer.getPassword()));
        customerMapper.insert(customer);
        Account ac = new Account();
        ac.setType(1);
        ac.setSubjectId(customer.getId());
        ac.setBalance(new BigDecimal(0.00));
        ac.setFrozen(new BigDecimal(0.00));
        ac.setStatus(1);
        //添加账户
        accountMapper.insert(ac);
        iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_NEWREGISTER, customer.getId());
        return selectByOpenId(customer.getUnionId());
    }

    /**
     * 检查验证码是否正确
     *
     * @return
     * @author 马会春
     */
    public SuperControl checkVerificationCode(CustomerExt customer) {
        boolean exists;
        if (customer.getType() == ProjectConstant.SENDSMS_TYPE_1) {
            exists = redisService.exists(ProjectConstant.REGISTER_SMS + customer.getPhone());
            if (exists) {
                String code = (String) redisService.get(ProjectConstant.REGISTER_SMS + customer.getPhone());
                if (customer.getSms().equals(code)) {
                    return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_200, "验证码正确");
                } else {
                    return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "验证码错误");
                }
            } else {
                return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "验证码已过期");
            }
        } else {
            exists = redisService.exists(ProjectConstant.PASSWORD_SMS + customer.getPhone());
            if (exists) {
                String code = (String) redisService.get(ProjectConstant.PASSWORD_SMS + customer.getPhone());
                if (customer.getSms().equals(code)) {
                    return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_200, "验证码正确");
                } else {
                    return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "验证码错误");
                }
            } else {
                return SuperControl.getObject(ProjectConstant.RECHARGEANDWITHDRAWAL_CODE_0, "验证码已过期");
            }
        }
    }

    /**
     * 邀请注册
     *
     * @param customer
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Customer inviteRegister(Customer customer) {
        //根据手机号查询，判断该手机号是否注册
        Customer c = customerMapper.selectOne(new QueryWrapper<Customer>().eq("phone", customer.getPhone()));
        if (c == null) {
            customer.setName(CodeUtils.getRandomStr(6, null));
            customerMapper.insert(customer);

            Customer c1 = customerMapper.selectOne(new QueryWrapper<Customer>().eq("phone", customer.getPhone()));

            Account ac = new Account();
            ac.setType(1);
            ac.setSubjectId(c1.getId());
            ac.setBalance(new BigDecimal(0.00));
            ac.setFrozen(new BigDecimal(0.00));
            ac.setStatus(1);
            //添加账户
            accountMapper.insert(ac);
            return c1;
        }
        return null;

    }

    /**
     * 获取用户列表
     *
     * @param ids
     * @return
     */
    @Override
    public List<Customer> getCustomerListByIds(List<Integer> ids) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Customer> list = customerMapper.selectList(queryWrapper);
        return list;
    }
    /**
     * @description 判断出价是否成功，私有函数
     * @author mengjia
     * @date 2019/9/1
     * @param subjectId 用户id，根据请求Header拿到
     * @param askedPrice    出价，根据请求参数拿到
     * @param proportion    比例，当前为5%
     * @return boolean
     * @throws
     **/
    private boolean isBidSuccess(int subjectId, BigDecimal askedPrice, BigDecimal proportion ){
        BigDecimal commission = askedPrice.multiply(proportion);
        Map<String,Object> result = accountMapper.selectBalanceAndWithBeansById(subjectId);
        BigDecimal balance;
        BigDecimal withBeans;
        if( result.get("balance") == null){
            balance = CalcUtils.ZERO;
        }else{
            balance = (BigDecimal) result.get("balance");
        }
        if(result.get("with_beans") == null){
            withBeans = CalcUtils.ZERO;
        }else{
            withBeans = (BigDecimal) result.get("with_beans");
        }
        log.info("balance:" + balance);
        log.info("withBeans:" + withBeans);
        if((balance.add(withBeans)).compareTo(commission) > -1){
            return true;
        }else{
            return false;
        }
    }
    /**
     * @description 判断出价是否成功临时测试函数
     * @author mengjia
     * @date 2019/9/1
     * @param subjectId
     * @param askedPrice
     * @param proportion
     * @return boolean
     * @throws
     **/
    @Override
    public boolean isBidSuccessProxy(int subjectId, BigDecimal askedPrice, BigDecimal proportion) {
        return isBidSuccess(subjectId,askedPrice,proportion);
    }

    /**
     * @description 获取用户的徽章信息，包括徽章等级编号、等级、豆量
     * @author mengjia
     * @date 2019/9/1
     * @param subjectId 用户Id，根据请求Header获取
     * @return Map集合，key为徽章类型（1-好友徽章；2-贡献徽章），value为{徽章等级编号,等级,豆量}
     * @throws
     **/
    private Map<Integer,List<Object>> getCustomerBadgeInfos(int subjectId){
        List<BadgeCustomerVo> badgeCustomerVos = badgeCustomerMapper.selectBadgeInfosById(subjectId);
        if(badgeCustomerVos == null || badgeCustomerVos.size() == 0){
            return null;
        }else{
            Map<Integer,List<Object>> badgeInfos = new HashMap<>();
            for(BadgeCustomerVo badgeCustomerVo : badgeCustomerVos){
                List<Object> badgeDetails = new ArrayList<>();
                badgeDetails.add(badgeCustomerVo.getEmblemId());
                badgeDetails.add(badgeCustomerVo.getLevel());
                badgeDetails.add(badgeCustomerVo.getBeans());
                badgeInfos.put(badgeCustomerVo.getId(),badgeDetails);
            }
            log.info(badgeInfos.toString());
            return badgeInfos;
        }
    }
    /**
     * @description 获取用户的徽章信息临时测试函数
     * @author mengjia
     * @date 2019/9/1
     * @param subjectId
     * @return java.util.Map<java.lang.Integer,java.util.List<java.lang.Object>>
     * @throws
     **/
    @Override
    public Map<Integer,List<Object>> getCustomerBadgeInfosProxy(int subjectId){
        return getCustomerBadgeInfos(subjectId);
    }
    private int updateCustomerctrbBadgeBeans(int emblemId,int subjectId,
                                              BigDecimal payBeans,BigDecimal currentBeans){
        BigDecimal newBeans = currentBeans.add(payBeans);
        BadgeCustomer badgeCustomer = new BadgeCustomer();
        badgeCustomer.setEmblemId(emblemId);
        badgeCustomer.setCustomerId(subjectId);
        badgeCustomer.setBeans(newBeans);
        int effectNum = badgeCustomerMapper.updateCustomerctrbBadgeBeans(badgeCustomer);
        if(effectNum != 0){
            log.info("更新" + effectNum + "条数据");
            return effectNum;
        }else{
            return 0;
        }
    }
    @Override
    public int updateCustomerCtrbBadgeBeansProxy(int subjectId){
        Map<Integer,List<Object>> customerBadgeInfo = getCustomerBadgeInfos(subjectId);
        int emblemId = (Integer) customerBadgeInfo.get(2).get(0);
        BigDecimal currentBeans = (BigDecimal) customerBadgeInfo.get(2).get(2);
        BigDecimal payBeans = new BigDecimal("50");
        return updateCustomerctrbBadgeBeans(emblemId,subjectId,currentBeans,payBeans);
    }
}
