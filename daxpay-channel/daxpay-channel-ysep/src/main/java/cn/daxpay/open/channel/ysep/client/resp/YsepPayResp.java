package cn.daxpay.open.channel.ysep.client.resp;

import cn.daxpay.open.channel.ysep.client.enums.YsepPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 银盛通道支付响应(主应用侧, 与子应用镜像)
@Data
@Accessors(chain = true)
public class YsepPayResp {

    /// 商户订单号
    private String outTradeNo;

    /// 银盛交易单号(可能为空, 以回调/查单为准)
    private String tradeNo;

    /// 支付内容
    private String payBody;

    /// 支付内容类型
    private YsepPayBodyType payBodyType;

    /// 是否已终态完成(银盛主扫类恒为 false)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 买家标识
    private String buyerId;
}
