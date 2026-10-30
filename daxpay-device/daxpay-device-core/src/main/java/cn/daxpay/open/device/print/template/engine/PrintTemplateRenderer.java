package cn.daxpay.open.device.print.template.engine;

import cn.daxpay.open.device.print.template.engine.model.LineModel;
import cn.daxpay.open.device.print.template.engine.model.PrintGoodsContext;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/// # 打印模板渲染门面
///
/// 平台侧渲染(拍板定稿): 模板 content + 数据变量 + 商品明细上下文 → [LineModel]。
/// 模板层止步于行模型——预览与打印同源: 预览出口与推送链路消费同一条合成管线,
/// 指令字节流的编译在编排层(print.service)完成, 预览保真由构造保证。
///
/// 商品合计/差额在**变量替换之前**注入: `${goodsTotal}` / `${goodsDiff}` 是派生变量,
/// 若放到替换之后再算, 模板里这两个占位符会被替换成空串。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
public final class PrintTemplateRenderer {

    private PrintTemplateRenderer() {
    }

    /// 渲染模板为行模型(无商品明细): 解析校验 → 变量替换 → 行合成
    public static LineModel preview(String content, Map<String, String> vars, int paperWidth) {
        return preview(content, vars, PrintGoodsContext.empty(), paperWidth);
    }

    /// 预览模板(含商品明细)
    public static LineModel preview(String content, Map<String, String> vars,
                                    PrintGoodsContext goods, int paperWidth) {
        return compose(content, vars, goods, paperWidth);
    }

    /// 解析校验 + 派生变量注入 + 变量替换 + 行合成
    private static LineModel compose(String content, Map<String, String> vars,
                                     PrintGoodsContext goods, int paperWidth) {
        List<PrintTemplateElement> elements = PrintTemplateParser.parse(content);
        Map<String, String> effectiveVars = withGoodsVars(vars, goods);
        List<PrintTemplateElement> resolved = PrintTemplateParser.resolve(elements, effectiveVars);
        return PrintLineComposer.compose(resolved, paperWidth, goods);
    }

    /// 注入商品派生变量(先替换后算会导致占位符为空, 顺序不可颠倒)
    ///
    /// - `${goodsTotal}`: 明细合计(元), 无明细为空串
    /// - `${goodsDiff}`: 实付金额 − 明细合计(带符号), 无明细、金额缺失或差额为 0 时为空串
    static Map<String, String> withGoodsVars(Map<String, String> vars, PrintGoodsContext goods) {
        Map<String, String> effective = new LinkedHashMap<>();
        if (vars != null) {
            effective.putAll(vars);
        }
        long goodsTotalFen = goods == null ? 0L : goods.totalFen();
        effective.put(PrintVars.GOODS_TOTAL,
                goodsTotalFen > 0 ? PrintVars.asAmountYuan(goodsTotalFen) : "");
        effective.put(PrintVars.GOODS_DIFF, goodsDiffText(vars, goodsTotalFen));
        return effective;
    }

    /// 差额文本: 实付 − 明细合计, 负数带减号; 无明细/无金额/差额为零时返回空串(配合 kv.skipIfEmpty 隐藏整行)
    private static String goodsDiffText(Map<String, String> vars, long goodsTotalFen) {
        if (goodsTotalFen <= 0 || vars == null) {
            return "";
        }
        long amountFen = parseFen(vars.get(PrintVars.AMOUNT_FEN));
        if (amountFen <= 0) {
            return "";
        }
        long diff = amountFen - goodsTotalFen;
        if (diff == 0) {
            return "";
        }
        String abs = PrintVars.asAmountYuan(Math.abs(diff));
        return diff < 0 ? "-" + abs : abs;
    }

    /// 金额变量容错解析(非法值按 0 处理, 不影响整票渲染)
    private static long parseFen(String value) {
        if (value == null || value.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
