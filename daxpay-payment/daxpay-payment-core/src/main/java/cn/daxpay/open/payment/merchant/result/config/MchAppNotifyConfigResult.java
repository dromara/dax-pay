package cn.daxpay.open.payment.merchant.result.config;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户应用事件通知配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户应用事件通知配置结果")
public class MchAppNotifyConfigResult extends BaseResult {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用ID
    @Schema(description = "应用ID")
    private String appId;

    /// 回调地址
    @Schema(description = "回调地址")
    private String notifyUrl;

    /// 通知方式
    @Schema(description = "通知方式")
    private String notifyWay;

    /// 订阅事件类型
    @Schema(description = "订阅事件类型")
    private String subscribedEvents;

    /// 启用状态
    @Schema(description = "启用状态")
    private Boolean status;

    /// 备注
    @Schema(description = "备注")
    private String remark;
}
