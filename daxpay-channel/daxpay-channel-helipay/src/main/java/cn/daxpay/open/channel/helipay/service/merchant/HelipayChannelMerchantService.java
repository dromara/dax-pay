package cn.daxpay.open.channel.helipay.service.merchant;

import cn.daxpay.open.channel.helipay.code.HelipayCode;
import cn.daxpay.open.channel.helipay.param.HelipayChannelMerchantCreateParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 合利宝通道商户管理
///
/// 创建为纯建档动作: 仅写通用通道商户主表(mch_channel_merchant), 生成 HELIPAY 前缀通道商户号,
/// 不预建 helipay_key_config 行 —— 对接配置(商户编号/签名密钥/场景AppId)由密钥配置首次保存时创建。
/// 合利宝无独立沙箱网关, 沙箱标记仍按支付产品生效环境读取(实际恒为 false)。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [cn.daxpay.open.channel.helipay.strategy.merchant.HelipayChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;

    private final PayProductConfigManager payProductConfigManager;

    /// 创建合利宝通道商户(纯建档)
    ///
    /// 仅写通用通道商户主表, 生成通道商户号并从支付产品同步沙箱标记;
    /// 合利宝对接配置不在此录入, 由密钥配置后置维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(HelipayChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate(HelipayCode.CHANNEL_MCH_NO_PREFIX);
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        // 沙箱标记从支付产品生效环境同步写入, 禁止商户/表单设置
        boolean sandbox = payProductConfigManager.isSandboxActive(param.getProduct());
        channelMerchant.setSandbox(sandbox);
        channelMerchantManager.save(channelMerchant);
    }
}
