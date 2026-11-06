package cn.daxpay.open.payment.elf.result.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 精灵小票模板选项结果
///
/// 开源版暂不提供平台模板管理, 返回空清单由桌面端回退内置模板;
/// 契约与商业版保持一致, 后续接入设备域模板时桌面端无需改动。
@Data
@Accessors(chain = true)
@Schema(title = "精灵小票模板选项结果")
public class ElfPrintTemplateOptionsResult {

    /// 模板总数
    @Schema(description = "模板总数")
    private Integer total;

    /// 当前应用默认模板ID
    @Schema(description = "当前应用默认模板ID")
    private String activeTemplateId;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 应用名称
    @Schema(description = "应用名称")
    private String appName;

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称
    @Schema(description = "商户名称")
    private String mchName;

    /// 模板清单
    @Schema(description = "模板清单")
    private List<TemplateOption> templates;

    /// 模板选项
    @Data
    @Accessors(chain = true)
    @Schema(title = "精灵小票模板选项")
    public static class TemplateOption {

        /// 模板ID
        @Schema(description = "模板ID")
        private String templateId;

        /// 模板名称
        @Schema(description = "模板名称")
        private String name;

        /// 来源码
        @Schema(description = "来源码")
        private String origin;

        /// 来源名称
        @Schema(description = "来源名称")
        private String originName;

        /// 场景
        @Schema(description = "场景")
        private String scene;

        /// 更新时间
        @Schema(description = "更新时间")
        private String updatedAt;

        /// 模板内容
        @Schema(description = "模板内容")
        private String content;

        /// 内容哈希
        @Schema(description = "内容哈希")
        private String contentHash;
    }
}
