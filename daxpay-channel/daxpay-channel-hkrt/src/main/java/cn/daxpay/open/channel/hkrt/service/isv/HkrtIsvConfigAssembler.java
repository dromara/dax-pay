package cn.daxpay.open.channel.hkrt.service.isv;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.dao.isv.HkrtIsvChannelMerchantManager;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvChannelMerchant;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvKeyConfig;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductConfig;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 海科融通服务商通道凭证组装器
///
/// 从服务商密钥配置([HkrtIsvKeyConfig]) + 通道商户绑定([HkrtIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起海科融通 API 调用。
///
/// 字段映射(对齐海科融通接口):
/// - agent_no / access_id / access_key ← [HkrtIsvKeyConfig](服务商级, 全局唯一)
/// - merch_no ← [HkrtIsvChannelMerchant.merchNo](海科商户号)
/// - pn ← [HkrtIsvChannelMerchant.pn](SAAS 终端号)
///
/// 供支付策略([cn.daxpay.open.channel.hkrt.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtIsvConfigAssembler {

    private final HkrtIsvChannelMerchantManager hkrtIsvChannelMerchantManager;
    private final HkrtIsvKeyConfigService hkrtIsvKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装海科融通通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(海科商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 海科融通不按能力路由)
    /// @return 海科融通 SDK 凭证(含服务商密钥 + 商户号/终端号)
    public HkrtSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 先读取支付产品配置的生效环境判断 sandbox, 再按 sandbox 查对应环境的密钥
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.HKRT_PAY.getCode())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
        // 服务商密钥(按 sandbox 分环境, 含 agentNo/accessId/accessKey; 缺失或关键字段为空时 fail-fast)
        HkrtIsvKeyConfig keyConfig = hkrtIsvKeyConfigService.getByProductForPay(ProductEnum.HKRT_PAY.getCode(), sandbox);
        // 通道商户绑定(取 merchNo + pn)
        HkrtIsvChannelMerchant channelMerchant = hkrtIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 海科融通: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 环境一致性校验
        if (channelMerchant.isSandbox() != sandbox) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.envMismatch");
        }

        HkrtSdkCredential credential = new HkrtSdkCredential();
        // 服务商身份与密钥
        credential.setAgentNo(keyConfig.getAgentNo());
        credential.setAccessId(keyConfig.getAccessId());
        credential.setAccessKey(keyConfig.getAccessKey());
        credential.setSandbox(sandbox);
        // 子商户身份
        credential.setMerchNo(channelMerchant.getMerchNo());
        // SAAS 终端号(从通道商户配置取)
        String pn = channelMerchant.getPn();
        if (StrUtil.isBlank(pn)) {
            // 海科融通: 终端号未配置, 请在商户配置中填写
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "channel.error.hkrtTermNoNotConfigured");
        }
        credential.setPn(pn);
        return credential;
    }
}
