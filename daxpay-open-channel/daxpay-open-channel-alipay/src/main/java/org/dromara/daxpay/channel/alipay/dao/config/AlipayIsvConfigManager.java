package org.dromara.daxpay.channel.alipay.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付宝服务商配置Manager
 * @author xxm
 * @since 2024/11/1
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AlipayIsvConfigManager extends BaseManager<AlipayIsvConfigMapper, AlipayIsvConfig> {

    /**
     * 根据服务商号查询
     */
    public Optional<AlipayIsvConfig> findByIsvNo(String isvNo) {
        return findByField(AlipayIsvConfig::getIsvNo, isvNo);
    }
}
