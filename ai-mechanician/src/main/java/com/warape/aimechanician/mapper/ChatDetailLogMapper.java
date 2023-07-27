package com.warape.aimechanician.mapper;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warape.aimechanician.domain.vo.SessionRecordVo;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.ChatDetailLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 问答记录表 Mapper 接口
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Mapper
public interface ChatDetailLogMapper extends BaseMapper<ChatDetailLog> {

  List<SessionRecordVo> selectLastQuestion (@Param("userId") Long userId, @Param("chatRole") String chatRole);

  List<TrendVO> trend (@Param("start") DateTime start, @Param("end") Date end);
}
