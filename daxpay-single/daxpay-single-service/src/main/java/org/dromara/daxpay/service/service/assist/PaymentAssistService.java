package org.dromara.daxpay.service.service.assist;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.DateTimeUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.SignTypeEnum;
import org.dromara.daxpay.core.exception.VerifySignFailedException;
import org.dromara.daxpay.core.param.PaymentCommonParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.util.PaySignUtil;
import org.dromara.daxpay.service.common.cache.MchAppCacheService;
import org.dromara.daxpay.service.common.context.ClientLocal;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.entity.merchant.MchApp;
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


    private final MchAppCacheService mchAppCacheService;

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
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        // 判断是否不需要签名
        if (!mchAppInfo.getReqSign()){
            return;
        }
        String sign = this.genSign(param);
        if (!Objects.equals(sign,param.getSign())){
            throw new VerifySignFailedException();
        }
    }

    /**
     * 请求有效时间校验
     */
    public void reqTimeoutVerify(PaymentCommonParam param) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        if (Objects.nonNull(mchAppInfo.getReqTimeout()) ){
            LocalDateTime now = LocalDateTime.now();
            // 时间差值(秒)
            long durationSeconds = Math.abs(LocalDateTimeUtil.between(now, param.getReqTime()).getSeconds());
            // 当前时间是否晚于请求时间
            if (DateTimeUtil.lt(now, param.getReqTime())){
                // 请求时间比服务器时间晚, 超过一分钟直接打回
                if (durationSeconds > 60){
                    throw new ValidationFailedException("请求时间晚于服务器接收到的时间，请检查");
                }
            } else {
                // 请求时间比服务器时间早, 超过配置时间直接打回
                if (durationSeconds > mchAppInfo.getReqTimeout()){
                    throw new ValidationFailedException("请求已过期，请重新发送！");
                }
            }

        }
    }

    /**
     * 对响应对象进行签名
     */
    public void sign(DaxResult<?> result) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        String signType = mchAppInfo.getSignType();
        if (Objects.equals(SignTypeEnum.HMAC_SHA256.getCode(), signType)){
            result.setSign(PaySignUtil.hmacSha256Sign(result, mchAppInfo.getSignSecret()));
        }
        else if (Objects.equals(SignTypeEnum.MD5.getCode(), signType)){
            result.setSign(PaySignUtil.md5Sign(result, mchAppInfo.getSignSecret()));
        } else if (Objects.equals(SignTypeEnum.SM3.getCode(), signType)){
            result.setSign(PaySignUtil.md5Sign(result, mchAppInfo.getSignSecret()));
        }
        else {
            throw new ValidationFailedException("未获取到签名方式，请检查");
        }
    }

    /**
     * 获取请求参数的签名值
     */
    public String genSign(PaymentCommonParam param) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        String signType = mchAppInfo.getSignType();
        if (Objects.equals(SignTypeEnum.HMAC_SHA256.getCode(), signType)){
           return PaySignUtil.hmacSha256Sign(param, mchAppInfo.getSignSecret());
        }
        else if (Objects.equals(SignTypeEnum.MD5.getCode(), signType)){
            return PaySignUtil.md5Sign(param, mchAppInfo.getSignSecret());
        } else if (Objects.equals(SignTypeEnum.SM3.getCode(), signType)){
            return PaySignUtil.md5Sign(param, mchAppInfo.getSignSecret());
        }
        else {
            throw new ValidationFailedException("未获取到签名方式，请检查");
        }
    }


    /**
     * 初始化应用信息
     * 1. 统一支付相关接口调用时，要进行初始化
     * 2. 接收到回调时，要进行初始化
     * 3. 接收到消息通知时, 要进行初始化
     *
     */
    public void initMchApp(String appId) {
        // 获取应用信息
        MchApp mchApp = mchAppCacheService.get(appId);
        // 初始化支付上下文信息
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        BeanUtil.copyProperties(mchApp, mchAppInfo);
    }
}
