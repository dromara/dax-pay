package cn.daxpay.open.device.print.template.engine.model;

/// # 小票商品明细项
///
/// 渲染引擎的轻量明细模型(与支付域商品明细解耦, 由推送链路在装配时映射):
/// 名称 / 数量 / 单价(分)。小计由 [subtotal] 现算——入参本身不带小计。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
///
/// @param name      商品名称(空则该条不打印)
/// @param quantity 数量(<=0 则该条不打印)
/// @param unitPrice 单价(分, <=0 则该条不打印)
public record PrintGoodsItem(String name, int quantity, long unitPrice) {

    /// 行小计(分): 数量 × 单价
    public long subtotal() {
        return (long) quantity * unitPrice;
    }
}
