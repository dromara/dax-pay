package cn.daxpay.open.channel.douyin.client.req;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import lombok.Data;

/// # 抖音通道退款同步请求
@Data
public class DouyinRefundSyncReq {
    /// 退款单号
    private String outRefundNo;
    /// 通道调用凭证
    private DouyinSdkCredential credential;
}
