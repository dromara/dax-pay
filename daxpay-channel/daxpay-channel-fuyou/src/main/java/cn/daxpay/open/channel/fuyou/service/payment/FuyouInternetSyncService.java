package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouInternetChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetSyncReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.PayTradeContainerFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 富友互联网直连订单同步业务服务
///
/// 通过 [FuyouInternetChannelClient] 调用子应用查询富友订单状态(orderQuery.fuiou)。
/// 同步状态: SUCCESS / PROGRESS(互联网查询无已关闭独立状态码, 非成功均视为处理中)。
///
/// 查询需原下单日期(order_date): 取 PayTrade.createTime 转东八区 yyyyMMdd。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetSyncService {

    /// 同步状态: 成功(富友 order_st=1)
    private static final String STATUS_SUCCESS = "SUCCESS";

    private final FuyouInternetChannelClient fuyouInternetChannelClient;
    private final PayTradeContainerFields payTradeContainerFields;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, FuyouRsaSdkCredential credential) {
        FuyouInternetSyncReq req = new FuyouInternetSyncReq();
        req.setCredential(credential);
        var fields = payTradeContainerFields.resolve(order);
        // 富友凭商户订单号(order_id = 平台交易号)查询
        req.setRelationOrderNo(fields.relationOrderNo());
        // 原下单日期(富友查询接口要求)
        req.setOriginOrderTime(order.getCreateTime());

        DaxResult<FuyouSyncResp> result = fuyouInternetChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        FuyouSyncResp resp = result.getData();
        bo.setSyncSuccess(Boolean.TRUE.equals(resp.getSyncSuccess()));
        bo.setSyncErrorMsg(resp.getSyncErrorMsg());
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setPayStatus(mapSyncStatus(resp.getSyncStatus()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getSyncStatus(), STATUS_SUCCESS)) {
            bo.setAmount(resp.getAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 富友同步状态 → 平台资金状态
    private PayFundStatusEnum mapSyncStatus(String syncStatus) {
        if (Objects.equals(syncStatus, STATUS_SUCCESS)) {
            return PayFundStatusEnum.SUCCESS;
        }
        // PROGRESS / 未知视为处理中(互联网查询无已关闭状态码)
        return PayFundStatusEnum.PROCESSING;
    }
}
