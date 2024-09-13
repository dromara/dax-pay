package cn.daxpay.single.service.core.payment.common.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PaySignTypeEnum;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.daxpay.single.core.exception.VerifySignFailedException;
import cn.daxpay.single.core.param.PaymentCommonParam;
import cn.daxpay.single.core.result.PaymentCommonResult;
import cn.daxpay.single.core.util.PaySignUtil;
import cn.daxpay.single.service.common.context.ClientLocal;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 支付、退款等各类操作支持服务
 * @author xxm
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAssistService {
    private final PlatformConfigService platformConfigService;

    /**
     * 初始化请求相关信息上下文
     */
    public void initClient(PaymentCommonParam paymentCommonParam){
        ClientLocal request = PaymentContextLocal.get().getClientInfo();
        request.setClientIp(paymentCommonParam.getClientIp());
    }

    /**
     * 入参签名校验
     */
    public void signVerify(PaymentCommonParam param) {
        PlatformLocal platformInfo = PaymentContextLocal.get().getPlatformInfo();
        // 如果平台配置所有属性为空, 进行初始化
        if (BeanUtil.isEmpty(platformInfo)){
            platformConfigService.initPlatform();
        }
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
                throw new VerifySignFailedException();
            }
        } else if (Objects.equals(PaySignTypeEnum.MD5.getCode(), signType)){
            boolean verified = PaySignUtil.verifyMd5Sign(param, platform.getSignSecret(), param.getSign());
            if (!verified){
                throw new VerifySignFailedException();
            }
        } else {
            throw new VerifySignFailedException();
        }
    }

    /**
     * 请求有效时间校验
     */
    public void reqTimeoutVerify(PaymentCommonParam param) {
        PlatformLocal platformInfo = PaymentContextLocal.get().getPlatformInfo();
        // 如果平台配置所有属性为空, 进行初始化
        if (BeanUtil.isEmpty(platformInfo)){
            platformConfigService.initPlatform();
        }
        if (Objects.nonNull(platformInfo.getReqTimeout()) ){
            LocalDateTime now = LocalDateTime.now();
            // 时间差值(秒)
            long durationSeconds = Math.abs(LocalDateTimeUtil.between(now, param.getReqTime()).getSeconds());
            // 当前时间是否晚于请求时间
            if (LocalDateTimeUtil.lt(now, param.getReqTime())){
                // 请求时间比服务器时间晚, 超过一分钟直接打回
                if (durationSeconds > 60){
                    throw new ValidationFailedException("请求时间晚于服务器接收到的时间，请检查");
                }
            } else {
                // 请求时间比服务器时间早, 超过配置时间直接打回
                if (durationSeconds > platformInfo.getReqTimeout()){
                    throw new ValidationFailedException("请求已过期，请重新发送！");
                }
            }

        }
    }

    /**
     * 对对象进行签名
     */
    public void sign(PaymentCommonResult result) {
        PlatformLocal platformInfo = PaymentContextLocal.get().getPlatformInfo();
        // 如果平台配置所有属性为空, 进行初始化
        if (BeanUtil.isEmpty(platformInfo, "reqSign")){
            platformConfigService.initPlatform();
        }
        String signType = platformInfo.getSignType();
        if (Objects.equals(PaySignTypeEnum.HMAC_SHA256.getCode(), signType)){
            result.setSign(PaySignUtil.hmacSha256Sign(result, platformInfo.getSignSecret()));
        } else if (Objects.equals(PaySignTypeEnum.MD5.getCode(), signType)){
            result.setSign(PaySignUtil.md5Sign(result, platformInfo.getSignSecret()));
        } else {
            throw new ValidationFailedException("未获取到签名方式，请检查");
        }
    }
}
