package cn.daxpay.open.platform.system.dao.region;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.system.entity.region.CityAdjacent;
import org.springframework.stereotype.Repository;

/// # 城市接壤关系
///
/// 纯关系表(无审计列), 仅供地理围栏 balanced 策略邻市容错查询;
/// 数据由 update-datas.sql 脚本机器生成导入, 运行期仅提供读取能力。
@Repository
public class CityAdjacentManager extends BaseManager<CityAdjacentMapper, CityAdjacent> {

}
