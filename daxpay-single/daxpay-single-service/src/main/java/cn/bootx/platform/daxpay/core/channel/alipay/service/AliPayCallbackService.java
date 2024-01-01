package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.CertUtil;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.daxpay.code.AliPayCode;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.record.callback.dao.CallbackRecordManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayConfig;
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

    private final AliPayConfigService aliasConfigService;

    public AliPayCallbackService(RedisClient redisClient, CallbackRecordManager callbackRecordManager,
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
        Map<String, String> params =PaymentContextLocal.get().getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        String appId = params.get(AliPayCode.APP_ID);
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
            if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_KEY)) {
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
        PaymentContextLocal.get().getAsyncPayInfo().setTradeNo(callbackParam.get(AliPayCode.TRADE_NO));
    }

    /**
     * 获取支付id
     */
    @Override
    public Long getPaymentId() {
        Map<String, String> params = PaymentContextLocal.get().getCallbackParam();
        return Long.valueOf(params.get(AliPayCode.OUT_TRADE_NO));
    }

    /**
     * 获取返回信息
     */
    @Override
    public String getReturnMsg() {
        return "success";
    }

}
