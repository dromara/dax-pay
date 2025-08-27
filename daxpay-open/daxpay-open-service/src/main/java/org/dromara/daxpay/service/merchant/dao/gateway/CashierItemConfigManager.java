package org.dromara.daxpay.service.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.merchant.entity.gateway.CashierItemConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierItemConfigManager extends BaseManager<CashierItemConfigMapper, CashierItemConfig> {

    /**
     * 根据id和appId查询
     */
    public Optional<CashierItemConfig> findByIdAndAppId(Long id, String appId) {
        return lambdaQuery()
                .eq(CashierItemConfig::getId, id)
                .eq(CashierItemConfig::getAppId, appId)
                .oneOpt();
    }

    /**
     * 根据分组查询
     */
    public List<CashierItemConfig> findAllByGroupId(Long groupId) {
        return lambdaQuery()
                .eq(CashierItemConfig::getGroupId, groupId)
                .orderByAsc(CashierItemConfig::getSortNo)
                .orderByDesc(CashierItemConfig::getId)
                .list();
    }
    /**
     * 根据分组列表查询
     */
    public List<CashierItemConfig> findAllByGroupIds(List<Long> groupIds) {
        return lambdaQuery()
                .in(CashierItemConfig::getGroupId, groupIds)
                .orderByAsc(CashierItemConfig::getSortNo)
                .orderByDesc(CashierItemConfig::getId)
                .list();
    }

    /**
     * 判断分组是否有数据
     */
    public boolean existedByGroupId(Long groupId) {
        return existedByField(CashierItemConfig::getGroupId, groupId);
    }

}
