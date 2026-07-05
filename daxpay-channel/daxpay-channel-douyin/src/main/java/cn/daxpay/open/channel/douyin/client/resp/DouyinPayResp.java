package cn.daxpay.open.channel.douyin.client.resp;

import cn.daxpay.open.channel.douyin.client.enums.DouyinPayBodyType;
import lombok.Data;

/// # 抖音通道支付响应
@Data
public class DouyinPayResp {
    /// 商户订单号(回显)
    private String outTradeNo;
    /// 支付内容
    private String payBody;
    /// 支付内容类型
    private DouyinPayBodyType payBodyType;
}
