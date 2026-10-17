package cn.daxpay.open.channel.youzan.service.payment.sync;

import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 有赞支付同步业务服务
///
/// 有赞(高汇通)统收单线无订单状态查询接口(对齐参考实现能力: 状态依赖 pay_notify_url 异步通知),
/// 本服务不做通道调用, 恒返回软失败让订单保持原状态, 终态以支付回调为准。
@Slf4j
@Service
public class YouzanSyncService {

    /// 同步订单状态(通道不支持查询, 软失败)
    public PaySyncResultBo sync() {
        return new PaySyncResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("有赞通道不支持订单状态查询, 等待支付异步通知确认");
    }
}
