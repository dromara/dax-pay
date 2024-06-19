package cn.daxpay.single.service.core.channel.wechat.service;

import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.exception.ConfigErrorException;
import cn.daxpay.single.core.exception.TradeFaileException;
import cn.daxpay.single.service.common.context.ErrorInfoLocal;
import cn.daxpay.single.service.common.context.RefundLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.RefundModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static cn.daxpay.single.service.code.WeChatPayCode.*;

/**
 * 微信退款服务
 * @author xxm
 * @since 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayRefundService {

    /**
     * 退款方法
     * 微信需要同时传输订单金额或退款金额
     */
    public void refund(RefundOrder refundOrder, WeChatPayConfig weChatPayConfig) {
        String refundFee = String.valueOf(refundOrder.getOrderAmount());
        String totalFee = String.valueOf(refundOrder.getAmount());
        // 设置退款信息
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        ErrorInfoLocal errorInfo = PaymentContextLocal.get().getErrorInfo();
        Map<String, String> params = RefundModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .notify_url(weChatPayConfig.getNotifyUrl())
                .out_trade_no(String.valueOf(refundOrder.getOrderNo()))
                .out_refund_no(String.valueOf(refundOrder.getRefundNo()))
                .total_fee(totalFee)
                .refund_fee(refundFee)
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        // 获取证书文件
        if (StrUtil.isBlank(weChatPayConfig.getP12())){
            String errorMsg = "微信p.12证书未配置，无法进行退款";
            ConfigErrorException configErrorException = new ConfigErrorException(errorMsg);
            errorInfo.setException(configErrorException);
            throw configErrorException;
        }
        byte[] fileBytes = Base64.decode(weChatPayConfig.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        // 证书密码为 微信商户号
        String xmlResult = WxPayApi.orderRefund(false, params, inputStream, weChatPayConfig.getWxMchId());
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        // 微信退款是否成功需要查询状态或者回调, 所以设置为退款中状态
        refundInfo.setStatus(RefundStatusEnum.PROGRESS)
                .setOutRefundNo(result.get(CALLBACK_REFUND_ID));
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(Map<String, String> result) {
        String returnCode = result.get(RETURN_CODE);
        String resultCode = result.get(RESULT_CODE);
        if (!WxPayKit.codeIsOk(returnCode) || !WxPayKit.codeIsOk(resultCode)) {
            String errorMsg = result.get(ERR_CODE_DES);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(RETURN_MSG);
            }
            log.error("订单退款失败 {}", errorMsg);
            TradeFaileException tradeFaileException = new TradeFaileException(errorMsg);
            ErrorInfoLocal errorInfo = PaymentContextLocal.get().getErrorInfo();
            errorInfo.setException(tradeFaileException);
            throw tradeFaileException;
        }
    }

}
