package org.dromara.daxpay.service.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvGatewayPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class IsvGatewayPayConfigManager extends BaseManager<IsvGatewayPayConfigMapper, IsvGatewayPayConfig> {

    /**
     * 查询配置
     */
    public Optional<IsvGatewayPayConfig> findFirst() {
        return this.findAll().stream().findFirst();
    }
}
