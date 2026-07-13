package cn.daxpay.open.channel.adapay.service.direct;

import cn.daxpay.open.channel.adapay.convert.direct.AdapayDirectKeyConfigConvert;
import cn.daxpay.open.channel.adapay.dao.direct.AdapayDirectKeyConfigManager;
import cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig;
import cn.daxpay.open.channel.adapay.param.direct.AdapayDirectKeyConfigParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Adapay 直连密钥配置
///
/// 管理直连商户维度的密钥配置, 查询时不存在则创建默认记录, 保存时合并敏感字段。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectKeyConfigService {

    private final AdapayDirectKeyConfigManager adapayDirectKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    ///
    /// @param sandbox 沙箱标志(生产/沙箱双环境并存, 按环境分别存一份密钥)
    @Transactional(rollbackFor = Exception.class)
    public AdapayDirectKeyConfig findByChannelMchNo(String channelMchNo, boolean sandbox) {
        var existing = adapayDirectKeyConfigManager.findByChannelMchNoAndSandbox(channelMchNo, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new AdapayDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setSandbox(sandbox);
        // 查询通用通道商户主表填充商户号
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        adapayDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段(adapayAppId/apiKey/privateKey/publicKey);
    /// mchNo/channelMchNo 为不可变身份字段(实体 FieldStrategy.NEVER)。
    @Transactional(rollbackFor = Exception.class)
    public void save(AdapayDirectKeyConfigParam param) {
        boolean sandbox = Boolean.TRUE.equals(param.getSandbox());
        var config = this.findByChannelMchNo(param.getChannelMchNo(), sandbox);
        AdapayDirectKeyConfigConvert.CONVERT.copy(param, config);
        adapayDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        adapayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
