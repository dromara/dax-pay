package cn.daxpay.open.channel.duolabao.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 哆啦宝通道回调验签解析响应(主应用侧)
///
/// 子应用对哆啦宝异步通知验签解析后回传; tradeType 恒为 PAY(退款为同步接口无通知);
/// [#ackBody] 为哆啦宝约定文本应答串(success/error), 主应用原样返回给哆啦宝。
@Data
@Accessors(chain = true)
public class DuolabaoCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(恒为 PAY)
    private String tradeType;

    /// 商户订单号(requestNum = 平台支付交易号)
    private String outTradeNo;

    /// 哆啦宝订单号(orderNum)
    private String outOrderNo;

    /// 交易状态(标准化: SUCCESS / FAIL)
    private String tradeStatus;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 完成时间(completeTime, 东八区)
    private OffsetDateTime finishTime;

    /// 买家标识(subOpenId)
    private String buyerId;

    /// 通道外部交易号(bankOutTradeNum)
    private String outTransNo;

    /// 哆啦宝应答串(success/error 文本, 主应用原样返回)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调明文(用于对账/排障)
    private String syncData;
}
