package org.dromara.daxpay.channel.douyin.service.direct;

import org.dromara.daxpay.channel.douyin.convert.direct.DouyinDirectKeyConfigConvert;
import org.dromara.daxpay.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import org.dromara.daxpay.channel.douyin.dao.direct.DouyinDirectKeyConfigManager;
import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import org.dromara.daxpay.channel.douyin.param.direct.DouyinDirectKeyConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 抖音直连密钥配置
///
/// 管理直连商户维度的密钥配置，查询时不存在则创建默认记录，保存时合并敏感字段。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectKeyConfigService {

    private final DouyinDirectKeyConfigManager douyinDirectKeyConfigManager;
    private final DouyinDirectChannelMerchantManager douyinDirectChannelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public DouyinDirectKeyConfig findByChannelMchNo(String channelMchNo) {
        var existing = douyinDirectKeyConfigManager.findByChannelMchNo(channelMchNo);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new DouyinDirectKeyConfig()
                .setChannelMchNo(channelMchNo);
        // 查询通道商户记录填充商户号
        douyinDirectChannelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        douyinDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(DouyinDirectKeyConfigParam param) {
        var config = this.findByChannelMchNo(param.getChannelMchNo());
        config.setMchNo(param.getMchNo());
        DouyinDirectKeyConfigConvert.CONVERT.copy(param, config);
        douyinDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        douyinDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
