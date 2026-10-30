package cn.daxpay.open.device.comm;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付成功设备通知消息体
///
/// 消费端只信任 tradeNo 重新查库, 不信任消息内业务状态。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
@Data
@Accessors(chain = true)
public class DevicePaySuccessMessage {

    /// 资金交易凭证号
    private String tradeNo;
}
