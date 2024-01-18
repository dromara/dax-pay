package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.common.context.AsyncRefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.util.PayUtil;
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
import java.util.Optional;

/**
 * 微信退款服务
 * @author xxm
 * @since 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundService {

    private final PayOrderChannelManager payOrderChannelManager;

    /**
     * 退款方法
     * 微信需要同时传输订单金额或退款金额
     */
    public void refund(PayOrder payOrder, int amount, WeChatPayConfig weChatPayConfig) {
        PayOrderChannel orderChannel = payOrderChannelManager.findByPaymentIdAndChannel(payOrder.getId(), PayChannelEnum.WECHAT.getCode())
                .orElseThrow(() -> new PayFailureException("未找到微信支付的详细信息"));
        String refundFee = String.valueOf(amount);
        String totalFee = String.valueOf(orderChannel.getAmount());
        // 设置退款信息
        AsyncRefundLocal refundInfo = PaymentContextLocal.get().getAsyncRefundInfo();
        refundInfo.setRefundRequestNo(PayUtil.getRefundNo());

        Map<String, String> params = RefundModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .out_trade_no(String.valueOf(payOrder.getId()))
                .out_refund_no(refundInfo.getRefundRequestNo())
                .total_fee(totalFee)
                .refund_fee(refundFee)
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        // 获取证书文件
        if (StrUtil.isBlank(weChatPayConfig.getP12())){
            String errorMsg = "微信p.12证书未配置，无法进行退款";
            refundInfo.setErrorMsg(errorMsg);
            refundInfo.setErrorCode(PayRefundStatusEnum.FAIL.getCode());
            throw new PayFailureException(errorMsg);
        }
        byte[] fileBytes = Base64.decode(weChatPayConfig.getP12());
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
            log.error("订单退款失败 {}", errorMsg);
            AsyncRefundLocal refundInfo = PaymentContextLocal.get().getAsyncRefundInfo();
            refundInfo.setErrorMsg(errorMsg);
            refundInfo.setErrorCode(Optional.ofNullable(resultCode).orElse(returnCode));
            throw new PayFailureException(errorMsg);
        }
    }

}