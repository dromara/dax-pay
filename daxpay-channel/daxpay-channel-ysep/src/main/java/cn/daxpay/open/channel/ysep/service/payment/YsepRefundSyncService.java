package cn.daxpay.open.channel.ysep.service.payment;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银盛退款同步业务服务(两产品共用)
///
/// 银盛两产品族(ysep_pay / yseqt_pay)退款接口均为同步终态返回(成功即成功, 失败即抛异常),
/// 无独立退款查询接口与退款异步通知, 退款单不存在 PROCESSING 中间态;
/// 本同步恒软失败(对齐快钱直连降级模式), 退款单终态以退款接口同步结果为准。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepRefundSyncService {

    /// 退款同步(恒软失败, 退款终态以退款接口同步结果为准)
    public RefundResultBo sync() {
        RefundResultBo bo = new RefundResultBo();
        bo.setSyncSuccess(false);
        bo.setSyncErrorMsg("银盛无退款查询接口, 退款终态以退款接口同步结果为准");
        return bo;
    }
}
