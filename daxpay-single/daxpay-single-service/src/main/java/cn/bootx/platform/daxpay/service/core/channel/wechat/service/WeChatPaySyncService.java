package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.GatewaySyncResult;
import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 微信支付同步服务
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaySyncService {

    /**
     * 同步查询
     */
    public GatewaySyncResult syncPayStatus(Long paymentId, WeChatPayConfig weChatPayConfig) {
        GatewaySyncResult syncResult = new GatewaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
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
            syncResult.setSyncInfo(JSONUtil.toJsonStr(result));
            // 查询失败
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RETURN_CODE))) {
                log.warn("查询微信订单失败:{}", result);
                return syncResult;
            }

            // 未查到订单
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RESULT_CODE))) {
                log.warn("疑似未查询到订单:{}", result);
                return syncResult.setSyncStatus(PaySyncStatusEnum.NOT_FOUND);
            }
            String tradeStatus = result.get(WeChatPayCode.TRADE_STATE);
            // 支付完成
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_SUCCESS) || Objects.equals(tradeStatus, WeChatPayCode.TRADE_ACCEPT)) {
                String timeEnd = result.get(WeChatPayCode.TIME_END);
                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
                PaymentContextLocal.get().getAsyncPayInfo().setPayTime(time);
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_SUCCESS);
            }
            // 待支付
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_NOTPAY)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_USERPAYING)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_WAIT);
            }

            // 已退款/退款中
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_REFUND)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.REFUND);
            }
            // 已关闭
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_CLOSED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_REVOKED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_PAYERROR)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED);
            }
        }
        catch (RuntimeException e) {
            log.error("查询订单失败:", e);
            syncResult.setErrorMsg(e.getMessage());
        }
        return syncResult;
    }
}
