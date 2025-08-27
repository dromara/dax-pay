package cn.bootx.platform.baseapi.dao.region;

import cn.bootx.platform.baseapi.entity.region.Area;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 区域表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Repository
@RequiredArgsConstructor
public class AreaManager extends BaseManager<AreaMapper, Area> {

    public List<Area> findAllByCityCode(String cityCode) {
        return findAllByField(Area::getCityCode, cityCode);
    }

}
