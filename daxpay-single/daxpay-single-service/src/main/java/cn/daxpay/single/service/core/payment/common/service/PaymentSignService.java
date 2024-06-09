package cn.daxpay.single.service.core.payment.common.service;

import cn.daxpay.single.code.PaySignTypeEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.PaymentCommonParam;
import cn.daxpay.single.result.PaymentCommonResult;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import cn.daxpay.single.util.PaySignUtil;
import cn.hutool.core.bean.BeanUtil;
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

    private final PaymentAssistService paymentAssistService;

    private final PlatformConfigService platformConfigService;

    /**
     * 入参签名校验
     */
    public void verifySign(PaymentCommonParam param) {
        // 先触发上下文的初始化
        paymentAssistService.initContext(param);
        PlatformLocal platformInfo = PaymentContextLocal.get().getPlatformInfo();

        // 判断当前接口是否不需要签名
        if (!platformInfo.isReqSign()){
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
        PlatformLocal platformInfo = PaymentContextLocal.get().getPlatformInfo();
        // 如果平台配置所有属性为空, 进行初始化
        if (BeanUtil.isEmpty(platformInfo)){
            platformConfigService.initPlatform();
        }
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
