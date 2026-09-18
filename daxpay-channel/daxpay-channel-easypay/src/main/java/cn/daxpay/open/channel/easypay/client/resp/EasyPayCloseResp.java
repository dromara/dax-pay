package cn.daxpay.open.channel.easypay.client.resp;

import lombok.Data;

/// # 易支付通道关单响应
///
/// 易支付关单接口无业务回执字段, 关单受理即视为成功。
/// 注意: 该接口在部分易支付平台部署中不可用, 主应用侧按移植约定容错处理(异常亦视为已关闭)。
@Data
public class EasyPayCloseResp {
}
