package cn.daxpay.open.payment.merchant.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 网关收银台支付项结果
@Data
@Accessors(chain = true)
@Schema(title = "网关收银台支付项结果")
public class GatewayCashierItemResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "收银台类型: h5/web")
    private String cashierType;

    @Schema(description = "H5终端场景; WEB为空")
    private String scene;

    @Schema(description = "前台展示名称")
    private String name;

    @Schema(description = "图标编码")
    private String icon;

    @Schema(description = "是否推荐")
    private Boolean recommend;

    @Schema(description = "排序号")
    private Integer sortNo;

    @Schema(description = "解析模式: method/direct")
    private String resolveMode;

    @Schema(description = "支付方式(METHOD模式)")
    private String method;

    @Schema(description = "通道商户号(DIRECT模式)")
    private String channelMchNo;

    @Schema(description = "支付能力(DIRECT模式)")
    private String capability;

    @Schema(description = "版本号")
    private Integer version;
}
