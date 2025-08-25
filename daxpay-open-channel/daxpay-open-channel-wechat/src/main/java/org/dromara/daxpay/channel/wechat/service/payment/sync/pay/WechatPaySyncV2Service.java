package org.dromara.daxpay.channel.wechat.service.payment.sync.pay;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.payment.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.util.WechatPayUtil;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.pay.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import com.github.binarywang.wxpay.constant.WxPayConstants.WxpayTradeStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class WechatPaySyncV2Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 支付信息查询
     */
    public PaySyncResultBo sync(PayOrder order, WechatPayConfig wechatPayConfig) {
        PaySyncResultBo syncResult = new PaySyncResultBo();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        try {
            var result = wxPayService.queryOrder(null, order.getOrderNo());
            syncResult.setSyncData(JacksonUtil.toJson(result))
                    .setOutOrderNo(result.getTransactionId());
            // 支付状态 - 成功 SUCCESS：支付成功  REFUND：转入退款
            if (List.of(WxpayTradeStatus.SUCCESS, WxpayTradeStatus.REFUND).contains(result.getTradeState())){
                syncResult.setPayStatus(PayStatusEnum.SUCCESS)
                        .setFinishTime(WechatPayUtil.parseV2(result.getTimeEnd()))
                        .setAmount(PayUtil.conversionAmount(result.getTotalFee()))
                        .setBuyerId(result.getOpenid());
            }
            // 支付状态 - 支付中  NOTPAY：未支付，等待扣款 USERPAYING：用户支付中（付款码支付）
            if (List.of(WxpayTradeStatus.NOTPAY, WxpayTradeStatus.USER_PAYING).contains(result.getTradeState())){
                syncResult.setPayStatus(PayStatusEnum.PROGRESS);
            }
            // 支付状态 - 失败 PAYERROR：支付失败(其他原因，如银行返回失败)
            if (Objects.equals(WxpayTradeStatus.PAY_ERROR, result.getTradeState())){
                syncResult.setPayStatus(PayStatusEnum.FAIL);
            }
            // 关闭  REVOKED：已撤销（付款码支付） CLOSED：已关闭
            if (Objects.equals(WxpayTradeStatus.CLOSED, result.getTradeState())){
                syncResult.setPayStatus(PayStatusEnum.CLOSE);
            }
            // 撤销
            if (Objects.equals(WxpayTradeStatus.REVOKED, result.getTradeState())){
                syncResult.setPayStatus(PayStatusEnum.CANCEL);
            }
        } catch (WxPayException e) {
            log.error("微信支付V2订单查询失败", e);
            // 订单不存在
            if (Objects.equals(e.getErrCode(), "ORDERNOTEXIST")){
                syncResult.setPayStatus(PayStatusEnum.CLOSE);
            } else {
                syncResult.setSyncErrorMsg(e.getCustomErrorMsg())
                        .setSyncSuccess(false)
                        .setPayStatus(PayStatusEnum.FAIL);
            }
        }
        return syncResult;
    }
}
