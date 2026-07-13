package cn.daxpay.open.payment.merchant.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户应用事件通知配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户应用事件通知配置参数")
public class MchAppNotifyConfigParam {

    /// 应用ID
    @Schema(description = "应用ID")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    private String appId;

    /// 回调地址
    @Schema(description = "回调地址(https)")
    @Size(max = 255, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;

    /// 通知方式
    @Schema(description = "通知方式(http-HTTP异步回调)")
    private String notifyWay;

    /// 订阅事件类型
    @Schema(description = "订阅事件类型(逗号分隔,TradeTypeEnum的code)")
    private String subscribedEvents;

    /// 启用状态
    @Schema(description = "启用状态")
    private Boolean status;

    /// 备注
    @Schema(description = "备注")
    @Size(max = 255, message = "{validation.field.remark.size}")
    private String remark;
}
