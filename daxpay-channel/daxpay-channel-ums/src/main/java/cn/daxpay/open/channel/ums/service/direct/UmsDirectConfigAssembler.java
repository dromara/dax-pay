package cn.daxpay.open.channel.ums.service.direct;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.dao.direct.UmsDirectChannelMerchantManager;
import cn.daxpay.open.channel.ums.entity.direct.UmsDirectChannelMerchant;
import cn.daxpay.open.channel.ums.entity.direct.UmsDirectKeyConfig;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务直连通道凭证组装器
///
/// 从进件实体([UmsDirectChannelMerchant] + [UmsDirectKeyConfig])读取
/// appId / appKey / 商户号 / 终端号 / 通讯密钥, 组装为下发给子应用的通道调用凭证 [UmsSdkCredential]。
///
/// 银联商务为聚合支付, 不区分应用能力(capability 参数保留但不使用)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectConfigAssembler {

    private final UmsDirectKeyConfigService keyConfigService;
    private final UmsDirectChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(兜底定位)
    /// @param channelMchNo 通道商户号(定位密钥/商户绑定)
    /// @param capability   支付能力编码(UMS 不使用, 保留对齐签名)
    /// @return 银联商务 SDK 凭证
    public UmsSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 读取通道商户绑定(获取商户号/终端号/沙箱标志)
        UmsDirectChannelMerchant merchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.channel.ums.mchNotFound"));

        // 2. 读取密钥配置(appId/appKey/secretKey)
        UmsDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo);

        // 3. 组装凭证
        var credential = new UmsSdkCredential();
        credential.setUmsAppId(keyConfig.getUmsAppId());
        credential.setAppKey(keyConfig.getAppKey());
        credential.setMerchantNo(merchant.getMerchantNo());
        credential.setTerminalNo(merchant.getTerminalNo());
        credential.setSecretKey(keyConfig.getSecretKey());
        credential.setSandbox(merchant.isSandbox());
        return credential;
    }
}
