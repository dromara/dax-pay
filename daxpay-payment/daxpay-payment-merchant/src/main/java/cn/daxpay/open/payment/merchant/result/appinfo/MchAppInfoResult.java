package cn.daxpay.open.payment.merchant.result.appinfo;

import cn.daxpay.open.platform.core.result.BaseResult;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户应用信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户应用信息")
public class MchAppInfoResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 应用名称
    @Schema(description = "应用名称")
    private String appName;

    /// 状态
    /// @see MchAppStatusEnum
    @Schema(description = "状态")
    private String status;

    /// 默认应用
    @Schema(description = "默认应用")
    private boolean defaultApp;

}

