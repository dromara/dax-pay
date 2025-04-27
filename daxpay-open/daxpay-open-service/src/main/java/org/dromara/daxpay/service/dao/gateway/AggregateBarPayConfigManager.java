package org.dromara.daxpay.service.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.gateway.AggregateBarPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网关聚合付款码支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AggregateBarPayConfigManager extends BaseManager<AggregateBarPayConfigMapper, AggregateBarPayConfig> {

    /**
     * 查询配置
     */
    public List<AggregateBarPayConfig> findAllByAppId(String appId) {
        return lambdaQuery()
                .eq(AggregateBarPayConfig::getAppId, appId)
                .list();
    }

    /**
     * 根据应用ID和类型查询配置
     */
    public List<AggregateBarPayConfig> findByAppIdAndType(String appId, String barPayType) {
        return lambdaQuery()
                .eq(AggregateBarPayConfig::getAppId, appId)
                .eq(AggregateBarPayConfig::getBarPayType, barPayType)
                .list();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type){
        return lambdaQuery()
                .eq(AggregateBarPayConfig::getAppId, appId)
                .eq(AggregateBarPayConfig::getBarPayType, type)
                .exists();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type, Long id){
        return lambdaQuery()
                .eq(AggregateBarPayConfig::getAppId, appId)
                .eq(AggregateBarPayConfig::getBarPayType, type)
                .ne(AggregateBarPayConfig::getId, id)
                .exists();
    }
}
