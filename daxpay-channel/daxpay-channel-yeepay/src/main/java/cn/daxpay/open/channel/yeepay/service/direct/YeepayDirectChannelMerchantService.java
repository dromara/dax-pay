package cn.daxpay.open.channel.yeepay.service.direct;

import cn.daxpay.open.channel.yeepay.code.YeepayCode;
import cn.daxpay.open.channel.yeepay.dao.direct.YeepayDirectKeyConfigManager;
import cn.daxpay.open.channel.yeepay.entity.direct.YeepayDirectKeyConfig;
import cn.daxpay.open.channel.yeepay.param.direct.YeepayDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.channel.dao.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final YeepayDirectKeyConfigManager yeepayDirectKeyConfigManager;

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

    /// 根据通道商户号删除(级联删除直连配置)
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        yeepayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
