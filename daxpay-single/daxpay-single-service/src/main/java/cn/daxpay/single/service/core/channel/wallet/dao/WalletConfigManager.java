package cn.daxpay.single.service.core.channel.wallet.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.single.service.core.channel.wallet.entity.WalletConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 钱包配置
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WalletConfigManager extends BaseManager<WalletConfigMapper, WalletConfig> {
}
