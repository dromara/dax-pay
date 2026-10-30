package cn.daxpay.open.device.print.support;

import cn.daxpay.open.device.print.template.engine.model.LineModel;
import cn.daxpay.open.device.print.template.support.QrPreviewBlock;

import java.util.List;

/// # 试打预览出参(纸面/行模型视图 + 版式命中信息)
///
/// 字段与前端预览组件约定的前两件(行模型 + 二维码旁挂点阵), 额外排障字段:
/// - **templateCode/templateName**: 本次命中的内置版式(开源版无模板表, 版式为代码常量,
///   命中即确定, 不再有"指定模板/应用默认"的来源分叉, 见
///   [cn.daxpay.open.device.print.template.PrintTemplateConstants]);
/// - **goodsArea**: 实际渲染版式是否含商品区(goods 元素或商品合计/差额变量), 前端据此显隐
///   「注入样例商品」开关 —— 版式无商品区时注入样例无处渲染, 开关隐藏避免被当成故障。
///
/// 关键约束: 预览走 [cn.daxpay.open.device.print.service.PrintPushService] 的版式解析与变量组装
/// (该设备的真实商户/应用/门店/纸宽), 不走样例变量池 —— 否则"预览好看、真机溢版", 预览就失去意义。
public record TestPrintPreviewResult(
        LineModel lineModel,
        List<QrPreviewBlock> qrs,
        String templateCode,
        String templateName,
        Integer paperWidth,
        boolean goodsArea) {
}
