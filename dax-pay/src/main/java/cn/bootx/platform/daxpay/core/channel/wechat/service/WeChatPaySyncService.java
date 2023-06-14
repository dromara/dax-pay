package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.daxpay.code.pay.PaySyncStatus;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayCode;
import cn.bootx.platform.daxpay.core.pay.result.PaySyncResult;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 微信支付同步服务
 *
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaySyncService {

    /**
     * 同步查询
     */
    public PaySyncResult syncPayStatus(Long paymentId, WeChatPayConfig weChatPayConfig) {
        PaySyncResult paySyncResult = new PaySyncResult().setPaySyncStatus(PaySyncStatus.FAIL);
        Map<String, String> params = UnifiedOrderModel.builder()
            .appid(weChatPayConfig.getWxAppId())
            .mch_id(weChatPayConfig.getWxMchId())
            .nonce_str(WxPayKit.generateStr())
            .out_trade_no(String.valueOf(paymentId))
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        try {
            String xmlResult = WxPayApi.orderQuery(params);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
            // 查询失败
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RETURN_CODE))) {
                log.warn("查询微信订单失败:{}", result);
                return paySyncResult;
            }

            // 未查到订单
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RESULT_CODE))) {
                log.warn("疑似未查询到订单:{}", result);
                return paySyncResult.setPaySyncStatus(PaySyncStatus.NOT_FOUND);
            }
            String tradeStatus = result.get(WeChatPayCode.TRADE_STATE);
            // 支付完成
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_SUCCESS)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_ACCEPT)) {
                return paySyncResult.setPaySyncStatus(PaySyncStatus.TRADE_SUCCESS).setMap(result);
            }
            // 待支付
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_NOTPAY)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_USERPAYING)) {
                return paySyncResult.setPaySyncStatus(PaySyncStatus.WAIT_BUYER_PAY);
            }

            // 已退款/退款中
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_REFUND)) {
                return paySyncResult.setPaySyncStatus(PaySyncStatus.TRADE_REFUND);
            }
            // 已关闭
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_CLOSED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_REVOKED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_PAYERROR)) {
                return paySyncResult.setPaySyncStatus(PaySyncStatus.TRADE_CLOSED);
            }

        }
        catch (RuntimeException e) {
            log.error("查询订单失败:", e);
        }
        return paySyncResult;
    }

}
