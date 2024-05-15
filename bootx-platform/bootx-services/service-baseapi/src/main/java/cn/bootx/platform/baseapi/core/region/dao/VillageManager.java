package cn.bootx.platform.baseapi.core.region.dao;

import cn.bootx.platform.baseapi.core.region.entity.Village;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 村庄/社区
 *
 * @author xxm
 * @since 2023/2/3
 */
@Repository
@RequiredArgsConstructor
public class VillageManager extends BaseManager<VillageMapper, Village> {

    public List<Village> findAllByStreetCode(String streetCode) {
        return findAllByField(Village::getStreetCode, streetCode);
    }

}
