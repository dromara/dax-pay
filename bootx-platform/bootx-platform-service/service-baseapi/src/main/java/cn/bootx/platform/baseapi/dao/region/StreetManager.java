package cn.bootx.platform.baseapi.dao.region;

import cn.bootx.platform.baseapi.entity.region.Street;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 街道表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Repository
@RequiredArgsConstructor
public class StreetManager extends BaseManager<StreetMapper, Street> {

    public List<Street> findAllByAreaCode(String areaCode) {
        return findAllByField(Street::getAreaCode, areaCode);
    }

}
