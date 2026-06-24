package cn.daxpay.open.platform.system.result.protocol;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 用户协议版本
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户协议版本")
public class UserProtocolVersionResult extends BaseResult {

    /// 协议ID
    @Schema(description = "协议ID")
    private Long protocolId;

    /// 语言
    @Schema(description = "语言")
    private String language;

    /// 版本号
    @Schema(description = "版本号")
    private Integer versionNo;

    /// 版本标签
    @Schema(description = "版本标签")
    private String versionLabel;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 内容
    @Schema(description = "内容")
    private String content;

    /// 渲染后的HTML
    @Schema(description = "渲染后的HTML")
    private String contentHtml;

    /// 内容格式
    @Schema(description = "内容格式")
    private String contentFormat;

    /// 状态
    @Schema(description = "状态")
    private String status;

    /// 生效时间
    @Schema(description = "生效时间")
    private OffsetDateTime effectiveTime;

    /// 变更说明
    @Schema(description = "变更说明")
    private String summary;
}
