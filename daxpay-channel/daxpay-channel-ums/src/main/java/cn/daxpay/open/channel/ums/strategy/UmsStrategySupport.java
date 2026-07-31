package cn.daxpay.open.channel.ums.strategy;

import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;

/// # 银联商务产品策略辅助
///
/// 按支付产品决定 close/sync/refund/refundSync 下发给子应用的通道支付方式([UmsPayMethod])。
///
/// 银联商务子应用对关单/查单/退款/退款查询采用扫码与 H5 二分接口:
/// - 扫码类产品([ProductEnum#UMS_QRCODE]/[ProductEnum#UMS_BARCODE])走 bills 系列(需 [UmsPayMethod#QRCODE])
/// - H5 类产品([ProductEnum#UMS_H5]/[ProductEnum#UMS_JSAPI]/[ProductEnum#UMS_MINI]/[ProductEnum#UMS_APP])
///   走 netpay 系列(任一非 QRCODE 值即可, 子应用按 isQR 二分)
public final class UmsStrategySupport {

    private UmsStrategySupport() {
    }

    /// 按产品解析关单/查单/退款/退款查询时下发给子应用的通道支付方式
    public static UmsPayMethod resolveCloseSyncMethod(ProductEnum product) {
        // 扫码类(含被扫)走 bills 接口; 其余 H5 类走 netpay 接口, 子应用按 isQR 二分
        return (product == ProductEnum.UMS_QRCODE || product == ProductEnum.UMS_BARCODE)
                ? UmsPayMethod.QRCODE
                : UmsPayMethod.ALIPAY_H5;
    }
}
