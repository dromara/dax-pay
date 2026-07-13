package cn.daxpay.open.channel.ums.service.direct;

import cn.daxpay.open.channel.ums.convert.direct.UmsDirectKeyConfigConvert;
import cn.daxpay.open.channel.ums.dao.direct.UmsDirectKeyConfigManager;
import cn.daxpay.open.channel.ums.entity.direct.UmsDirectKeyConfig;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectKeyConfigParam;
import cn.daxpay.open.payment.channel.dao.ChannelMerchantManager;
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
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public UmsDirectKeyConfig findByChannelMchNo(String channelMchNo, boolean sandbox) {
        var existing = umsDirectKeyConfigManager.findByChannelMchNoAndSandbox(channelMchNo, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new UmsDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setSandbox(sandbox);
        // 查询通用通道商户主表填充商户号
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        umsDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段(terminalNo/umsAppId/appKey/secretKey);
    /// mchNo/merchantNo 为不可变身份字段(实体 FieldStrategy.NEVER), 由 findByChannelMchNo 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(UmsDirectKeyConfigParam param) {
        var config = this.findByChannelMchNo(param.getChannelMchNo(), Boolean.TRUE.equals(param.getSandbox()));
        UmsDirectKeyConfigConvert.CONVERT.copy(param, config);
        umsDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        umsDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
