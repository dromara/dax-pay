package cn.bootx.platform.daxpay.service.core.payment.common.service;

import cn.bootx.platform.daxpay.code.PaySignTypeEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import cn.bootx.platform.daxpay.result.PaymentCommonResult;
import cn.bootx.platform.daxpay.service.common.context.ApiInfoLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.util.PaySignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付签名服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSignService {

    private final PaymentAssistService paymentAssistService;;

    /**
     * 入参签名校验
     */
    public void verifySign(PaymentCommonParam param) {
        // 先触发上下文的初始化
        paymentAssistService.initContext(param);
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();

        // 判断当前接口是否不需要签名
        if (!apiInfo.isReqSign()){
            return;
        }
        // 参数转换为Map对象
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        String signType = platform.getSignType();
        if (Objects.equals(PaySignTypeEnum.HMAC_SHA256.getCode(), signType)){
            boolean verified = PaySignUtil.verifyHmacSha256Sign(param, platform.getSignSecret(), param.getSign());
            if (!verified){
                throw new PayFailureException("未通过签名验证");
            }
        } else if (Objects.equals(PaySignTypeEnum.MD5.getCode(), signType)){
            boolean verified = PaySignUtil.verifyMd5Sign(param, platform.getSignSecret(), param.getSign());
            if (!verified){
                throw new PayFailureException("未通过签名验证");
            }
        } else {
            throw new PayFailureException("签名方式错误");
        }
    }

    /**
     * 对对象进行签名
     */
    public void sign(PaymentCommonResult result) {
        PlatformLocal platformInfo = PaymentContextLocal.get()
                .getPlatformInfo();
        String signType = platformInfo.getSignType();
        if (Objects.equals(PaySignTypeEnum.HMAC_SHA256.getCode(), signType)){
            result.setSign(PaySignUtil.hmacSha256Sign(result, platformInfo.getSignSecret()));
        } else if (Objects.equals(PaySignTypeEnum.MD5.getCode(), signType)){
            result.setSign(PaySignUtil.md5Sign(result, platformInfo.getSignSecret()));
        } else {
            throw new PayFailureException("未获取到签名方式，请检查");
        }
    }
}
