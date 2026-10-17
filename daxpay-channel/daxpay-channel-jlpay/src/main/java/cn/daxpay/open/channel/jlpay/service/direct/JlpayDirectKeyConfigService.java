package cn.daxpay.open.channel.jlpay.service.direct;

import cn.daxpay.open.channel.jlpay.convert.direct.JlpayDirectKeyConfigConvert;
import cn.daxpay.open.channel.jlpay.dao.direct.JlpayDirectKeyConfigManager;
import cn.daxpay.open.channel.jlpay.entity.direct.JlpayDirectKeyConfig;
import cn.daxpay.open.channel.jlpay.param.direct.JlpayDirectKeyConfigParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 嘉联直连密钥配置
///
/// 管理直连商户维度的密钥配置, 查询时不存在则创建默认记录, 保存时合并敏感字段。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectKeyConfigService {

    private final JlpayDirectKeyConfigManager jlpayDirectKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    ///
    /// @param sandbox 沙箱标志(生产/沙箱双环境并存, 按环境分别存一份密钥)
    @Transactional(rollbackFor = Exception.class)
    public JlpayDirectKeyConfig findByChannelMchNo(String channelMchNo, boolean sandbox) {
        var existing = jlpayDirectKeyConfigManager.findByChannelMchNoAndSandbox(channelMchNo, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new JlpayDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setSandbox(sandbox);
        // 查询通用通道商户主表填充商户号
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        // 双环境并存时身份字段(appId/mchId/termNo)从另一环境的已有记录继承, 避免沙箱记录缺身份无法调用
        jlpayDirectKeyConfigManager.findByChannelMchNo(channelMchNo)
                .ifPresent(other -> {
                    config.setAppId(other.getAppId());
                    config.setMchId(other.getMchId());
                    config.setTermNo(other.getTermNo());
                });
        jlpayDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段(SM2 私钥 + 平台公钥 + 终端号);
    /// appId/mchId 为不可变身份字段, 由 findByChannelMchNo 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(JlpayDirectKeyConfigParam param) {
        boolean sandbox = Boolean.TRUE.equals(param.getSandbox());
        var config = this.findByChannelMchNo(param.getChannelMchNo(), sandbox);
        JlpayDirectKeyConfigConvert.CONVERT.copy(param, config);
        jlpayDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        jlpayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
