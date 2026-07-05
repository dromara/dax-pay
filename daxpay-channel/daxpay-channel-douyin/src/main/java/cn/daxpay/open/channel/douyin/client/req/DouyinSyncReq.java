package cn.daxpay.open.channel.douyin.client.req;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import lombok.Data;

/// # 抖音通道支付同步请求
@Data
public class DouyinSyncReq {
    /// 商户订单号
    private String outTradeNo;
    /// 通道调用凭证
    private DouyinSdkCredential credential;
}
