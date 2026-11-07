package cn.daxpay.open.payment.device.terminal.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端报备扩展字段定义
///
/// 各通道策略通过 [cn.daxpay.open.payment.device.terminal.strategy.AbsChannelTerminalStrategy#getExtraFields]
/// 声明本通道的差异化报备参数, 运营端按此元数据渲染结构化表单(不向用户直接暴露 extra JSON);
/// 存储仍为通道终端的 extra JSON 字段, 由前端按字段定义组装。
@Data
@Accessors(chain = true)
public class TerminalExtraField {

    /// 字段键(即 extra JSON 的 key, 各通道语义见策略实现)
    private String key;

    /// 字段标签(表单展示名, 通道协议字段名, 随策略下发)
    private String label;

    /// 控件类型: input 单行 / textarea 多行
    private String type;

    /// 是否必填
    private Boolean required;

    /// 占位提示
    private String placeholder;

    /// 字段说明(灰色辅助文案)
    private String desc;

    /// 单行输入快捷构造
    public static TerminalExtraField input(String key, String label, String placeholder) {
        return new TerminalExtraField()
                .setKey(key)
                .setLabel(label)
                .setType("input")
                .setRequired(false)
                .setPlaceholder(placeholder);
    }

    /// 多行输入快捷构造
    public static TerminalExtraField textarea(String key, String label, String placeholder) {
        return new TerminalExtraField()
                .setKey(key)
                .setLabel(label)
                .setType("textarea")
                .setRequired(false)
                .setPlaceholder(placeholder);
    }
}
