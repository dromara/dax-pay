package cn.bootx.platform.baseapi.core.region.dao;

import cn.bootx.platform.baseapi.core.region.entity.City;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 城市表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Repository
@RequiredArgsConstructor
public class CityManager extends BaseManager<CityMapper, City> {

    public List<City> findAllByProvinceCode(String provinceCode) {
        return findAllByField(City::getProvinceCode, provinceCode);
    }

}
