package cn.daxpay.open.channel.ysep.service.payment;

import cn.daxpay.open.channel.ysep.client.YsepChannelClient;
import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.client.req.YsepSyncReq;
import cn.daxpay.open.channel.ysep.client.resp.YsepSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/// # 银盛订单同步业务服务(两产品共用)
///
/// 通过 [YsepChannelClient] 调用子应用查询银盛订单状态, 按 [ProductEnum] 组装 req.product 分发。
///
/// 银盛交易状态映射:
/// - ysep_pay: TRADE_SUCCESS=成功, TRADE_CLOSED=关闭, 其他=处理中
/// - yseqt_pay: SUCCESS=成功, FAIL=失败, 其他=处理中
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepSyncService {

    private final YsepChannelClient ysepChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, YsepSdkCredential credential, ProductEnum product) {
        YsepSyncReq req = new YsepSyncReq();
        req.setCredential(credential);
        req.setProduct(product.getCode());
        req.setOutTradeNo(order.getTradeNo());
        req.setTradeNo(order.getOutOrderNo());
        // 开放平台族 shopdate 须与下单时一致(订单创建日, 东八区)
        if (Objects.nonNull(order.getCreateTime())) {
            req.setTradeDate(order.getCreateTime()
                    .atZoneSameInstant(ZoneOffset.ofHours(8))
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }

        DaxResult<YsepSyncResp> result = ysepChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        YsepSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getTradeState(), "TRADE_SUCCESS")
                || Objects.equals(resp.getTradeState(), "SUCCESS")) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getTotalAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 银盛交易状态 → 平台资金状态(两产品状态码并集映射)
    private PayFundStatusEnum mapTradeState(String tradeState) {
        // 支付成功(ysep_pay / yseqt_pay)
        if (Objects.equals(tradeState, "TRADE_SUCCESS") || Objects.equals(tradeState, "SUCCESS")) {
            return PayFundStatusEnum.SUCCESS;
        }
        // 支付失败(yseqt_pay)
        if (Objects.equals(tradeState, "FAIL") || Objects.equals(tradeState, "TRADE_FAIL")) {
            return PayFundStatusEnum.FAIL;
        }
        // 已关闭(ysep_pay)
        if (Objects.equals(tradeState, "TRADE_CLOSED") || Objects.equals(tradeState, "CLOSED")) {
            return PayFundStatusEnum.CLOSE;
        }
        // 处理中(FAIL_PROCESSING 及其他)
        return PayFundStatusEnum.PROCESSING;
    }
}
