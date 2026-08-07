package cn.daxpay.open.channel.douyin.result.direct;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音转账配置
///
/// 转账配置返回结果对象。冗余展示字段(发起应用名/douyinAppId/应用类型)由
/// [cn.daxpay.open.channel.douyin.service.direct.DouyinTransferConfigService] 填充,
/// 不经 MapStruct 自动映射。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "抖音转账配置")
public class DouyinTransferConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "转账发起应用引用")
    private Long transferAppRefId;

    // ===== 冗余展示(由 Service 填充) =====

    @Schema(description = "发起应用名称")
    private String transferAppName;

    @Schema(description = "发起应用 AppId(真实抖音应用 AppId)")
    private String douyinAppId;

    @Schema(description = "发起应用类型(须为 web_app 网站应用)")
    private String appType;
}
