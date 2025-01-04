package org.dromara.daxpay.service.dao.allocation;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.entity.allocation.AllocConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 分账配置
 * @author xxm
 * @since 2024/10/6
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocConfigManager extends BaseManager<AllocConfigMapper, AllocConfig> {

    /**
     * 判断是都已经存在数据
     */
    public boolean existsByAppId(String appId) {
        return existedByField(MchAppBaseEntity::getAppId, appId);
    }

    /**
     * 根据应用ID进行查询
     */
    public Optional<AllocConfig> findByAppId(String appId) {
        return findByField(MchAppBaseEntity::getAppId, appId);
    }
}
