package cn.daxpay.open.channel.alipay.result.direct;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付宝转账场景配置
///
/// 转账场景配置返回结果对象。报备字段元数据([reportInfoTypes]/[reportInfoDescriptions])
/// 由枚举 [cn.daxpay.open.channel.alipay.enums.AlipayTransferSceneEnum] 推导, 供前端动态渲染报备输入框。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝转账场景配置")
public class AlipayTransferSceneConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "转账场景名称")
    private String sceneName;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "是否默认场景")
    private Boolean isDefault;

    @Schema(description = "报备字段定义(支付宝协议固定中文 infoType, 由枚举推导)")
    private List<String> reportInfoTypes;

    @Schema(description = "报备字段说明(与 reportInfoTypes 平行, 含支付宝文档示例)")
    private List<String> reportInfoDescriptions;
}
