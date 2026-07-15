package cn.daxpay.open.payment.unipay.client.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 码牌支付信息(公开接口返回, 脱敏)
///
/// 供 H5 码牌支付页(/h/:code)扫码后查询展示, 不含 appId/mchNo 等内部字段。
@Data
@Accessors(chain = true)
@Schema(title = "码牌支付信息")
public class CodePayInfoResult {

    @Schema(description = "码牌编码")
    private String code;

    @Schema(description = "码牌名称")
    private String name;

    /// 金额类型
    /// @see cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum
    @Schema(description = "金额类型(random-自定义/fixed-固定)")
    private String amountType;

    @Schema(description = "固定金额(分)")
    private Long fixedAmount;
}
