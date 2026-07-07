package cn.daxpay.open.channel.vbill.service.isv;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvChannelMerchantManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvChannelMerchant;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvKeyConfig;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductConfig;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 随行付服务商通道凭证组装器
///
/// 从服务商密钥配置([VbillIsvKeyConfig]) + 通道商户绑定([VbillIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起随行付 API 调用。
///
/// 字段映射(对齐随行付天阙开放平台接口):
/// - orgId ← [VbillIsvKeyConfig.orgId] (天阙合作机构ID, 全局唯一)
/// - privateKey/publicKey ← [VbillIsvKeyConfig] (服务商级, 全局唯一)
/// - mno ← [VbillIsvChannelMerchant.vbillMchNo] (天阙商户号, 子商户级)
/// - sandbox ← PayProductConfig.activeEnv (沙箱/生产)
///
/// 供支付策略([cn.daxpay.open.channel.vbill.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillIsvConfigAssembler {

    private final VbillIsvChannelMerchantManager vbillIsvChannelMerchantManager;
    private final VbillIsvKeyConfigService vbillIsvKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装随行付通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(随行付商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 随行付不按能力路由)
    /// @return 随行付 SDK 凭证(含服务商密钥 + 天阙商户号)
    public VbillSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 服务商密钥(全局唯一, 含机构号 + 私钥/公钥; 缺失或关键字段为空时 fail-fast)
        VbillIsvKeyConfig keyConfig = vbillIsvKeyConfigService.getByProductForPay(ProductEnum.VBILL_PAY.getCode());
        // 通道商户绑定(取天阙商户号 mno)
        VbillIsvChannelMerchant channelMerchant = vbillIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 随行付: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        VbillSdkCredential credential = new VbillSdkCredential();
        // 服务商身份与密钥
        credential.setOrgId(keyConfig.getOrgId());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        // 沙箱状态读取支付产品配置的生效环境
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.VBILL_PAY.getCode())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
        credential.setSandbox(sandbox);
        // 子商户身份(天阙商户号 mno)
        String mno = channelMerchant.getVbillMchNo();
        if (StrUtil.isBlank(mno)) {
            // 随行付: 天阙商户号未配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.vbill.mnoNotConfigured");
        }
        credential.setMno(mno);
        return credential;
    }
}
