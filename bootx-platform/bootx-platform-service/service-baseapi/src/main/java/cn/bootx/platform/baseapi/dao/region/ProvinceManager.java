package cn.bootx.platform.baseapi.dao.region;

import cn.bootx.platform.baseapi.entity.region.Province;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 省份表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Repository
@RequiredArgsConstructor
public class ProvinceManager extends BaseManager<ProvinceMapper, Province> {

}
