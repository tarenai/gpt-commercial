package com.warape.aimechanician.mapper;

import java.math.BigDecimal;

import com.warape.aimechanician.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 账户表 Mapper 接口
 * </p>
 *
 * @author warape
 * @since 2023-04-02 05:10:17
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

  int commonUpdateAmount (@Param("id") Long id, @Param("changeAmount") BigDecimal changeAmount, @Param("operatorName") String operatorName);
}
