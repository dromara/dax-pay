package cn.daxpay.open.payment.common.assist;

import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.business.VerifySignFailedException;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.common.assist.query.MerchantAccessQueryService;
import cn.daxpay.open.payment.unipay.param.PaymentCommonParam;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付签名服务
///
/// 从原 `PaymentAssistService` 拆出(签名职责):
/// - `signVerify`:入参验签(线程上下文中的商户号查商户公钥)
/// - `sign`:出参签名(平台私钥)
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySignService {

    private final PlatformConfigProperties platformConfigProperties;
    private final MerchantAccessQueryService merchantAccessQueryService;
    private final PaymentContext paymentContext;

    /// 入参签名校验:使用线程上下文中的商户号查询商户公钥进行验签
    public void signVerify(PaymentCommonParam param) {
        // 获取商户公钥
        String publicKey = merchantAccessQueryService.findMerchantPublicKey(paymentContext.getMchNo());
        // 签名和公钥校验
        if (StrUtil.isBlank(publicKey)) {
            // 商户公钥为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchPublicKeyEmpty");
        }
        if (StrUtil.isBlank(param.getSign())) {
            // 签名为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.signEmpty");
        }
        // 使用商户公钥验签
        if (!PaySignUtil.verify(param, publicKey)) {
            throw new VerifySignFailedException();
        }
    }

    /// 使用平台私钥对响应对象签名
    public void sign(DaxResult<?> result) {
        String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
        result.setSign(PaySignUtil.sign(result, privateKey));
    }
}
