package cn.daxpay.open.channel.union.service.direct;

import cn.daxpay.open.channel.union.code.UnionCode;
import cn.daxpay.open.channel.union.convert.direct.UnionDirectKeyConfigConvert;
import cn.daxpay.open.channel.union.dao.direct.UnionDirectKeyConfigManager;
import cn.daxpay.open.channel.union.entity.direct.UnionDirectKeyConfig;
import cn.daxpay.open.channel.union.param.direct.UnionDirectKeyConfigParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 云闪付直连密钥配置
///
/// 管理直连商户维度的银联证书配置, 查询时不存在则创建默认记录, 保存时合并证书字段。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionDirectKeyConfigService {

    private final UnionDirectKeyConfigManager unionDirectKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public UnionDirectKeyConfig findByChannelMchNo(String channelMchNo, boolean sandbox) {
        var existing = unionDirectKeyConfigManager.findByChannelMchNoAndSandbox(channelMchNo, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new UnionDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setSignType(UnionCode.SIGN_TYPE_RSA2)
                .setCertSign(true)
                .setSandbox(sandbox);
        // 查询通用通道商户主表填充平台商户号(运营端写 MchBaseEntity 必须显式 mchNo)
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        unionDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新证书字段;
    /// mchNo/merId 为不可变身份字段(实体 FieldStrategy.NEVER), 由 findByChannelMchNo 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(UnionDirectKeyConfigParam param) {
        var config = this.findByChannelMchNo(param.getChannelMchNo(), Boolean.TRUE.equals(param.getSandbox()));
        UnionDirectKeyConfigConvert.CONVERT.copy(param, config);
        unionDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        unionDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
