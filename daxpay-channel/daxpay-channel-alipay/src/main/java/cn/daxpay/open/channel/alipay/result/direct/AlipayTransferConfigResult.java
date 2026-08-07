package cn.daxpay.open.channel.alipay.result.direct;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝转账配置
///
/// 转账配置返回结果对象。冗余展示字段(转出应用名/aliAppId/应用类型)由
/// [cn.daxpay.open.channel.alipay.service.direct.AlipayTransferConfigService] 填充,
/// 不经 MapStruct 自动映射。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝转账配置")
public class AlipayTransferConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "转账转出应用引用")
    private Long transferAppRefId;

    // ===== 冗余展示(由 Service 填充) =====

    @Schema(description = "转出应用名称")
    private String transferAppName;

    @Schema(description = "转出应用支付宝AppId")
    private String aliAppId;

    @Schema(description = "转出应用类型(mini_program-小程序/mobile_app-移动应用/web_app-网站应用)")
    private String appType;
}
