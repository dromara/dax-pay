package cn.daxpay.open.channel.ums.result.direct;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务直连通道商户绑定返回结果
@Data
@Accessors(chain = true)
public class UmsDirectChannelMerchantResult {

    /// 通道商户号
    private String channelMchNo;

    /// 所属支付产品
    private String product;

    /// 银联商务商户号
    private String merchantNo;

    /// 终端号
    private String terminalNo;

    /// 订单号前缀
    private String orderPrefix;

    /// 是否沙箱环境
    private boolean sandbox;
}
