package cn.daxpay.open.channel.sheng.client.req;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import lombok.Data;

/// # 盛付通通道关单请求(主应用侧, 与子应用镜像)
///
/// useCancel 语义透传: true=当日撤单(reverseOrder), false=关单(closeOrder)。
@Data
public class ShengCloseReq {

    private ShengSdkCredential credential;

    /// 是否撤单(true=当日撤销 reverseOrder, false=关单 closeOrder)
    private boolean useCancel;

    /// 原商户订单号(与 originTradeNo 二选一)
    private String originOutTradeNo;

    /// 原盛付通交易号(与 originOutTradeNo 二选一)
    private String originTradeNo;
}
