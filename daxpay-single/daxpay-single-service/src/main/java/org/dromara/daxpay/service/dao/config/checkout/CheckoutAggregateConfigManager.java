package org.dromara.daxpay.service.dao.config.checkout;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutAggregateConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 收银台聚合支付配置
 * @author xxm
 * @since 2024/11/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutAggregateConfigManager extends BaseManager<CheckoutAggregateConfigMapper, CheckoutAggregateConfig> {

    /**
     * 查询配置
     */
    public List<CheckoutAggregateConfig> findAllByAppId(String appId) {
        return lambdaQuery()
                .eq(CheckoutAggregateConfig::getAppId, appId)
                .list();
    }

    /**
     * 根据应用ID和类型查询配置
     */
    public Optional<CheckoutAggregateConfig> findByAppIdAndType(String appId, String type) {
        return lambdaQuery()
                .eq(CheckoutAggregateConfig::getAppId, appId)
                .eq(CheckoutAggregateConfig::getType, type)
                .oneOpt();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type){
        return lambdaQuery()
                .eq(CheckoutAggregateConfig::getAppId, appId)
                .eq(CheckoutAggregateConfig::getType, type)
                .exists();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type, Long id){
        return lambdaQuery()
                .eq(CheckoutAggregateConfig::getAppId, appId)
                .eq(CheckoutAggregateConfig::getType, type)
                .ne(CheckoutAggregateConfig::getId, id)
                .exists();
    }
}
