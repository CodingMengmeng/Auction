package com.example.auctionapp.dao;

import com.example.auctionapp.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
public interface AccountMapper extends BaseMapper<Account> {

    /**
     * 根据主题类型查询出账户
     * @param account
     * @return
     */
    Account selectBySubjectIdAndTpye(Account account);

    /**
     * 更新账户
     * @param account
     * @return
     */
    Integer updateBalanceById(Account account);

    /**
     * 修改账户余额和冻结金额，提现时用
     * @param account
     * @return
     */
    Integer updateBalanceAndFrozenById(Account account);

    /**
     * @description 根据用户编号查询用户的拍豆和赠豆
     * @author mengjia
     * @date 2019/10/11
     * @param subjectId 用户编号
     * @return 字段和值的映射
     * @throws
     **/
    Map<String,Object> selectBalanceAndWithBeansById(@Param("subjectId") Integer subjectId);
    /**
     * @description 根据用户编号查询账户表信息
     * @author mengjia
     * @date 2019/10/11
     * @param subjectId
     * @return com.example.auctionapp.entity.Account 账户表实体
     * @throws
     **/
    Account selectAccountInfoBySubjectId(@Param("subjectId") Integer subjectId);
    /**
     * @description 根据用户编号更新拍豆和赠豆
     * @author mengjia
     * @date 2019/10/11
     * @param account
     * @return java.lang.Integer
     * @throws
     **/
    Integer updateBalanceAndWithBeansById(Account account);
}
