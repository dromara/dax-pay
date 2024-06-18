package cn.daxpay.single.service.core.channel.wechat.service;

import cn.daxpay.single.service.code.WeChatPayCode;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.CloseOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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
     * 撤销接口
     */
    public void cancel(PayOrder payOrder, WeChatPayConfig weChatPayConfig){
        // 只有部分需要调用微信网关进行关闭
        Map<String, String> params = CloseOrderModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .out_trade_no(payOrder.getOrderNo())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        // 获取证书文件
        if (StrUtil.isBlank(weChatPayConfig.getP12())){
            String errorMsg = "微信p.12证书未配置，无法进行退款";
            throw new PayFailureException(errorMsg);
        }
        byte[] fileBytes = Base64.decode(weChatPayConfig.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        // 证书密码为 微信商户号
        String xmlResult = WxPayApi.orderReverse(params,inputStream,weChatPayConfig.getWxMchId());
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
