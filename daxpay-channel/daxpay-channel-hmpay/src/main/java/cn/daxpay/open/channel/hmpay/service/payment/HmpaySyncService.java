package cn.daxpay.open.channel.hmpay.service.payment;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.req.HmpaySyncReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpaySyncResp;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/// # 河马付服务商订单同步业务服务
///
/// 通过 [HmpayChannelClient] 调用子应用查询河马付(杉德)订单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpaySyncService {

    /// 杉德纯日期时间格式(yyyyMMddHHmmss)
    private static final DateTimeFormatter PURE_DATETIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final HmpayChannelClient hmpayChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, HmpaySdkCredential credential) {
        HmpaySyncReq req = new HmpaySyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());
        // 原支付下单时间(杉德 order_create_time)
        req.setOrderCreateTime(formatPureDateTime(order.getCreateTime()));

        DaxResult<HmpaySyncResp> result = hmpayChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        HmpaySyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getTradeState(), "SUCCESS")) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getRealAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 子应用 tradeState(已映射为 SUCCESS/FAIL/CLOSE/原值) → 平台资金状态
    private PayFundStatusEnum mapTradeState(String tradeState) {
        if (Objects.equals(tradeState, "SUCCESS")) {
            return PayFundStatusEnum.SUCCESS;
        }
        if (Objects.equals(tradeState, "FAIL")) {
            return PayFundStatusEnum.FAIL;
        }
        if (Objects.equals(tradeState, "CLOSE")) {
            return PayFundStatusEnum.CLOSE;
        }
        // 其他(处理中)
        return PayFundStatusEnum.PROCESSING;
    }

    /// OffsetDateTime → yyyyMMddHHmmss(东八区)
    private String formatPureDateTime(OffsetDateTime time) {
        return time == null ? null : time.toLocalDateTime().format(PURE_DATETIME);
    }
}
