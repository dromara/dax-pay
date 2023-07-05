package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayCode;
import cn.bootx.platform.daxpay.core.refund.local.AsyncRefundLocal;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayment;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.starter.file.service.FileUploadService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.CloseOrderModel;
import com.ijpay.wxpay.model.RefundModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.bootx.platform.daxpay.code.pay.PayStatusCode.REFUND_PROCESS_FAIL;

/**
 * 微信支付关闭和退款
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayCancelService {

    private final FileUploadService uploadService;

    /**
     * 关闭支付
     */
    @Retryable(value = RetryableException.class)
    public void cancelRemote(Payment payment, WeChatPayConfig weChatPayConfig) {
        // 只有部分需要调用微信网关进行关闭
        Map<String, String> params = CloseOrderModel.builder()
            .appid(weChatPayConfig.getWxAppId())
            .mch_id(weChatPayConfig.getWxMchId())
            .out_trade_no(String.valueOf(payment.getId()))
            .nonce_str(WxPayKit.generateStr())
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.closeOrder(params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 退款
     */
    public void refund(Payment payment, WeChatPayment weChatPayment, BigDecimal amount,
            WeChatPayConfig weChatPayConfig) {
        String totalFee = weChatPayment.getAmount().multiply(BigDecimal.valueOf(100)).toBigInteger().toString();
        String refundFee = amount.multiply(BigDecimal.valueOf(100)).toBigInteger().toString();
        // 设置退款号
        AsyncRefundLocal.set(IdUtil.getSnowflakeNextIdStr());
        Map<String, String> params = RefundModel.builder()
            .appid(weChatPayConfig.getWxAppId())
            .mch_id(weChatPayConfig.getWxMchId())
            .out_trade_no(String.valueOf(payment.getId()))
            .out_refund_no(AsyncRefundLocal.get())
            .total_fee(totalFee)
            .refund_fee(refundFee)
            .nonce_str(WxPayKit.generateStr())
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        // 获取证书文件流
        if (Objects.isNull(weChatPayConfig.getP12())){
            String errorMsg = "微信p.12证书未配置，无法进行退款";
            AsyncRefundLocal.setErrorMsg(errorMsg);
            AsyncRefundLocal.setErrorCode(REFUND_PROCESS_FAIL);
            throw new PayFailureException(errorMsg);
        }
        byte[] fileBytes = uploadService.getFileBytes(weChatPayConfig.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        // 证书密码为 微信商户号
        String xmlResult = WxPayApi.orderRefund(false, params, inputStream, weChatPayConfig.getWxMchId());
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(Map<String, String> result) {
        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        if (!WxPayKit.codeIsOk(returnCode) || !WxPayKit.codeIsOk(resultCode)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(WeChatPayCode.RETURN_MSG);
            }
            log.error("订单退款/关闭失败 {}", errorMsg);
            AsyncRefundLocal.setErrorMsg(errorMsg);
            AsyncRefundLocal.setErrorCode(Optional.ofNullable(resultCode).orElse(returnCode));
            throw new PayFailureException(errorMsg);
        }
    }

}
