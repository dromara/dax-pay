package cn.daxpay.open.channel.ums.service.direct;

import cn.daxpay.open.channel.ums.convert.direct.UmsDirectKeyConfigConvert;
import cn.daxpay.open.channel.ums.dao.direct.UmsDirectChannelMerchantManager;
import cn.daxpay.open.channel.ums.dao.direct.UmsDirectKeyConfigManager;
import cn.daxpay.open.channel.ums.entity.direct.UmsDirectKeyConfig;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectKeyConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 银联商务直连密钥配置
///
/// 管理直连商户维度的密钥配置, 查询时不存在则创建默认记录, 保存时合并敏感字段。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectKeyConfigService {

    private final UmsDirectKeyConfigManager umsDirectKeyConfigManager;
    private final UmsDirectChannelMerchantManager umsDirectChannelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public UmsDirectKeyConfig findByChannelMchNo(String channelMchNo) {
        var existing = umsDirectKeyConfigManager.findByChannelMchNo(channelMchNo);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new UmsDirectKeyConfig()
                .setChannelMchNo(channelMchNo);
        // 查询通道商户记录填充商户号
        umsDirectChannelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        umsDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(UmsDirectKeyConfigParam param) {
        var config = this.findByChannelMchNo(param.getChannelMchNo());
        config.setMchNo(param.getMchNo());
        UmsDirectKeyConfigConvert.CONVERT.copy(param, config);
        umsDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        umsDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
