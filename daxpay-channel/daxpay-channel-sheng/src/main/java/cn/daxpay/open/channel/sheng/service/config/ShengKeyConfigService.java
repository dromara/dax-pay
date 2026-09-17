package cn.daxpay.open.channel.sheng.service.config;

import cn.daxpay.open.channel.sheng.convert.ShengKeyConfigConvert;
import cn.daxpay.open.channel.sheng.dao.ShengKeyConfigManager;
import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.channel.sheng.param.ShengKeyConfigParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 盛付通通道密钥配置
///
/// 管理通道商户维度的盛付通密钥配置, 查询时不存在则创建默认记录, 保存时合并敏感字段(空值不覆盖)。
/// 盛付通不提供集测接口, 无沙箱双环境, 每通道商户仅一条配置。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengKeyConfigService {

    private final ShengKeyConfigManager shengKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    ///
    /// 运营端写入 [cn.daxpay.open.payment.common.entity.MchBaseEntity] 必须显式 setMchNo
    /// (运营端不装载商户 PaymentContext, mchNo 从通用通道商户主表回填)。
    @Transactional(rollbackFor = Exception.class)
    public ShengKeyConfig findByChannelMchNo(String channelMchNo) {
        var existing = shengKeyConfigManager.findByChannelMchNo(channelMchNo);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new ShengKeyConfig()
                .setChannelMchNo(channelMchNo);
        // 查询通用通道商户主表填充商户号(单独赋值, 不链式)
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        shengKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询密钥配置(必填校验, 不创建记录)
    ///
    /// 只读不写: 记录不存在或关键字段(shengMchId/merchantPrivateKey/shengpayPublicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public ShengKeyConfig getByChannelMchNoForPay(String channelMchNo) {
        ShengKeyConfig config = shengKeyConfigManager.findByChannelMchNo(channelMchNo)
                // 盛付通: 通道密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.sheng.keyNotConfigured"));
        if (StrUtil.hasBlank(config.getShengMchId(), config.getMerchantPrivateKey(), config.getShengpayPublicKey())) {
            throw new BizInfoException("error.channel.sheng.keyNotConfigured");
        }
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段;
    /// mchNo/channelMchNo 为不可变身份字段(实体 FieldStrategy.NEVER), 由 findByChannelMchNo 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(ShengKeyConfigParam param) {
        var config = this.findByChannelMchNo(param.getChannelMchNo());
        ShengKeyConfigConvert.CONVERT.copy(param, config);
        shengKeyConfigManager.updateById(config);
    }
}
