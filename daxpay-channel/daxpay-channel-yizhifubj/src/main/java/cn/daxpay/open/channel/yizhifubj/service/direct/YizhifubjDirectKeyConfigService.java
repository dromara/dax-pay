package cn.daxpay.open.channel.yizhifubj.service.direct;

import cn.daxpay.open.channel.yizhifubj.convert.direct.YizhifubjDirectKeyConfigConvert;
import cn.daxpay.open.channel.yizhifubj.dao.direct.YizhifubjDirectKeyConfigManager;
import cn.daxpay.open.channel.yizhifubj.entity.direct.YizhifubjDirectKeyConfig;
import cn.daxpay.open.channel.yizhifubj.param.direct.YizhifubjDirectKeyConfigParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 一直付直连密钥配置
///
/// 管理直连商户维度的密钥配置, 查询时不存在则创建默认记录, 保存时合并敏感字段。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjDirectKeyConfigService {

    private final YizhifubjDirectKeyConfigManager yizhifubjDirectKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    ///
    /// @param sandbox 沙箱标志(生产/沙箱双环境并存, 按环境分别存一份密钥)
    @Transactional(rollbackFor = Exception.class)
    public YizhifubjDirectKeyConfig findByChannelMchNo(String channelMchNo, boolean sandbox) {
        var existing = yizhifubjDirectKeyConfigManager.findByChannelMchNoAndSandbox(channelMchNo, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new YizhifubjDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setSandbox(sandbox);
        // 查询通用通道商户主表填充商户号
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        yizhifubjDirectKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段(服务端ID/报备序列号/商户 pfx 证书与密码/平台证书);
    /// merchantId 为不可变身份字段, 由 findByChannelMchNo 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(YizhifubjDirectKeyConfigParam param) {
        boolean sandbox = Boolean.TRUE.equals(param.getSandbox());
        var config = this.findByChannelMchNo(param.getChannelMchNo(), sandbox);
        YizhifubjDirectKeyConfigConvert.CONVERT.copy(param, config);
        yizhifubjDirectKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        yizhifubjDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
