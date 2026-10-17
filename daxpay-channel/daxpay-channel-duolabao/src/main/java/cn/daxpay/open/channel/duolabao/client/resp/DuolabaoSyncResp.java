package cn.daxpay.open.channel.duolabao.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 哆啦宝通道订单同步响应(主应用侧)
///
/// tradeState 为哆啦宝原始订单状态(status: SUCCESS=成功 / FAIL=失败 / 其他=处理中)。
@Data
@Accessors(chain = true)
public class DuolabaoSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 哆啦宝订单号(orderInfoResModel.num)
    private String outOrderNo;

    /// 哆啦宝订单状态原始码(status)
    private String tradeState;

    /// 用户实付金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(成功时返回 completeTime)
    private OffsetDateTime finishTime;

    /// 买家标识(成功时返回 subOpenId)
    private String buyerId;

    /// 通道外部交易号(成功时返回 bankOutTradeNum)
    private String outTransNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
