package cn.daxpay.open.channel.ysep.client.req;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import lombok.Data;

/// # 银盛通道订单查询请求(主应用侧, 与子应用镜像)
@Data
public class YsepSyncReq {

    private YsepSdkCredential credential;

    /// 产品编码(ysep_pay / yseqt_pay)
    private String product;

    /// 商户订单号(与 tradeNo 二选一)
    private String outTradeNo;

    /// 银盛交易单号(与 outTradeNo 二选一, yseqt 族不使用)
    private String tradeNo;

    /// 原下单日期(yyyyMMdd, ysep_pay shopdate 须与交易时一致)
    private String tradeDate;
}
