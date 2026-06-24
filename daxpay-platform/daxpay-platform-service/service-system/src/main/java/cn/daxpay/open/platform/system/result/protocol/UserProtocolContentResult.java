package cn.daxpay.open.platform.system.result.protocol;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 用户协议内容(对外展示)
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户协议内容")
public class UserProtocolContentResult extends BaseResult {

    /// 名称
    @Schema(description = "名称")
    private String name;

    /// 显示名称
    @Schema(description = "显示名称")
    private String showName;

    /// 类型
    @Schema(description = "类型")
    private String type;

    /// 端类型
    @Schema(description = "端类型")
    private String clientType;

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

    /// 生效时间
    @Schema(description = "生效时间")
    private OffsetDateTime effectiveTime;
}
