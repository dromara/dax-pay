package org.dromara.daxpay.payment.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvCheckoutCounterConfig;
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
public class IsvCheckoutCounterConfigManager extends BaseManager<IsvCheckoutCounterConfigMapper, IsvCheckoutCounterConfig> {

    /**
     * 根据分组列表查询
     */
    public List<IsvCheckoutCounterConfig> findAllByIsvNoAndType(String isvNo, String type) {
        return lambdaQuery()
                .in(IsvCheckoutCounterConfig::getIsvNo, isvNo)
                .eq(IsvCheckoutCounterConfig::getType, type)
                .orderByAsc(IsvCheckoutCounterConfig::getSortNo)
                .orderByDesc(IsvCheckoutCounterConfig::getId)
                .list();
    }

}
