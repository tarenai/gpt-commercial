package com.warape.aimechanician.impl;

import com.warape.aimechanician.entity.AccountLog;
import com.warape.aimechanician.mapper.AccountLogMapper;
import com.warape.aimechanician.service.AccountLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户表日志 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-04-02 05:10:18
 */
@Service
public class AccountLogServiceImpl extends ServiceImpl<AccountLogMapper, AccountLog> implements AccountLogService {

}
