package cn.daxpay.open.channel.union.service.direct;

import cn.daxpay.open.channel.union.code.UnionCode;
import cn.daxpay.open.channel.union.dao.direct.UnionDirectKeyConfigManager;
import cn.daxpay.open.channel.union.entity.direct.UnionDirectKeyConfig;
import cn.daxpay.open.channel.union.param.direct.UnionDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 云闪付直连通道商户管理
///
/// 管理通道商户绑定的创建。银联商户号(merId)在创建时录入直连配置表,
/// RSA2 三证书(私钥/中级/根)由密钥配置单独维护。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final UnionDirectKeyConfigManager unionDirectKeyConfigManager;

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表和云闪付直连配置表(含银联商户号 merId)。
    /// 三证书由密钥配置单独维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(UnionDirectChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("UNIONPAY");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        // 沙箱标记从支付产品生效环境同步写入
        boolean sandbox = payProductConfigManager.isSandboxActive(param.getProduct());
        channelMerchant.setSandbox(sandbox);
        channelMerchantManager.save(channelMerchant);
        // 写云闪付直连配置(仅银联商户号 merId, 三证书由密钥配置单独维护)
        var keyConfig = new UnionDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setMerId(param.getMerId())
                .setSignType(UnionCode.SIGN_TYPE_RSA2)
                .setCertSign(true);
        // mchNo 继承自 MchBaseEntity, 链式返回父类型, 单独赋值
        keyConfig.setMchNo(param.getMchNo());
        unionDirectKeyConfigManager.save(keyConfig);
    }
}
