package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.common.context.RefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
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

import static cn.bootx.platform.daxpay.service.code.WeChatPayCode.*;

/**
 * 微信退款服务
 * @author xxm
 * @since 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayRefundService {

    /**
     * 退款方法
     * 微信需要同时传输订单金额或退款金额
     */
    public void refund(RefundOrder refundOrder, int amount, PayChannelOrder orderChannel, WeChatPayConfig weChatPayConfig) {
        String refundFee = String.valueOf(amount);
        String totalFee = String.valueOf(orderChannel.getAmount());
        // 设置退款信息
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        Map<String, String> params = RefundModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .notify_url(weChatPayConfig.getNotifyUrl())
                .out_trade_no(String.valueOf(refundOrder.getPaymentId()))
                .out_refund_no(String.valueOf(refundOrder.getId()))
                .total_fee(totalFee)
                .refund_fee(refundFee)
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        // 获取证书文件
        if (StrUtil.isBlank(weChatPayConfig.getP12())){
            String errorMsg = "微信p.12证书未配置，无法进行退款";
            refundInfo.setErrorMsg(errorMsg);
            refundInfo.setErrorCode(RefundStatusEnum.FAIL.getCode());
            throw new PayFailureException(errorMsg);
        }
        byte[] fileBytes = Base64.decode(weChatPayConfig.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        // 证书密码为 微信商户号
        String xmlResult = WxPayApi.orderRefund(false, params, inputStream, weChatPayConfig.getWxMchId());
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        // 微信退款是否成功需要查询状态或者回调, 所以设置为退款中状态
        refundInfo.setStatus(RefundStatusEnum.PROGRESS)
                .setGatewayOrderNo(result.get(CALLBACK_REFUND_ID));
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
            RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
            refundInfo.setErrorMsg(errorMsg);
            refundInfo.setErrorCode(Optional.ofNullable(resultCode).orElse(returnCode));
            throw new PayFailureException(errorMsg);
        }
    }

}
