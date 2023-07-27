package com.warape.aimechanician.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.UserInfo;
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
public interface UserInfoMapper extends BaseMapper<UserInfo> {

  List<TrendVO> trend (@Param("start") Date start, @Param("end") Date end);
}
