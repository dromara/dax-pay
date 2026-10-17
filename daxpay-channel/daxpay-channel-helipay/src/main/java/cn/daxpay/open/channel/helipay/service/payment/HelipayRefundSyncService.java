package cn.daxpay.open.channel.helipay.service.payment;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝退款同步业务服务
///
/// 合利宝扫码产品线无退款状态查询接口(退款为同步结果模式: AppPayRefund 受理即返回退款流水号与金额),
/// 本服务不做通道调用, 恒返回软失败让退款单保持处理中(实际退款终态已在退款受理时落定,
/// 本路径仅兜底人工/对账场景触发的退款同步)。
@Slf4j
@Service
public class HelipayRefundSyncService {

    /// 同步退款状态(通道不支持查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("合利宝通道不支持退款状态查询(退款为同步结果模式)");
    }
}
