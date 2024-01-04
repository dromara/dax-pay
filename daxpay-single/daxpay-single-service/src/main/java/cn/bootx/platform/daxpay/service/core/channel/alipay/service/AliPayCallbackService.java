package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.CertUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.payment.callback.service.PayCallbackService;
import cn.bootx.platform.daxpay.service.core.record.callback.dao.PayCallbackRecordManager;
import cn.bootx.platform.daxpay.service.func.AbsPayCallbackStrategy;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.daxpay.service.code.AliPayCode.*;

/**
 * 支付宝回调处理
 *
 * @author xxm
 * @since 2021/2/28
 */
@Slf4j
@Service
public class AliPayCallbackService extends AbsPayCallbackStrategy {

    private final AliPayConfigService aliasConfigService;

    public AliPayCallbackService(RedisClient redisClient, PayCallbackRecordManager callbackRecordManager,
                                 PayCallbackService payCallbackService, AliPayConfigService aliasConfigService) {
        super(redisClient, callbackRecordManager, payCallbackService);
        this.aliasConfigService = aliasConfigService;
    }

    /**
     * 获取支付通道
     */
    @Override
    public PayChannelEnum getPayChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 获取交易状态
     */
    @Override
    public String getTradeStatus() {
        Map<String, String> params = PaymentContextLocal.get().getCallbackParam();
        String tradeStatus = params.get(TRADE_STATUS);
        if (Objects.equals(tradeStatus, NOTIFY_TRADE_SUCCESS)) {
            return PayStatusEnum.SUCCESS.getCode();
        }
        return PayStatusEnum.FAIL.getCode();
    }

    /**
     * 验证信息格式是否合法
     */
    @SneakyThrows
    @Override
    public boolean verifyNotify() {
        Map<String, String> params =PaymentContextLocal.get().getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        String appId = params.get(APP_ID);
        if (StrUtil.isBlank(appId)) {
            log.error("支付宝回调报文 appId 为空 {}", callReq);
            return false;
        }
        AliPayConfig alipayConfig = aliasConfigService.getConfig();
        if (Objects.isNull(alipayConfig)) {
            log.error("支付宝支付配置不存在: {}", callReq);
            return false;
        }
        try {
            if (Objects.equals(alipayConfig.getAuthType(), AUTH_TYPE_KEY)) {
                return AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
            else {
                return AlipaySignature.verifyV1(params, CertUtil.getCertByContent(alipayConfig.getAlipayCert()), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验签失败", e);
            return false;
        }
    }

    /**
     * 分通道特殊处理, 如将解析的数据放到上下文中
     */
    @Override
    public void initContext() {
        Map<String, String> callbackParam = PaymentContextLocal.get().getCallbackParam();
        // 订单号
        PaymentContextLocal.get().getAsyncPayInfo().setTradeNo(callbackParam.get(TRADE_NO));
        // 支付时间
        String gmpTime = callbackParam.get(GMT_PAYMENT);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            PaymentContextLocal.get().getAsyncPayInfo().setPayTime(time);
        } else {
            PaymentContextLocal.get().getAsyncPayInfo().setPayTime(LocalDateTime.now());
        }
    }

    /**
     * 获取支付id
     */
    @Override
    public Long getPaymentId() {
        Map<String, String> params = PaymentContextLocal.get().getCallbackParam();
        return Long.valueOf(params.get(OUT_TRADE_NO));
    }

    /**
     * 获取返回信息
     */
    @Override
    public String getReturnMsg() {
        return "success";
    }

}
