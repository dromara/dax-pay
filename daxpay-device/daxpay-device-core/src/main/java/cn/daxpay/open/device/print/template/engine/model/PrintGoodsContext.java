package cn.daxpay.open.device.print.template.engine.model;

import java.util.List;

/// # 小票商品明细上下文(交易级)
///
/// 由推送链路按交易装配一次, 供该笔支付的全部设备复用(与商户订单号同层级, 不得逐设备查询)。
/// 明细为空时 `goods` 元素输出 0 行、`${goodsTotal}`/`${goodsDiff}` 为空串, 票据自动退化为普通收款小票。
///
/// [sample] 为试打固定样例(3 条, 合计 13.50 元, 与样例金额 12.50 形成 -1.00 差额演示优惠行,
/// 含一条超长品名用于验证截断)。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
public record PrintGoodsContext(List<PrintGoodsItem> items) {

    /// 紧凑构造: null 归一为空列表, 并做不可变拷贝(防止调用方后续改动影响渲染)
    public PrintGoodsContext {
        items = items == null ? List.of() : List.copyOf(items);
    }

    /// 空明细(支付链路未接入商品时使用)
    public static PrintGoodsContext empty() {
        return new PrintGoodsContext(List.of());
    }

    /// 试打样例明细(合计 13.50 元, 与样例金额 12.50 形成 -1.00 差额, 便于演示优惠行)
    public static PrintGoodsContext sample() {
        return new PrintGoodsContext(List.of(
                new PrintGoodsItem("可乐 330ml", 2, 350),
                new PrintGoodsItem("薯片 经典原味", 1, 300),
                new PrintGoodsItem("经典原味薯片家庭分享装大包 500g 超值组合装 限时特惠", 1, 350)));
    }

    /// 是否无明细
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /// 明细合计(分): 全部条目求和(含超出打印条数上限的部分, 合计口径与票面展示无关)
    /// 数量或单价非正的脏数据不参与求和(避免负值把合计拉低)
    public long totalFen() {
        long total = 0L;
        for (PrintGoodsItem item : items) {
            if (item != null && item.quantity() > 0 && item.unitPrice() > 0) {
                total += item.subtotal();
            }
        }
        return total;
    }
}
