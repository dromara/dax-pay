package org.dromara.daxpay.platform.system.dao.region;

import org.dromara.daxpay.platform.system.entity.region.Area;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 区域表
///
@Repository
@RequiredArgsConstructor
public class AreaManager extends BaseManager<AreaMapper, Area> {

    /// 根据城市编码查询所有区县
    ///
    /// @param cityCode 城市编码
    /// @return 区县列表
    public List<Area> findAllByCityCode(String cityCode) {
        return findAllByField(Area::getCityCode, cityCode);
    }

}


