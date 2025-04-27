package org.dromara.daxpay.service.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.gateway.AggregatePayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AggregatePayConfigManager extends BaseManager<AggregatePayConfigMapper, AggregatePayConfig> {

    /**
     * 查询配置
     */
    public List<AggregatePayConfig> findAllByAppId(String appId) {
        return lambdaQuery()
                .eq(AggregatePayConfig::getAppId, appId)
                .list();
    }

    /**
     * 根据应用ID和类型查询配置
     */
    public Optional<AggregatePayConfig> findByAppIdAndType(String appId, String type) {
        return lambdaQuery()
                .eq(AggregatePayConfig::getAppId, appId)
                .eq(AggregatePayConfig::getAggregateType, type)
                .oneOpt();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type){
        return lambdaQuery()
                .eq(AggregatePayConfig::getAppId, appId)
                .eq(AggregatePayConfig::getAggregateType, type)
                .exists();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type, Long id){
        return lambdaQuery()
                .eq(AggregatePayConfig::getAppId, appId)
                .eq(AggregatePayConfig::getAggregateType, type)
                .ne(AggregatePayConfig::getId, id)
                .exists();
    }
}
