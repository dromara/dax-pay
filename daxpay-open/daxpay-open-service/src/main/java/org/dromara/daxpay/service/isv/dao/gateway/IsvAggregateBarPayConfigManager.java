package org.dromara.daxpay.service.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvAggregateBarPayConfig;
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
public class IsvAggregateBarPayConfigManager extends BaseManager<IsvAggregateBarPayConfigMapper, IsvAggregateBarPayConfig> {


    /**
     * 根据应用ID和类型查询配置
     */
    public List<IsvAggregateBarPayConfig> findByType(String barPayType) {
        return lambdaQuery()
                .eq(IsvAggregateBarPayConfig::getBarPayType, barPayType)
                .list();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByType(String type){
        return lambdaQuery()
                .eq(IsvAggregateBarPayConfig::getBarPayType, type)
                .exists();
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByType(String type, Long id){
        return lambdaQuery()
                .eq(IsvAggregateBarPayConfig::getBarPayType, type)
                .ne(IsvAggregateBarPayConfig::getId, id)
                .exists();
    }
}
