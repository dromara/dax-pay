package cn.daxpay.open.channel.fuyou.service.rsa;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友 RSA 通道凭证组装器
///
/// 从 RSA 密钥配置([FuyouRsaKeyConfig]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起「互联网直连」/「H5 聚合收银台」API 调用(JSON + RSA 加密信封)。
///
/// 两产品共用本组装器与密钥表, 以产品编码区分密钥行归属。
///
/// 字段映射(对齐富友互联网/收银台接口):
/// - merchantNo ← mchntCd(富友商户号)
/// - privateKey ← privateKey(商户RSA私钥, 响应/回调解密)
/// - publicKey  ← publicKey(富友平台RSA公钥, 请求加密)
/// - sandbox    ← 密钥配置固化标识(创建通道商户时按产品 activeEnv 写入, 不随产品切换改变)
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouRsaConfigAssembler {

    private final FuyouRsaKeyConfigService fuyouRsaKeyConfigService;

    /// 组装富友 RSA 通道调用凭证(下发给子应用)
    ///
    /// @param channelMchNo 通道商户号(RSA 密钥配置主键)
    /// @return RSA SDK 凭证(商户号 + 商户私钥 + 平台公钥 + 沙箱标识)
    public FuyouRsaSdkCredential buildConfig(String channelMchNo) {
        FuyouRsaKeyConfig keyConfig = fuyouRsaKeyConfigService.getByChannelMchNoForPay(channelMchNo);
        FuyouRsaSdkCredential credential = new FuyouRsaSdkCredential();
        credential.setMerchantNo(keyConfig.getMchntCd());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(keyConfig.isSandbox());
        return credential;
    }
}
