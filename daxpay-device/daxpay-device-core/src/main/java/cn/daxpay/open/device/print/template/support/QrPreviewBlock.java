package cn.daxpay.open.device.print.template.support;

/// # 二维码预览旁挂点阵
///
/// 行模型的 qrcode 行只携带内容与模块点数, 不携带点阵; 纸面预览要画二维码,
/// 由平台侧用 ZXing 导出含 4 模块静区的点阵旁挂在预览结果里(与打印同一次编码口径)。
///
/// @param lineIndex 所属行下标(行模型 lines 下标, 前端定位插入位置)
/// @param modules   点阵边长(含静区)
/// @param matrix    点阵(true=黑)
public record QrPreviewBlock(int lineIndex, int modules, boolean[][] matrix) {
}
