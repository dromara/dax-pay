package cn.daxpay.open.platform.iam.result.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// 邮件模板(管理端列表行, 含中英两语言各自的出厂默认与运营端覆盖合并后的生效值)
@Data
@Accessors(chain = true)
@Schema(title = "邮件模板")
public class MailTemplateResult {

    /// 模板编码
    @Schema(description = "模板编码")
    private String templateCode;

    /// 场景名(中文主题, 列表展示用)
    @Schema(description = "场景名")
    private String name;

    /// 可用变量声明
    @Schema(description = "可用变量声明")
    private List<VariableResult> variables;

    /// 中文槽位(生效值与出厂默认)
    @Schema(description = "中文槽位")
    private SlotBlock zh;

    /// 英文槽位(生效值与出厂默认)
    @Schema(description = "英文槽位")
    private SlotBlock en;

    /// 模板变量
    @Data
    @Accessors(chain = true)
    @Schema(title = "邮件模板变量")
    public static class VariableResult {

        /// 变量名(占位符书写形式 {变量名})
        @Schema(description = "变量名")
        private String name;

        /// 变量说明
        @Schema(description = "变量说明")
        private String desc;
    }

    /// 单语言槽位块
    @Data
    @Accessors(chain = true)
    @Schema(title = "邮件模板单语言槽位")
    public static class SlotBlock {

        /// 语言(zh/en)
        @Schema(description = "语言")
        private String language;

        /// 是否已自定义覆盖(未覆盖时五槽位均为出厂默认)
        @Schema(description = "是否已自定义覆盖")
        private Boolean customized;

        /// 邮件主题(当前生效值)
        @Schema(description = "邮件主题")
        private String subject;

        /// 邮件标题(当前生效值)
        @Schema(description = "邮件标题")
        private String title;

        /// 正文文案(当前生效值)
        @Schema(description = "正文文案")
        private String content;

        /// 提示行文案(当前生效值)
        @Schema(description = "提示行文案")
        private String tip;

        /// 页脚文案(当前生效值)
        @Schema(description = "页脚文案")
        private String footer;

        /// 出厂默认主题(编辑时"填入默认值"用)
        @Schema(description = "出厂默认主题")
        private String defaultSubject;

        /// 出厂默认标题
        @Schema(description = "出厂默认标题")
        private String defaultTitle;

        /// 出厂默认正文
        @Schema(description = "出厂默认正文")
        private String defaultContent;

        /// 出厂默认提示行
        @Schema(description = "出厂默认提示行")
        private String defaultTip;

        /// 出厂默认页脚
        @Schema(description = "出厂默认页脚")
        private String defaultFooter;
    }
}
