package com.warape.aimechanician.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateTime;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.PaymentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Mapper
public interface PaymentInfoMapper extends BaseMapper<PaymentInfo> {

  List<TrendVO<Integer>> trend (@Param("start") DateTime start, @Param("end") Date end, @Param("payState") Integer payState);

  List<TrendVO<BigDecimal>> payAmountTrend (@Param("start") DateTime start, @Param("end") Date end);
}
