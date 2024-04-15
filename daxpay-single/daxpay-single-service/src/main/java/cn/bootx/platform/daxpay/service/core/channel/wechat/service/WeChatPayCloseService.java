package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.CloseOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信支付关闭和退款
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayCloseService {

    /**
     * 关闭支付, 微信对已经关闭的支付单也可以重复关闭
     */
    @Retryable(value = RetryableException.class)
    public void close(PayOrder payOrder, WeChatPayConfig weChatPayConfig) {
        // 只有部分需要调用微信网关进行关闭
        Map<String, String> params = CloseOrderModel.builder()
            .appid(weChatPayConfig.getWxAppId())
            .mch_id(weChatPayConfig.getWxMchId())
            .out_trade_no(payOrder.getOrderNo())
            .nonce_str(WxPayKit.generateStr())
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.closeOrder(params);

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
            log.error("订单关闭失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }

}
