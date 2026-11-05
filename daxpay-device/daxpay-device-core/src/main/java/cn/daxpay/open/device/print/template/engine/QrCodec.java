package cn.daxpay.open.device.print.template.engine;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.Map;

/// # 二维码编码与模块点数
///
/// 只做三件事: 用 ZXing 编码一次(校验内容合法并取模块数/点阵)、按纸宽推导每模块点数、
/// 导出含静区的点阵(纸面预览旁挂用)。**不做打印光栅化** —— 二维码统一走
/// 固件 `GS ( k` 原生指令, 纠错/静区/绘制由固件负责。
///
/// ## 为什么仍保留 ZXing
/// 一为前置校验: 内容过长在合成期就报错, 不拖到打印时才发现;
/// 二为尺寸推导: 模块数决定放大后的实际宽度, 而固件不返回编码结果, 打印端算不出来;
/// 三为预览点阵: 纸面预览要画出二维码, 只能由平台侧导出矩阵(与打印同一次编码)。
///
/// ## 点数口径
/// 目标宽两档纸宽统一按 58mm 物理口径: (384-24)×0.95=342 点(约 42mm 见方), 封顶 58mm 12 点 /
/// 80mm 16 点(控制票面高度); 下限 3 点, 低于该值说明内容过长, 直接报错而非打出不可扫的糊块。
/// 封顶必须与占宽比联动抬升: 只抬比例会被封顶钳死(2026-10-24 二次上调 0.85/10/14 → 0.95/12/16,
/// 长内容 v10+ 码可见区 51%→64%, 4 dots/模块从逼近极限回到好用)。
/// 2026-10-27 起目标宽不再随纸宽放大: 80mm 按行宽 95% 出码约 65mm 见方, 用户反馈过大,
/// 改与 58mm 落纸尺寸一致(80mm 封顶 16 从此不触顶, 保留仅为公式形态与未来调比例预留)。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 异常文案 messageKey 化。
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QrCodec {

    /// 可扫性下限(每模块点数)
    public static final int MIN_MODULE_DOTS = 3;

    /// 占行宽比例(留边后再缩至 95%)
    private static final double WIDTH_RATIO = 0.95;

    /// 先扣除的两侧留边点数
    private static final int WIDTH_RESERVED_DOTS = 24;

    /// 每模块点数上限(58mm 纸): 仅控制票面高度, 原生指令无传输量约束
    public static final int MAX_MODULE_DOTS_58MM = 12;

    /// 每模块点数上限(80mm 纸)
    public static final int MAX_MODULE_DOTS_80MM = 16;

    /// 内容 → 模块数(含 4 模块静区, 用于宽度推导; 编码失败抛中文异常)
    public static int modules(String content) {
        return matrix(content).length;
    }

    /// 内容 → 含 4 模块静区的点阵(true=黑, 仅纸面预览旁挂用; 编码失败抛中文异常)
    ///
    /// 与 [#modules] 同一次编码: 维度 = 编码宽度 + 8, 与每模块点数([#moduleDots])的
    /// 尺寸口径一致, 保证预览画出的二维码尺寸 = 固件原生指令落纸尺寸。
    public static boolean[][] matrix(String content) {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            ByteMatrix encoded = Encoder.encode(content, ErrorCorrectionLevel.M, hints).getMatrix();
            int n = encoded.getWidth();
            boolean[][] dots = new boolean[n + 8][n + 8];
            for (int y = 0; y < n; y++) {
                for (int x = 0; x < n; x++) {
                    dots[y + 4][x + 4] = encoded.get(x, y) == 1;
                }
            }
            return dots;
        } catch (WriterException e) {
            // 打印: 二维码内容无法编码(过长或含不支持字符)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.device.print.qrNotEncodable");
        }
    }

    /// 模块数 + 纸宽 → 每模块点数(向下取整并封顶, 最小 1; 低于 [MIN_MODULE_DOTS] 表示内容过长)
    ///
    /// 目标宽统一按 58mm 物理口径(342 点), 两档纸宽落纸尺寸一致, 与 PC 端
    /// TemplateLineComposer.QrModuleDots 逐语句镜像
    public static int moduleDots(int modules, int dotsPerLine) {
        int target = (int) ((PrintLineComposer.DOTS_58MM - WIDTH_RESERVED_DOTS) * WIDTH_RATIO);
        int cap = dotsPerLine >= PrintLineComposer.DOTS_80MM ? MAX_MODULE_DOTS_80MM : MAX_MODULE_DOTS_58MM;
        return Math.max(1, Math.min(target / modules, cap));
    }
}
