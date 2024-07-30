package cn.daxpay.multi.channel.alipay.service.callback;

import cn.bootx.platform.core.util.CertUtil;
import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.core.enums.CallbackStatusEnum;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static cn.daxpay.multi.channel.alipay.code.AliPayCode.*;

/**
 * 支付宝回调服务
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCallbackService {

    private final AliPayConfigService aliPayConfigService;

    /**
     * 支付回调处理, 解析数据
     */
    public String pay(HttpServletRequest request) {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = PayUtil.toMap(request);
        callback.setCallbackData(callbackParam);

        // 通道和回调类型
        callback.setChannel(ChannelEnum.ALI.getCode())
                .setCallbackType(TradeTypeEnum.PAY);

        if (!verifyNotify(callbackParam)) {
            log.error("支付宝回调报文验签失败");
            callback.setCallbackStatus(CallbackStatusEnum.FAIL);
            return "fail";
        }

        // 网关订单号
        callback.setOutTradeNo(callbackParam.get(TRADE_NO));
        // 支付订单ID
        callback.setTradeNo(callbackParam.get(OUT_TRADE_NO));
        // 支付状态
        PayStatusEnum payStatus = Objects.equals(callbackParam.get(TRADE_STATUS), PayStatus.TRADE_SUCCESS) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callback.setTradeStatus(payStatus.getCode());
        // 支付金额
        String amountStr = callbackParam.get(TOTAL_AMOUNT);
        callback.setAmount(new BigDecimal(amountStr));

        // 支付时间
        String gmpTime = callbackParam.get(GMT_PAYMENT);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            callback.setFinishTime(time);
        } else {
            callback.setFinishTime(LocalDateTime.now());
        }
        // 进行退款的处理
        return "success";
    }

    /**
     * 验证信息格式是否合法
     */
    public boolean verifyNotify(Map<String, String> params) {
        String appId = params.get(APP_ID);
        if (StrUtil.isBlank(appId)) {
            log.error("支付宝回调报文appId为空");
            return false;
        }
        AliPayConfig alipayConfig = aliPayConfigService.getAliPayConfig();
        if (Objects.isNull(alipayConfig)) {
            log.error("支付宝支付配置不存在");
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
}
