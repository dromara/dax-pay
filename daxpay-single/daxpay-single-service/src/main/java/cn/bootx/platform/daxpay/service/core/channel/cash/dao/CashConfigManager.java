package cn.bootx.platform.daxpay.service.core.channel.cash.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.channel.cash.entity.CashConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 现金支付配置
 * @author xxm
 * @since 2024/2/17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashConfigManager extends BaseManager<CashConfigMapper, CashConfig> {
}
