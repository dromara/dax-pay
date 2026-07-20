package cn.daxpay.open.channel.yeepay.service.direct;

import cn.daxpay.open.channel.yeepay.code.YeepayCode;
import cn.daxpay.open.channel.yeepay.dao.direct.YeepayDirectKeyConfigManager;
import cn.daxpay.open.channel.yeepay.entity.direct.YeepayDirectKeyConfig;
import cn.daxpay.open.channel.yeepay.param.direct.YeepayDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupService;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易宝直连通道商户管理
///
/// 管理通道商户绑定的创建/删除。
/// 商户身份(merchantNo/yopIsvNo)在创建时录入直连配置表,
/// 密钥(appKey/privateKey/yopPublicKey/wxAppId/wxAppSecret)由密钥配置单独维护,
/// 沙箱环境运行时读取支付产品配置(pay_md_product_config.activeEnv)。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]）。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final YeepayDirectKeyConfigManager yeepayDirectKeyConfigManager;

    /// 通道编码（对应 [ChannelEnum#YEE_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.YEE_PAY.getCode();
    }

    /// 清理指定通道商户号下易宝直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        yeepayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表和易宝直连配置表(含 merchantNo/yopIsvNo)。
    /// 密钥由密钥配置单独维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(YeepayDirectChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate(YeepayCode.CHANNEL_MCH_NO_PREFIX);
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
        // 写易宝直连配置(仅商户身份 merchantNo/yopIsvNo, 密钥由密钥配置单独维护)
        var keyConfig = new YeepayDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setMerchantNo(param.getMerchantNo())
                .setYopIsvNo(param.getYopIsvNo());
        // mchNo 继承自 MchBaseEntity, 链式返回父类型, 单独赋值
        keyConfig.setMchNo(param.getMchNo());
        yeepayDirectKeyConfigManager.save(keyConfig);
    }
}
