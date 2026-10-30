package cn.daxpay.open.device.print.support;

import cn.daxpay.open.device.print.PrintTerminal;

/// # 纸宽覆盖视图(双流备宽渲染用)
///
/// 除纸宽外全部委托原终端的 [PrintTerminal] 包装: 供
/// [cn.daxpay.open.device.print.service.PrintPushService] 按指定纸宽把同一设备再渲染一遍,
/// 得到与主流同模板同变量的备宽字节流(虚拟设备实际纸宽平台不可知, 推送时 58/80 双流下发,
/// 终端按本地纸宽设置挑流)。不修改台账、不影响主流渲染。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
public record PaperWidthView(PrintTerminal delegate, int paperWidthMm) implements PrintTerminal {

    /// 80mm 视图工厂(备宽流固定 80: 80mm 纸下 58 版式只占约 2/3 纸宽, 二维码明显偏小)
    public static PaperWidthView w80(PrintTerminal delegate) {
        return new PaperWidthView(delegate, 80);
    }

    @Override
    public String getDeviceNo() {
        return delegate.getDeviceNo();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getAlias() {
        return delegate.getAlias();
    }

    @Override
    public Integer getPaperWidth() {
        return paperWidthMm;
    }

    @Override
    public String getMchNo() {
        return delegate.getMchNo();
    }
}
