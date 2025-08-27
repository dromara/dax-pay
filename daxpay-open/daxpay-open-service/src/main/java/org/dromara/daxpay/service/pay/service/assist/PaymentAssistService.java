package org.dromara.daxpay.service.pay.service.assist;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.core.context.PaymentReqInfoLocal;
import org.dromara.daxpay.core.context.PaymentClientLocal;
import org.dromara.daxpay.core.enums.*;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.VerifySignFailedException;
import org.dromara.daxpay.core.param.PaymentCommonParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.util.PaySignUtil;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 交易支持服务接口
 * @author xxm
 * @since 2023/12/26
 */
public interface PaymentAssistService {

    /**
     * 初始化请求相关信息上下文
     */
    default void initClient(PaymentCommonParam paymentCommonParam){
        PaymentClientLocal request = PaymentContextLocal.get().getClientInfo();
        request.setClientIp(paymentCommonParam.getClientIp());
    }

    /**
     * 入参签名校验
     */
    default void signVerify(PaymentCommonParam param) {
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        // 判断是否不需要签名
        if (!reqInfo.getReqSign()){
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
    default void reqTimeoutVerify(PaymentCommonParam param) {
        var reqInfo = PaymentContextLocal.get().getReqInfo();
        // 开启请求超时校验并设置值
        if (reqInfo.getReqTimeout() && Objects.nonNull(reqInfo.getReqTimeoutSecond()) ){
            LocalDateTime now = LocalDateTime.now();
            // 时间差值(秒)
            long durationSeconds = Math.abs(LocalDateTimeUtil.between(now, param.getReqTime()).getSeconds());
            // 当前时间是否晚于请求时间
            if (DateTimeUtil.lt(now, param.getReqTime())){
                // 请求时间比服务器时间晚, 超过配置时间直接打回
                if (durationSeconds > reqInfo.getReqTimeoutSecond()){
                    throw new ValidationFailedException("请求时间晚于服务器接收到的时间，请检查");
                }
            } else {
                // 请求时间比服务器时间早, 超过配置时间直接打回
                if (durationSeconds > reqInfo.getReqTimeoutSecond()){
                    throw new ValidationFailedException("请求已过期，请重新发送！");
                }
            }

        }
    }

    /**
     * 对响应对象进行签名
     */
    default void sign(DaxResult<?> result) {
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        String signType = reqInfo.getSignType();
        if (Objects.equals(SignTypeEnum.HMAC_SHA256.getCode(), signType)){
            result.setSign(PaySignUtil.hmacSha256Sign(result, reqInfo.getSignSecret()));
        }
        else if (Objects.equals(SignTypeEnum.MD5.getCode(), signType)){
            result.setSign(PaySignUtil.md5Sign(result, reqInfo.getSignSecret()));
        } else if (Objects.equals(SignTypeEnum.SM3.getCode(), signType)){
            result.setSign(PaySignUtil.md5Sign(result, reqInfo.getSignSecret()));
        }
        else {
            throw new ValidationFailedException("未获取到签名方式，请检查");
        }
    }

    /**
     * 获取请求参数的签名值
     */
    default String genSign(PaymentCommonParam param) {
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        String signType = reqInfo.getSignType();
        if (Objects.equals(SignTypeEnum.HMAC_SHA256.getCode(), signType)){
            return PaySignUtil.hmacSha256Sign(param, reqInfo.getSignSecret());
        } else if (Objects.equals(SignTypeEnum.MD5.getCode(), signType)){
            return PaySignUtil.md5Sign(param, reqInfo.getSignSecret());
        } else if (Objects.equals(SignTypeEnum.SM3.getCode(), signType)){
            return PaySignUtil.md5Sign(param, reqInfo.getSignSecret());
        }
        else {
            throw new ValidationFailedException("未获取到签名方式，请检查");
        }
    }

    /**
     * 初始化商户和应用信息, 用于设置当前的商户和应用上下文, 默认为空
     * 1. 统一支付相关接口调用时，要进行初始化
     * 2. 接收到回调时，要进行初始化
     * 3. 接收到消息通知时, 要进行初始化
     * 4. 运营端或代理商端进行操作时, 手动指定
     */
    default void initMchAndApp(String mchNo, String appId) {
    }

    /**
     * 初始化商户和应用信息, 用于设置当前的商户和应用上下文
     */
    default void initMchAndApp(String appId) {
    }

    /**
     * 验证
     */
    default void checkStatus(){
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        if (Objects.isNull(reqInfo)){
            throw new DataNotExistException("未获取到商户和应用信息，请检查");
        }
        // 应用
        if (!Objects.equals(reqInfo.getStatus(), MchAppStatusEnum.ENABLE.getCode())){
            throw new ConfigNotEnableException("商户应用未启用");
        }
        // 商户
        if (!Objects.equals(reqInfo.getMchStatus(), MerchantStatusEnum.ENABLE.getCode())){
            throw new ConfigNotEnableException("商户未启用");
        }
    }
}
