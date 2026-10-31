package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账单类型
///
/// pay_alloc_order.alloc_type 的取值集合。
/// - alloc: 分账单(向接收方拆分资金, 可多次发起)
/// - finish: 完结单(解冻剩余冻结资金给商户本身, 无接收方明细, 每单最多一张成功完结单)
///
/// 多次分账模型: 同一支付订单可存在多张 alloc 单 + 至多一张成功的 finish 单;
/// 单次即完结通道(如微信 unfreeze_unsplit)发起 alloc 时即随单解冻, 无需 finish 单。
@Getter
@RequiredArgsConstructor
public enum AllocOrderTypeEnum implements I18nSupport {

    /// 分账单(向接收方拆分资金)
    ALLOC("alloc"),
    /// 完结单(解冻剩余冻结资金给商户)
    FINISH("finish"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_order_type";
    }

    /// 根据编码获取枚举
    public static AllocOrderTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 该分账单类型不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.allocOrderTypeNotExist", code));
    }
}
