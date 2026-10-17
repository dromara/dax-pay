package cn.daxpay.open.channel.longpay.service.direct;

import cn.daxpay.open.channel.longpay.client.credential.LongpaySdkCredential;
import cn.daxpay.open.channel.longpay.dao.direct.LongpayDirectKeyConfigManager;
import cn.daxpay.open.channel.longpay.entity.direct.LongpayDirectKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取固化 sandbox 标识,
/// 从直连配置([LongpayDirectKeyConfig])读取建行商户三要素与两把公钥,
/// 组装为下发给子应用的通道调用凭证 [LongpaySdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [ChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayDirectConfigAssembler {

    private final LongpayDirectKeyConfigManager longpayDirectKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(建行不使用, 保留对齐签名)
    /// @return 建行龙支付 SDK 凭证
    public LongpaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(取 sandbox 固化标识)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 2. 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 3. 直连配置(商户三要素 + 两把公钥, 按 sandbox 取对应环境密钥)
        LongpayDirectKeyConfig keyConfig = longpayDirectKeyConfigManager.findByChannelMchNoAndSandbox(channelMchNo, sandbox)
                .orElseGet(() -> {
                    // 不存在则按默认记录落库后返回(对齐密钥配置服务语义)
                    LongpayDirectKeyConfig config = new LongpayDirectKeyConfig()
                            .setChannelMchNo(channelMchNo)
                            .setSandbox(sandbox);
                    // mchNo 继承自 MchBaseEntity, 链式返回父类型, 单独赋值
                    config.setMchNo(mchNo);
                    longpayDirectKeyConfigManager.save(config);
                    return config;
                });

        // 4. 组装凭证
        var credential = new LongpaySdkCredential();
        credential.setMerchantId(keyConfig.getMerchantId());
        credential.setPosId(keyConfig.getPosId());
        credential.setBranchId(keyConfig.getBranchId());
        credential.setMerchantPublicKey(keyConfig.getMerchantPublicKey());
        credential.setCcbPublicKey(keyConfig.getCcbPublicKey());
        credential.setSandbox(sandbox);
        return credential;
    }
}
