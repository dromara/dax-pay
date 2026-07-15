package cn.daxpay.open.payment.device.qrcode.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付码牌查询参数
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "支付码牌查询参数")
public class DeviceQrCodeQuery {

    /// 码牌编码
    @Schema(description = "码牌编码")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String code;

    /// 码牌名称
    @Schema(description = "码牌名称")
    private String name;

    /// 批次号
    @Schema(description = "批次号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String batchNo;

    /// 商户号
    @Schema(description = "商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String mchNo;

    /// 落地程序类型
    /// @see cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum
    @Schema(description = "落地程序类型(h5/mini_app)")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String programType;

    /// 金额类型
    /// @see cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum
    @Schema(description = "金额类型")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String amountType;

    /// 状态
    /// @see cn.daxpay.open.payment.device.enums.QrCodeStatusEnum
    @Schema(description = "状态")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String status;
}
