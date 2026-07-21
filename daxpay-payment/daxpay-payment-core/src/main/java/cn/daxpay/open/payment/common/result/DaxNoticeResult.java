package cn.daxpay.open.payment.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/// # 商户出站通知通用报文
///
/// 在 [DaxResult] 基础上补充 event / protocol / mchNo / appId，便于商户区分业务事件
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(title = "商户出站通知通用报文")
public class DaxNoticeResult<T> extends DaxResult<T> {

    /// 通知事件码（如 pay.success）
    @Schema(description = "通知事件码")
    private String event;

    /// 通知协议（system / easy_pay）
    @Schema(description = "通知协议")
    private String protocol;

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用ID
    @Schema(description = "应用ID")
    private String appId;

    public DaxNoticeResult(int successCode, T data, String msg) {
        super(successCode, data, msg);
    }

    public DaxNoticeResult(int successCode, String msg) {
        super(successCode, msg);
    }
}
