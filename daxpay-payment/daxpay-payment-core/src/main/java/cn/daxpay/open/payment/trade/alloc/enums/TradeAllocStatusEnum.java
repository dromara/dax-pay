package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 交易分账状态
///
/// pay_trade.alloc_status 的取值集合, 标识原支付交易的分账生命周期。
/// 字段为 null 表示普通(非分账)订单, 不设枚举值表达。
/// - none: 分账订单待发起(下单时产品支持分账)
/// - unsupported: 请求了分账但支付产品不支持, 已降级普通收款(终态, 不可发起)
/// - processing: 分账中
/// - done: 已分账(终态)
///
/// 状态迁移: none → processing → done; processing → none(发起失败回退允许重试);
/// null / unsupported / done 为终态。
/// 字典: trade_alloc_status
@Getter
@RequiredArgsConstructor
public enum TradeAllocStatusEnum implements I18nSupport {

    /// 待分账(下单时初始化, 可发起)
    NONE("none"),
    /// 不支持分账(请求了分账但产品不支持, 已降级普通收款, 不可发起)
    UNSUPPORTED("unsupported"),
    /// 分账中
    PROCESSING("processing"),
    /// 已分账
    DONE("done"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.trade_alloc_status";
    }

    /// 根据编码获取枚举
    public static TradeAllocStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 该交易分账状态不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.tradeAllocStatusNotExist", code));
    }

    /// 是否终态(已分账/不支持分账)
    public boolean isTerminal() {
        return this == DONE || this == UNSUPPORTED;
    }
}
