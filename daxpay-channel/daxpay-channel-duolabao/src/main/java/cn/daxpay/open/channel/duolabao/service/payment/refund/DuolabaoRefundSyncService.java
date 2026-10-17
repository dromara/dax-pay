package cn.daxpay.open.channel.duolabao.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 哆啦宝退款同步业务服务
///
/// 哆啦宝无退款状态查询接口(对齐参考插件能力: 退款为同步受理即成功),
/// 本服务不做通道调用, 恒返回软失败让调用方以退款受理结果为准。
@Slf4j
@Service
public class DuolabaoRefundSyncService {

    /// 同步退款状态(通道不支持查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("哆啦宝通道不支持退款状态查询, 退款为同步受理即成功");
    }
}
