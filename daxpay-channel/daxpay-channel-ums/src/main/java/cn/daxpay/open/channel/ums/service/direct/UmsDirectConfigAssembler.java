package cn.daxpay.open.channel.ums.service.direct;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.entity.direct.UmsDirectKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取 product 与固化 sandbox 标识,
/// 从直连配置([UmsDirectKeyConfig])读取商户身份(mid/tid) 与签名密钥(appId/appKey/secretKey),
/// 组装为下发给子应用的通道调用凭证 [UmsSdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [ChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择对应环境的密钥与网关地址。
///
/// 银联商务为聚合支付, 不区分应用能力(capability 参数保留但不使用)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectConfigAssembler {

    private final UmsDirectKeyConfigService keyConfigService;
    private final ChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(UMS 不使用, 保留对齐签名)
    /// @return 银联商务 SDK 凭证
    public UmsSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(取 sandbox 固化标识)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 2. 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 3. 直连配置(商户身份 mid/tid + 密钥 appId/appKey/secretKey, 按 sandbox 区分)
        UmsDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo, sandbox);

        // 4. 组装凭证
        var credential = new UmsSdkCredential();
        credential.setUmsAppId(keyConfig.getUmsAppId());
        credential.setAppKey(keyConfig.getAppKey());
        credential.setMerchantNo(keyConfig.getMerchantNo());
        credential.setTerminalNo(keyConfig.getTerminalNo());
        credential.setSecretKey(keyConfig.getSecretKey());
        credential.setSandbox(sandbox);
        return credential;
    }
}
