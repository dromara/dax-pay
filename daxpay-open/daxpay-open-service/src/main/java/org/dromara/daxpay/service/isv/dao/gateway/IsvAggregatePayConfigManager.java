package org.dromara.daxpay.service.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvAggregatePayConfig;
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
public class IsvAggregatePayConfigManager extends BaseManager<IsvAggregatePayConfigMapper, IsvAggregatePayConfig> {


    /**
     * 根据应用ID和类型查询配置
     */
    public Optional<IsvAggregatePayConfig> findByType(String type) {
        return lambdaQuery()
                .eq(IsvAggregatePayConfig::getAggregateType, type)
                .oneOpt();
    }

    /**
     * 根据服务商和类型查询配置是否存在
     */
    public boolean existsByType(String type){
        return lambdaQuery()
                .eq(IsvAggregatePayConfig::getAggregateType, type)
                .exists();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByType(String type, Long id){
        return lambdaQuery()
                .eq(IsvAggregatePayConfig::getAggregateType, type)
                .ne(IsvAggregatePayConfig::getId, id)
                .exists();
    }
}
