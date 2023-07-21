package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletConfigManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletConfig;
import cn.bootx.platform.daxpay.core.merchant.service.MchAppService;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletConfigDto;
import cn.bootx.platform.daxpay.param.channel.wechat.WalletConfigParam;
import cn.hutool.core.bean.BeanUtil;
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
    private final MchAppService mchAppService;

    /**
     * 根据应用编码获取钱包配置
     */
    public WalletConfigDto findByMchCode(String mchCode){
        return walletConfigManager.findByMchCode(mchCode)
                .map(WalletConfig::toDto)
                .orElse(new WalletConfigDto());
    }

    /**
     * 新增或更新
     */
    public void add(WalletConfigParam param){
        // 是否有管理关系判断
        if (!mchAppService.checkMatch(param.getMchCode(), param.getMchAppCode())) {
            throw new BizException("应用信息与商户信息不匹配");
        }
        WalletConfig walletConfig = WalletConvert.CONVERT.convert(param);
        walletConfigManager.save(walletConfig);
    }

    public void update(WalletConfigParam param){
        WalletConfig walletConfig = walletConfigManager.findById(param.getId())
                .orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,walletConfig);
        walletConfigManager.updateById(walletConfig);
    }

}
