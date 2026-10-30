package cn.daxpay.open.device.print.template;

import cn.daxpay.open.device.print.template.engine.PrintTemplateParser;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/// # 打印版式内置常量表
///
/// 开源版不移植打印模板表与 CRUD(拍板 2): 版式为代码内置 JSON 常量, 首期一套
/// 「标准收款小票」(内容自 plus `_config/sql/data.sql` 平台预置模板 `pay_print_template`
/// id=1800000000000000901 原样拷贝, 快照 773134af)。`resolveTemplate(templateCode)` 查本表,
/// 无表无 CRUD; 应用配置(`pay_mch_app_device_config.template_code`)只存代码, 下拉为枚举扩展位。
///
/// 常量内容在类加载时过一遍 [PrintTemplateParser#parse] 快速自检: 内容损坏(手改常量引入)
/// 会在首个调用点抛出明确异常, 而不是渲染出静默残票。
public final class PrintTemplateConstants {

    /// 标准收款小票版式代码(应用配置缺省值)
    public static final String CODE_STANDARD = "standard";

    /// 标准收款小票(plus 平台预置模板原样拷贝: 票头双宽商户名 + 金额 + 三要素 + 单号条码)
    static final String STANDARD_CONTENT = """
            {"elements":[{"type":"text","text":"${mchName}","align":"center","bold":true,"size":"double"},{"type":"text","text":"收款小票","align":"center"},{"type":"divider"},{"type":"amount"},{"type":"divider"},{"type":"kv","label":"收款方式","value":"${channelName}"},{"type":"kv","label":"业务单号","value":"${bizNo}"},{"type":"kv","label":"支付时间","value":"${paidTime}"},{"type":"divider"},{"type":"barcode","value":"${bizNo}"},{"type":"feed","lines":4},{"type":"cut"}]}""";

    /// 全部内置版式(LinkedHashMap 保序, code → 定义)
    private static final Map<String, TemplateDef> TEMPLATES = buildTemplates();

    private PrintTemplateConstants() {
    }

    private static Map<String, TemplateDef> buildTemplates() {
        Map<String, TemplateDef> templates = new LinkedHashMap<>();
        register(templates, new TemplateDef(CODE_STANDARD, "标准收款小票", STANDARD_CONTENT));
        return Collections.unmodifiableMap(templates);
    }

    /// 注册前自检: 常量内容必须是合法模板(手改常量引入损坏在加载期暴露)
    private static void register(Map<String, TemplateDef> templates, TemplateDef def) {
        PrintTemplateParser.parse(def.content());
        templates.put(def.code(), def);
    }

    /// 全部内置版式(应用配置版式下拉用)
    public static Collection<TemplateDef> all() {
        return TEMPLATES.values();
    }

    /// 版式代码是否存在(配置保存校验用)
    public static boolean exists(String templateCode) {
        return templateCode != null && TEMPLATES.containsKey(templateCode);
    }

    /// 按代码取版式, 不存在/空白抛错(同步运营操作用)
    public static TemplateDef requireByCode(String templateCode) {
        TemplateDef def = templateCode == null ? null : TEMPLATES.get(templateCode);
        if (def == null) {
            // 打印: 打印版式不存在: {0}
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.print.templateNotFound", templateCode);
        }
        return def;
    }

    /// 按代码取版式, 空白/未登记回落标准版式(支付旁路兜底用: 未知代码不阻断播报打印)
    public static TemplateDef resolveOrDefault(String templateCode) {
        if (templateCode == null || templateCode.isBlank()) {
            return TEMPLATES.get(CODE_STANDARD);
        }
        TemplateDef def = TEMPLATES.get(templateCode);
        return def == null ? TEMPLATES.get(CODE_STANDARD) : def;
    }

    /// 内置版式定义(代码 + 展示名 + content JSON)
    public record TemplateDef(String code, String name, String content) {
    }
}
