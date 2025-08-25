package org.dromara.daxpay.service.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.merchant.entity.gateway.CashierGroupConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网关收银台分组配置
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierGroupConfigManager extends BaseManager<CashierGroupConfigMapper, CashierGroupConfig> {

    /**
     * 查询指定类型的分组, 并进行排序
     */
    public List<CashierGroupConfig> findAllByAppIdAndType(String appId, String gatewayType){
        return lambdaQuery()
                .eq(CashierGroupConfig::getAppId, appId)
                .eq(CashierGroupConfig::getCashierType, gatewayType)
                .orderByAsc(CashierGroupConfig::getSortNo)
                .list();
    }

    /**
     * 判断指定类型的分组是否存在
     */
    public boolean existedByType(String gatewayType,String appId) {
        return lambdaQuery()
                .eq(CashierGroupConfig::getCashierType, gatewayType)
                .eq(CashierGroupConfig::getAppId, appId)
                .exists();
    }
}
