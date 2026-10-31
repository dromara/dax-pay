package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账能力位
///
/// 通道分账策略([cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy#getCapabilities])声明的能力集合,
/// 取代"实现了策略即支持分账"的单白名单判定。
/// 基础能力(发起订单分账)由策略存在性判定, 本枚举只描述增量能力:
/// - multi: 同一支付订单支持多次分账(剩余资金持续冻结)
/// - finish: 支持独立完结(解冻剩余资金)调用
/// - balance: 支持余额分账(不挂订单、从商户余额账户直接划款, P1 规划)
///
/// 未声明 multi 的通道为"单次即完结"档: 首次分账即解冻剩余资金, 二次分账请求被前置校验拒绝。
@Getter
@RequiredArgsConstructor
public enum AllocCapabilityEnum implements I18nSupport {

    /// 多次分账(同一订单可多次发起, 剩余资金保持冻结)
    MULTI("multi"),
    /// 独立完结(解冻剩余资金)
    FINISH("finish"),
    /// 余额分账(不挂订单从商户余额账户划款, P1 规划)
    BALANCE("balance"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_capability";
    }

    /// 根据编码获取枚举
    public static AllocCapabilityEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 该分账能力不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.allocCapabilityNotExist", code));
    }
}
