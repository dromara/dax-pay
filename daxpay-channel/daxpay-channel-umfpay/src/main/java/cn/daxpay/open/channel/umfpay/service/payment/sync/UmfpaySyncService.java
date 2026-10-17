package cn.daxpay.open.channel.umfpay.service.payment.sync;

import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 联动优势支付同步业务服务
///
/// 联动优势老网关**无独立订单查询接口**(对齐参考实现能力: 参考插件以轮询本地状态 + 异步通知确认终态,
/// 未提供任何 query service), 本服务不做通道调用, 恒返回软失败(syncSuccess=false):
/// 订单保持平台当前状态(处理中)不被误改, 终态以异步通知为准。
///
/// 代价与兜底: 若异步通知丢失, 支付单将停留在处理中, 需人工对账修复;
/// 后续若商户开通联动优势新开放平台查单接口, 可在本服务接入真实查询。
@Slf4j
@Service
public class UmfpaySyncService {

    /// 同步订单状态(通道无查询接口, 软失败等待回调)
    public PaySyncResultBo sync() {
        return new PaySyncResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("联动优势通道无订单查询接口, 等待异步通知确认终态");
    }
}
