package cn.daxpay.single.service.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.daxpay.single.service.code.WalletPayWay;
import cn.daxpay.single.service.core.channel.wallet.dao.WalletConfigManager;
import cn.daxpay.single.service.core.channel.wallet.entity.WalletConfig;
import cn.daxpay.single.service.core.system.config.service.PayChannelConfigService;
import cn.daxpay.single.service.param.channel.wechat.WalletConfigParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 钱包配置
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletConfigService {
    private final static Long ID = 0L;

    private final WalletConfigManager walletConfigManager;
    private final PayChannelConfigService payChannelConfigService;

    /**
     * 获取钱包配置
     */
    public WalletConfig getConfig(){
        return walletConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("钱包配置不存在"));
    }

    /**
     * 获取并校验配置
     */
    public WalletConfig getAndCheckConfig(){
        WalletConfig config = this.getConfig();
        if (!config.getEnable()){
            throw new DataNotExistException("钱包配置未启用");
        }
        return config;
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WalletConfigParam param){
        WalletConfig walletConfig = walletConfigManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,walletConfig);
        walletConfigManager.updateById(walletConfig);
    }

    public List<LabelValue> findPayWays() {
        return WalletPayWay.getPayWays()
                .stream()
                .map(e -> new LabelValue(e.getName(),e.getCode()))
                .collect(Collectors.toList());
    }
}
