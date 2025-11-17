package org.dromara.daxpay.payment.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.gateway.CheckoutCounterConfig;
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
public class CheckoutCounterManager extends BaseManager<CheckoutCounterMapper, CheckoutCounterConfig> {

    /**
     * 根据收银台类型和应用号查询
     */
    public List<CheckoutCounterConfig> findAllByAppIdAndType(String appId, String type) {
        return lambdaQuery()
                .eq(CheckoutCounterConfig::getAppId, appId)
                .eq(CheckoutCounterConfig::getType, type)
                .orderByAsc(CheckoutCounterConfig::getSortNo)
                .orderByDesc(CheckoutCounterConfig::getId)
                .list();
    }

}
