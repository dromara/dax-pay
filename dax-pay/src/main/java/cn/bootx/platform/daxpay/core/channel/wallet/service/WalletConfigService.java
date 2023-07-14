package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletConfigManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钱包配置
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletConfigService {
    private final WalletConfigManager walletConfigManager;


}
