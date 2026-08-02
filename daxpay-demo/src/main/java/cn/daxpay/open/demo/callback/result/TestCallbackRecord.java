package cn.daxpay.open.demo.callback.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 测试回调接收记录
///
/// 模拟商户端接收平台出站通知([DaxNoticeResult] 签名 JSON)后生成的记录,
/// 暂存于内存供联调时通过 `/test/callback/list` 查看。
@Data
@Accessors(chain = true)
@Schema(title = "测试回调接收记录")
public class TestCallbackRecord {

    /// 记录 ID
    @Schema(description = "记录ID")
    private String id;

    /// 通知事件码(如 pay.success / refund.success)
    @Schema(description = "通知事件码")
    private String event;

    /// 业务类型(pay / refund / unknown, 由 event 前缀解析)
    @Schema(description = "业务类型")
    private String bizType;

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用ID
    @Schema(description = "应用ID")
    private String appId;

    /// 业务号(优先取 tradeNo, 其次 outTradeNo / refundNo / outRefundNo)
    @Schema(description = "业务号")
    private String bizNo;

    /// 金额(分, 如报文携带)
    @Schema(description = "金额(分)")
    private String amount;

    /// 签名验证结果
    @Schema(description = "验签结果")
    private boolean verifyResult;

    /// 接收时间(UTC)
    @Schema(description = "接收时间(UTC)")
    private OffsetDateTime receiveTime;

    /// 原始报文(截断保留, 便于排查)
    @Schema(description = "原始报文")
    private String rawBody;
}
