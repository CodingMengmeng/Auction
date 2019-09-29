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

    Map<String,Object> selectBalanceAndWithBeansById(@Param("subjectId") Integer subjectId);
    Account selectAccountInfoBySubjectId(@Param("subjectId") Integer subjectId);
}
