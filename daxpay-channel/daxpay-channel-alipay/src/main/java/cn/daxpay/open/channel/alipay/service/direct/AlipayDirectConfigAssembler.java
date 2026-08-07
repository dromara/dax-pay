package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectChannelMerchantManager;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/// # 支付宝直连通道凭证组装器
///
/// 从进件商户对象([AlipayDirectApp] + [AlipayDirectAppKeyConfig])读取密钥/证书,
/// 组装为下发给子应用的通道调用凭证 [AlipaySdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [AlipayDirectChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择对应环境的密钥与网关地址。
///
/// 支付应用解析优先级：能力关联(显式配置 > appType 唯一推导) > 未命中报错(拒绝首个兜底);
/// 转账不走能力关联, 由转账配置([cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferConfig])
/// 显式指定转出应用, 见 [#buildTransferConfig]。
///
/// 供支付策略([cn.daxpay.open.channel.alipay.strategy.direct.AlipayDirectPayStrategy])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectConfigAssembler {

    private final AlipayDirectChannelMerchantManager alipayDirectChannelMerchantManager;
    private final AlipayDirectAppKeyConfigService alipayDirectAppKeyConfigService;
    private final AlipayDirectAppCapabilityService alipayDirectAppCapabilityService;
    private final AlipayDirectAppManager alipayDirectAppManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(最终兜底定位应用)
    /// @param channelMchNo 通道商户号(路由回填)
    /// @param capability   支付能力编码(路由回填,用于选择匹配的应用)
    /// @return 支付宝 SDK 凭证, 字段对齐子应用 AlipaySdkCredential
    public AlipaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        AlipayDirectApp app = resolveApp(mchNo, channelMchNo, capability);
        return assembleCredential(channelMchNo, app);
    }

    /// 组装转账通道调用凭证(转账无能力维度, 按转账配置显式指定的应用)
    ///
    /// @param mchNo        商户号(归属校验)
    /// @param channelMchNo 通道商户号
    /// @param appRefId     转账转出应用引用(alipay_direct_app 主键, 由转账配置绑定)
    /// @return 支付宝 SDK 凭证
    public AlipaySdkCredential buildTransferConfig(String mchNo, String channelMchNo, Long appRefId) {
        AlipayDirectApp app = alipayDirectAppManager.lambdaQuery()
                .eq(AlipayDirectApp::getId, appRefId)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.transferAppNotExist"));
        if (!Objects.equals(app.getMchNo(), mchNo)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferAppNotBelong");
        }
        return assembleCredential(channelMchNo, app);
    }

    /// 按通道商户与应用组装凭证(通道商户沙箱快照 + 应用级密钥)
    private AlipaySdkCredential assembleCredential(String channelMchNo, AlipayDirectApp app) {
        AlipayDirectChannelMerchant channelMerchant = alipayDirectChannelMerchantManager.lambdaQuery()
                .eq(AlipayDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入), 据此查对应环境密钥
        boolean sandbox = channelMerchant.isSandbox();
        AlipayDirectAppKeyConfig keyConfig = alipayDirectAppKeyConfigService.findByAlipayDirectAppId(app.getId(), sandbox);

        var credential = new AlipaySdkCredential();
        credential.setAliAppId(app.getAliAppId());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setAlipayPublicKey(keyConfig.getAlipayPublicKey());
        credential.setAuthType(keyConfig.getAuthType());
        credential.setAppCert(keyConfig.getAppCert());
        credential.setAlipayCert(keyConfig.getAlipayCert());
        credential.setAlipayRootCert(keyConfig.getAlipayRootCert());
        credential.setSandbox(sandbox);
        // TODO notifyUrl 后续按业务要求手动组装(来源已迁移至进件配置, 待确认组装方式)
        return credential;
    }

    /// 解析支付使用的应用（须已装载 mchNo，租户内）
    /// 优先级：能力关联（显式配置 > appType 唯一推导）> 未命中报错（不再首个兜底）
    private AlipayDirectApp resolveApp(String mchNo, String channelMchNo, String capability) {
        // 能力关联解析(显式配置 > appType 唯一推导)
        Optional<AlipayDirectApp> resolved = alipayDirectAppCapabilityService.resolveApp(channelMchNo, capability);
        if (resolved.isPresent()) {
            return resolved.get();
        }
        // 未命中：不再做通道商户首个/商户号首个兜底，直接报错
        // 支付宝: 未配置该能力对应的应用
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                "error.channel.alipay.appNotConfigured", capability);
    }
}
