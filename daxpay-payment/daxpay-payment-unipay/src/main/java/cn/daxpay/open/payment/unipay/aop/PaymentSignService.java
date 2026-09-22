package cn.daxpay.open.payment.unipay.aop;

import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.business.VerifySignFailedException;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService;
import cn.daxpay.open.payment.unipay.param.PaymentCommonParam;
import cn.daxpay.open.payment.unipay.param.assist.UnipayPingParam;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付签名服务
///
/// 从原 `PaymentAssistService` 拆出(签名职责):
/// - `signVerify`:入参验签(线程上下文中的商户号查商户公钥)
/// - `sign`:出参签名(平台私钥)
///
/// ## 签名口径(重要)
///
/// 出入参**共用同一条序列化链路**([PaySignUtil] 内部走 [cn.daxpay.open.platform.common.json.util.JacksonUtil]),
/// 即「签名串 == 报文规范字面量」:
/// - 出参签名后, 调用方按收到的报文即可验签;
/// - 入参验签校验的是调用方按**平台规范字面量**构造的签名串, 故时间字段必须使用契约格式
///   (东八区 `yyyy-MM-dd HH:mm:ss`), 详见对外接口文档「签名机制」。
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSignService {

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
        // 使用商户公钥按平台规范字面量验签
        if (!PaySignUtil.verify(param, param.getSign(), publicKey)) {
            // 留痕待签串, 便于对接方比对字段字面量(时间格式不一致是历史上最常见原因)
            if (log.isWarnEnabled()) {
                log.warn("支付接口验签失败, mchNo: {}, reqId: {}, 待签串: {}",
                        paymentContext.getMchNo(), param.getReqId(),
                        StrUtil.sub(PaySignUtil.buildSignStr(param), 0, 512));
            }
            // 签名自检探针(UnipayPingParam)回吐服务端待签串: 探针参数仅公共字段、不含 authCode/openId 等敏感数据,
            // 待签串也只是调用方自己提交内容的规范字面量, 回吐无泄露风险; 业务接口保持统一的验签失败提示
            if (param instanceof UnipayPingParam) {
                throw new VerifySignFailedException("pay.error.assist.signVerifyFailDetail",
                        StrUtil.sub(PaySignUtil.buildSignStr(param), 0, 2048));
            }
            throw new VerifySignFailedException();
        }
    }

    /// 使用平台私钥对响应对象签名
    public void sign(DaxResult<?> result) {
        String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
        result.setSign(PaySignUtil.sign(result, privateKey));
    }
}
