package org.dromara.daxpay.channel.wechat.service.direct;

import org.dromara.daxpay.channel.wechat.convert.direct.WechatDirectKeyConfigConvert;
import org.dromara.daxpay.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import org.dromara.daxpay.channel.wechat.dao.direct.WechatDirectKeyConfigManager;
import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectKeyConfig;
import org.dromara.daxpay.channel.wechat.param.direct.WechatDirectKeyConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信直连密钥配置
///
/// 管理直连商户维度的密钥和证书配置，查询时不存在则创建默认记录，保存时合并敏感字段。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectKeyConfigService {

    private final WechatDirectKeyConfigManager wechatDirectKeyConfigManager;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public WechatDirectKeyConfig findByChannelMchNo(String channelMchNo) {
        var existing = wechatDirectKeyConfigManager.findByChannelMchNo(channelMchNo);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new WechatDirectKeyConfig()
                .setChannelMchNo(channelMchNo);
        // 查询通道商户记录填充商户号
        wechatDirectChannelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        wechatDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(WechatDirectKeyConfigParam param) {
        var config = this.findByChannelMchNo(param.getChannelMchNo());
        config.setMchNo(param.getMchNo());
        WechatDirectKeyConfigConvert.CONVERT.copy(param, config);
        wechatDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        wechatDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
