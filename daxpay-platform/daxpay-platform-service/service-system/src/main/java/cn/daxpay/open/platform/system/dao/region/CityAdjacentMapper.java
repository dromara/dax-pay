package cn.daxpay.open.platform.system.dao.region;

import cn.daxpay.open.platform.system.entity.region.CityAdjacent;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 城市接壤关系
///
/// 纯关系表(无审计列), 仅供地理围栏 balanced 策略邻市容错查询。
@Mapper
public interface CityAdjacentMapper extends MPJBaseMapper<CityAdjacent> {

}
