package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.CertUtil;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.daxpay.code.AliPayCode;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.callback.dao.CallbackNotifyManager;
import cn.bootx.platform.daxpay.core.channel.alipay.dao.AlipayConfigManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.core.payment.callback.service.PayCallbackService;
import cn.bootx.platform.daxpay.func.AbsPayCallbackStrategy;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 支付宝回调处理
 *
 * @author xxm
 * @since 2021/2/28
 */
@Slf4j
@Service
public class AliPayCallbackService extends AbsPayCallbackStrategy {

    private final AlipayConfigManager alipayConfigManager;

    public AliPayCallbackService(RedisClient redisClient, CallbackNotifyManager callbackNotifyManager,
                                 PayCallbackService payCallbackService, AlipayConfigManager alipayConfigManager) {
        super(redisClient, callbackNotifyManager, payCallbackService);
        this.alipayConfigManager = alipayConfigManager;
    }

    @Override
    public PayChannelEnum getPayChannel() {
        return PayChannelEnum.ALI;
    }

    @Override
    public String getTradeStatus() {
        Map<String, String> params = PARAMS.get();
        String tradeStatus = params.get(AliPayCode.TRADE_STATUS);
        if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_SUCCESS)) {
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
        Map<String, String> params = PARAMS.get();
        String callReq = JSONUtil.toJsonStr(params);
        String appId = params.get(AliPayCode.APP_ID);
        if (StrUtil.isBlank(appId)) {
            log.error("支付宝回调报文 appId 为空 {}", callReq);
            return false;
        }
        AlipayConfig alipayConfig = null;
        if (Objects.isNull(alipayConfig)) {
            log.error("支付宝支付配置不存在: {}", callReq);
            return false;
        }

        try {
            if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_KEY)) {
                return AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), CharsetUtil.UTF_8,
                        AlipayConstants.SIGN_TYPE_RSA2);
            }
            else {
                return AlipaySignature.verifyV1(params, CertUtil.getCertByContent(alipayConfig.getAlipayCert()),
                        CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
        }
        catch (AlipayApiException e) {
            log.error("支付宝验签失败", e);
            return false;
        }
    }

    @Override
    public Long getPaymentId() {
        Map<String, String> params = PARAMS.get();
        return Long.valueOf(params.get(AliPayCode.OUT_TRADE_NO));
    }

    @Override
    public String getReturnMsg() {
        return "success";
    }

}
