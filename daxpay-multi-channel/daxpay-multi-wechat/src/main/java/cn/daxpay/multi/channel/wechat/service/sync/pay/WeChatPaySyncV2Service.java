package cn.daxpay.multi.channel.wechat.service.sync.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.util.WechatPayUtil;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.bo.sync.PaySyncResultBo;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.enums.PaySyncResultEnum;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.constant.WxPayConstants.WxpayTradeStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
public class WeChatPaySyncV2Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 支付信息查询
     */
    public PaySyncResultBo sync(PayOrder order, WechatPayConfig wechatPayConfig) {
        PaySyncResultBo syncResult = new PaySyncResultBo();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        try {
            var result = wxPayService.queryOrder(null, order.getOrderNo());
            syncResult.setSyncInfo(JSONUtil.toJsonStr(result))
                    .setOutOrderNo(result.getTransactionId())
                    .setAmount(PayUtil.conversionAmount(result.getTotalFee()));
            // 支付状态 - 成功 SUCCESS：支付成功  REFUND：转入退款
            if (List.of(WxpayTradeStatus.SUCCESS, WxpayTradeStatus.REFUND).contains(result.getTradeState())){
                syncResult.setSyncStatus(PaySyncResultEnum.SUCCESS)
                        .setFinishTime(WechatPayUtil.parseV2(result.getTimeEnd()));
            }
            // 支付状态 - 支付中  NOTPAY：未支付，等待扣款 USERPAYING：用户支付中（付款码支付）
            if (List.of(WxpayTradeStatus.NOTPAY, WxpayTradeStatus.USER_PAYING).contains(result.getTradeState())){
                syncResult.setSyncStatus(PaySyncResultEnum.PROGRESS);
            }
            // 支付状态 - 失败 PAYERROR：支付失败(其他原因，如银行返回失败)
            if (Objects.equals(WxpayTradeStatus.PAY_ERROR, result.getTradeState())){
                syncResult.setSyncStatus(PaySyncResultEnum.CLOSED);
            }
            // 关闭  REVOKED：已撤销（付款码支付） CLOSED：已关闭
            if (List.of(WxpayTradeStatus.REVOKED, WxpayTradeStatus.CLOSED).contains(result.getTradeState())){
                syncResult.setSyncStatus(PaySyncResultEnum.CLOSED);
            }
        } catch (WxPayException e) {
            log.error("微信支付V2订单查询失败", e);
            syncResult.setSyncErrorMsg(e.getCustomErrorMsg()).setSyncStatus(PaySyncResultEnum.SYNC_FAIL);
        }
        return syncResult;
    }
}
