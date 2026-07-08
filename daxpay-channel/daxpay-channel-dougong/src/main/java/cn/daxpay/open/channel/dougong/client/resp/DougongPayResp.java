package cn.daxpay.open.channel.dougong.client.resp;

import cn.daxpay.open.channel.dougong.client.enums.DougongPayBodyType;
import lombok.Data;

/// # 斗拱通道支付响应(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongPayResp` 镜像, 字段对齐。
@Data
public class DougongPayResp {

    /// 商户订单号
    private String outTradeNo;

    /// 汇付流水号(hf_seq_id)
    private String tradeNo;

    /// 支付内容
    private String payBody;

    /// 支付内容类型
    private DougongPayBodyType payBodyType;

    /// 是否已终态完成
    private Boolean complete;
}
