package cn.daxpay.open.channel.longpay.service.payment.sync;

import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付支付同步业务服务
///
/// 建行龙支付聚合线的订单查询交易码未获可考证公开资料(公开资料仅覆盖下单 TXCODE=530590
/// 与银企被扫线 PAY100/PAY101, 两者分属不同接口家族), 本服务不做通道调用,
/// 恒返回软失败让支付单保持处理中, 支付终态以异步通知为准;
/// 待《建行龙支付接入指南》到手后在此补齐查询报文与状态映射。
@Slf4j
@Service
public class LongpaySyncService {

    /// 同步订单状态(查询接口待正式文档补齐, 软失败)
    public PaySyncResultBo sync() {
        return new PaySyncResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("建行龙支付订单查询接口待正式文档确认, 暂以支付异步通知为准");
    }
}
