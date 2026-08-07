package cn.daxpay.open.channel.wechat.result.direct;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信转账配置
///
/// 转账配置返回结果对象。冗余展示字段(发起应用名/wxAppId/应用类型/场景名)由
/// [cn.daxpay.open.channel.wechat.service.direct.WechatTransferConfigService] 填充,
/// 不经 MapStruct 自动映射。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信转账配置")
public class WechatTransferConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "转账场景ID")
    private String transferScene;

    @Schema(description = "转账发起应用引用")
    private Long transferAppRefId;

    // ===== 冗余展示(由 Service 填充) =====

    @Schema(description = "场景名称(枚举推导, 便于展示)")
    private String sceneName;

    @Schema(description = "发起应用名称")
    private String transferAppName;

    @Schema(description = "发起应用 AppId(真实微信 AppId)")
    private String wxAppId;

    @Schema(description = "发起应用类型(须为 official_account 公众号)")
    private String appType;
}
