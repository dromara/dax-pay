package cn.daxpay.open.channel.yeepay.service.direct;

import cn.daxpay.open.channel.yeepay.convert.direct.YeepayDirectKeyConfigConvert;
import cn.daxpay.open.channel.yeepay.dao.direct.YeepayDirectKeyConfigManager;
import cn.daxpay.open.channel.yeepay.entity.direct.YeepayDirectKeyConfig;
import cn.daxpay.open.channel.yeepay.param.direct.YeepayDirectKeyConfigParam;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易宝直连密钥配置
///
/// 管理直连商户维度的密钥配置, 查询时不存在则创建默认记录, 保存时合并敏感字段。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectKeyConfigService {

    private final YeepayDirectKeyConfigManager yeepayDirectKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    ///
    /// @param sandbox 沙箱标志(生产/沙箱双环境并存, 按环境分别存一份密钥)
    @Transactional(rollbackFor = Exception.class)
    public YeepayDirectKeyConfig findByChannelMchNo(String channelMchNo, boolean sandbox) {
        var existing = yeepayDirectKeyConfigManager.findByChannelMchNoAndSandbox(channelMchNo, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new YeepayDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setSandbox(sandbox);
        // 查询通用通道商户主表填充商户号
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        yeepayDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(appKey/privateKey/yopPublicKey/wxAppId/wxAppSecret);
    /// merchantNo/yopIsvNo 为不可变身份字段, 由 findByChannelMchNo 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(YeepayDirectKeyConfigParam param) {
        boolean sandbox = Boolean.TRUE.equals(param.getSandbox());
        var config = this.findByChannelMchNo(param.getChannelMchNo(), sandbox);
        YeepayDirectKeyConfigConvert.CONVERT.copy(param, config);
        yeepayDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        yeepayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
