package com.warape.aimechanician.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warape.aimechanician.entity.ExchangeCardDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 兑换卡详情 Mapper 接口
 * </p>
 *
 * @author warape
 * @since 2023-04-05 04:51:24
 */
@Mapper
public interface ExchangeCardDetailMapper extends BaseMapper<ExchangeCardDetail> {

  int consume (@Param("userId") Long userId, @Param("consumeCount") Integer consumeCount);

  int sumSurplusCount (@Param("userId") Long userId);
}
