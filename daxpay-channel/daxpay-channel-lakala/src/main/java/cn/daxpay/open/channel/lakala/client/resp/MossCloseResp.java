package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;

/// # 拉卡拉 MOSS 关单响应(主应用侧, 与子应用镜像)
///
/// MOSS 无远端关单接口, 关单语义为「查单确认非成功 + order_eff_time 自然过期」, 无业务数据。
@Data
public class MossCloseResp {
}
