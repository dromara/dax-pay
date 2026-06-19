package cn.daxpay.open.channel.douyin.result.direct;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连商户应用结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连商户应用结果")
public class DouyinDirectAppResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "抖音应用AppId(APPID)")
    private String douyinAppId;

    @Schema(description = "应用类型")
    private String appType;
}
