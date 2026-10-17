package cn.daxpay.open.channel.llianpay.service.direct;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.entity.direct.LlianpayDirectKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 连连支付直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取 product 与固化 sandbox 标识,
/// 从直连配置([LlianpayDirectKeyConfig])读取连连商户编号/业务参数与商户私钥/连连平台公钥,
/// 组装为下发给子应用的通道调用凭证 [LlianpaySdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [ChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayDirectConfigAssembler {

    private final LlianpayDirectKeyConfigService keyConfigService;
    private final ChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(连连不使用, 保留对齐签名)
    /// @return 连连 SDK 凭证
    public LlianpaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(取 sandbox 固化标识)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 2. 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 3. 直连配置(商户身份 + 密钥, 按 sandbox 取对应环境密钥)
        LlianpayDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo, sandbox);

        // 4. 组装凭证
        var credential = new LlianpaySdkCredential();
        credential.setMchId(keyConfig.getMchId());
        credential.setSubMchid(keyConfig.getSubMchid());
        credential.setPayeeUid(keyConfig.getPayeeUid());
        credential.setBusiType(keyConfig.getBusiType());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setLlianpayPublicKey(keyConfig.getLlianpayPublicKey());
        credential.setSandbox(sandbox);
        return credential;
    }
}
