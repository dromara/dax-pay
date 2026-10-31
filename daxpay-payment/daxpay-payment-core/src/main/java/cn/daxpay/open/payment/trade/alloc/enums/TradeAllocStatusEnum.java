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
/// - processing: 分账中(存在处理中的分账单/完结单)
/// - allocated: 已分账未完结(多次分账中间态, 剩余冻结资金仍可继续分账或完结)
/// - finished: 已分账且已完结(分账即完结或显式完结后的终态)
/// - done: 历史存量值(单次分账语义的旧终态, 仅读兼容, 新代码不再写入)
///
/// 状态迁移:
/// - 单次模式(分账即完结): none → processing → finished
/// - 多次模式(需显式完结): none → processing → allocated → processing → ... → allocated → processing(完结单) → finished
/// - processing → none/allocated(发起失败按是否已有成功分账回退); null / unsupported / finished / done 为终态。
/// 字典: trade_alloc_status
@Getter
@RequiredArgsConstructor
public enum TradeAllocStatusEnum implements I18nSupport {

    /// 待分账(下单时初始化, 可发起)
    NONE("none"),
    /// 不支持分账(请求了分账但产品不支持, 已降级普通收款, 不可发起)
    UNSUPPORTED("unsupported"),
    /// 分账中(存在处理中的分账单/完结单)
    PROCESSING("processing"),
    /// 已分账未完结(剩余冻结资金可继续分账或发起完结)
    ALLOCATED("allocated"),
    /// 已分账且已完结(终态)
    FINISHED("finished"),
    /// 已分账(历史存量终态, 单次分账语义, 仅读兼容不写入)
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

    /// 是否终态(已完结/旧已分账/不支持分账)
    public boolean isTerminal() {
        return this == FINISHED || this == DONE || this == UNSUPPORTED;
    }

    /// 是否已完结语义(含历史 done 存量)
    public boolean isFinishedLike() {
        return this == FINISHED || this == DONE;
    }
}
