package cn.daxpay.open.channel.adapay.service.direct;

import cn.daxpay.open.channel.adapay.dao.direct.AdapayDirectKeyConfigManager;
import cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig;
import cn.daxpay.open.channel.adapay.param.direct.AdapayDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Adapay 直连通道商户管理
///
/// 管理通道商户绑定的创建/删除。
/// 创建时仅录入商户名称与所属产品, Adapay 应用 ID/密钥由密钥配置单独维护,
/// 沙箱环境运行时读取支付产品配置(pay_md_product_config.activeEnv)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final AdapayDirectKeyConfigManager adapayDirectKeyConfigManager;

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表和Adapay 直连配置表。
    /// adapayAppId/apiKey/privateKey/publicKey 由密钥配置单独维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(AdapayDirectChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("ADAPAY");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写Adapay 直连配置(merchantNo 创建时录入不可修改, adapayAppId/apiKey/privateKey/publicKey 由密钥配置单独维护)
        var keyConfig = new AdapayDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setMerchantNo(param.getMerchantNo());
        keyConfig.setMchNo(param.getMchNo());
        adapayDirectKeyConfigManager.save(keyConfig);
    }

    /// 根据通道商户号删除(级联删除直连配置)
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        adapayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
