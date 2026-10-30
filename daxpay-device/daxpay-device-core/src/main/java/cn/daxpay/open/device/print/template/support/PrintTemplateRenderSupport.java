package cn.daxpay.open.device.print.template.support;

import cn.daxpay.open.device.print.template.engine.QrCodec;
import cn.daxpay.open.device.print.template.engine.model.LineModel;

import java.util.ArrayList;
import java.util.List;

/// # 打印模板渲染辅助(预览旁挂点阵)
///
/// plus 侧本类还承担模板保存前校验(validateAndNormalize), 开源版无模板 CRUD
/// (版式为代码内置常量 [cn.daxpay.open.device.print.template.PrintTemplateConstants],
/// 内容随代码评审把关), 仅保留预览旁挂点阵导出。
public final class PrintTemplateRenderSupport {

    private PrintTemplateRenderSupport() {
    }

    /// 行模型中的 qrcode 行 → 预览旁挂点阵(行模型 qrcode 行不携带点阵, 预览出口在此补齐)
    public static List<QrPreviewBlock> qrBlocks(LineModel lineModel) {
        List<QrPreviewBlock> blocks = new ArrayList<>();
        List<LineModel.Line> lines = lineModel.lines();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i) instanceof LineModel.QrLine qr) {
                boolean[][] matrix = QrCodec.matrix(qr.content());
                blocks.add(new QrPreviewBlock(i, matrix.length, matrix));
            }
        }
        return blocks;
    }
}
