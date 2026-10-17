package cn.daxpay.open.channel.cpcn.service.direct;

import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.entity.direct.CpcnDirectKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取 product 与固化 sandbox 标识,
/// 从直连配置([CpcnDirectKeyConfig])读取机构编号/网关地址/指定分账子商户号与两份证书,
/// 组装为下发给子应用的通道调用凭证 [CpcnSdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [ChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变)。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnDirectConfigAssembler {

    private final CpcnDirectKeyConfigService keyConfigService;
    private final ChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(中金不使用, 保留对齐签名)
    /// @return 中金 SDK 凭证
    public CpcnSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(取 sandbox 固化标识)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 2. 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 3. 直连配置(机构身份 + 证书, 按 sandbox 取对应环境密钥)
        CpcnDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo, sandbox);

        // 4. 组装凭证
        var credential = new CpcnSdkCredential();
        credential.setInstitutionId(keyConfig.getInstitutionId());
        credential.setGatewayUrl(keyConfig.getGatewayUrl());
        credential.setAppointMerchantIds(keyConfig.getAppointMerchantIds());
        credential.setMerchantCertPfx(keyConfig.getMerchantCertPfx());
        credential.setMerchantCertPassword(keyConfig.getMerchantCertPassword());
        credential.setPlatformCert(keyConfig.getPlatformCert());
        credential.setSandbox(sandbox);
        return credential;
    }
}
