package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.code.WeChatPayCode;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.payment.sync.result.SyncResult;
import cn.hutool.json.JSONUtil;
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
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaySyncService {

    /**
     * 同步查询
     */
    public SyncResult syncPayStatus(Long paymentId, WeChatPayConfig weChatPayConfig) {
        SyncResult syncResult = new SyncResult().setSyncStatus(PaySyncStatusEnum.FAIL.getCode());
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
            syncResult.setJson(JSONUtil.toJsonStr(result));
            // 查询失败
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RETURN_CODE))) {
                log.warn("查询微信订单失败:{}", result);
                return syncResult;
            }

            // 未查到订单
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RESULT_CODE))) {
                log.warn("疑似未查询到订单:{}", result);
                return syncResult.setSyncStatus(PaySyncStatusEnum.NOT_FOUND.getCode());
            }
            String tradeStatus = result.get(WeChatPayCode.TRADE_STATE);
            // 支付完成
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_SUCCESS)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_ACCEPT)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_SUCCESS.getCode()).setMap(result);
            }
            // 待支付
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_NOTPAY)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_USERPAYING)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_WAIT.getCode());
            }

            // 已退款/退款中
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_REFUND)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.REFUND.getCode());
            }
            // 已关闭
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_CLOSED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_REVOKED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_PAYERROR)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED.getCode());
            }

        }
        catch (RuntimeException e) {
            log.error("查询订单失败:", e);
            syncResult.setMsg(e.getMessage());
        }
        return syncResult;
    }

}
