package cn.daxpay.open.platform.system.dao.region;

import cn.daxpay.open.platform.system.entity.region.City;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 城市表
///
@Repository
@RequiredArgsConstructor
public class CityManager extends BaseManager<CityMapper, City> {

    /// 根据省份编码查询所有城市
    ///
    /// @param provinceCode 省份编码
    /// @return 城市列表
    public List<City> findAllByProvinceCode(String provinceCode) {
        return findAllByField(City::getProvinceCode, provinceCode);
    }

}

