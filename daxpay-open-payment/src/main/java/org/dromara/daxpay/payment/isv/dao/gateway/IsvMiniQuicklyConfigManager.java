package org.dromara.daxpay.payment.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvMiniQuicklyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 小程序快捷支付配置管理
 * @author xxm
 * @since 2025/10/10
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class IsvMiniQuicklyConfigManager extends BaseManager<IsvMiniQuicklyConfigMapper, IsvMiniQuicklyConfig> {

    /**
     * 根据服务商号查询配置
     */
    public Optional<IsvMiniQuicklyConfig> findByIsvNo(String isvNo) {
        return this.findByField(IsvMiniQuicklyConfig::getIsvNo, isvNo);
    }
}
