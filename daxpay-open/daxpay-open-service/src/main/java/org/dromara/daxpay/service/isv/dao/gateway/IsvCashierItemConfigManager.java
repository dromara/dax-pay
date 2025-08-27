package org.dromara.daxpay.service.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvCashierItemConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class IsvCashierItemConfigManager extends BaseManager<IsvCashierItemConfigMapper, IsvCashierItemConfig> {

    /**
     * 根据分组查询
     */
    public List<IsvCashierItemConfig> findAllByGroupId(Long groupId) {
        return lambdaQuery()
                .eq(IsvCashierItemConfig::getGroupId, groupId)
                .orderByAsc(IsvCashierItemConfig::getSortNo)
                .orderByDesc(IsvCashierItemConfig::getId)
                .list();
    }
    /**
     * 根据分组列表查询
     */
    public List<IsvCashierItemConfig> findAllByGroupIds(List<Long> groupIds) {
        return lambdaQuery()
                .in(IsvCashierItemConfig::getGroupId, groupIds)
                .orderByAsc(IsvCashierItemConfig::getSortNo)
                .orderByDesc(IsvCashierItemConfig::getId)
                .list();
    }

    /**
     * 判断分组是否有数据
     */
    public boolean existedByGroupId(Long groupId) {
        return existedByField(IsvCashierItemConfig::getGroupId, groupId);
    }

}
