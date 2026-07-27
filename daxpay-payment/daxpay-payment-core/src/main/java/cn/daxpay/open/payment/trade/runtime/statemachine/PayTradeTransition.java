package cn.daxpay.open.payment.trade.runtime.statemachine;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;

import java.util.Map;
import java.util.Set;

/// # 资金交易状态转换守卫
///
/// 集中定义 [PayTrade.status] 的所有合法转换路径，杜绝分散在各 Service 中的
/// 手写状态校验。任何状态变更前须经 [assertLegal] 校验，确保不会出现非法跳变。
///
/// 合法转换一览：
/// <pre>
/// INIT       → PROCESSING, CLOSE
/// PROCESSING → SUCCESS, FAIL, CLOSE, CANCEL
/// FAIL       → SUCCESS    （同步纠正：本地误判 FAIL 后由通道查询翻转）
/// CLOSE      → SUCCESS    （超时关单安全化后：通道实际已付款，同步纠正）
/// SUCCESS    → （终态）
/// CANCEL     → （终态）
/// </pre>
///
/// 使用方式：
/// 1. 编程守卫：变更前调用 [assertLegal]，非法转换直接抛异常（防编程错误）
/// 2. CAS 前置态：调用方根据业务语义构造 `Set<String> expectFrom` 传入 CAS 更新
public final class PayTradeTransition {

    private PayTradeTransition() {
    }

    // ── 正向转换表：from → 允许到达的 to 集合 ──────────────────────────
    private static final Map<String, Set<String>> FORWARD = Map.of(
            PayFundStatusEnum.INIT.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode(),
                    PayFundStatusEnum.CLOSE.getCode()),
            PayFundStatusEnum.PROCESSING.getCode(), Set.of(
                    PayFundStatusEnum.SUCCESS.getCode(),
                    PayFundStatusEnum.FAIL.getCode(),
                    PayFundStatusEnum.CLOSE.getCode(),
                    PayFundStatusEnum.CANCEL.getCode()),
            PayFundStatusEnum.FAIL.getCode(), Set.of(
                    PayFundStatusEnum.SUCCESS.getCode()),
            PayFundStatusEnum.CLOSE.getCode(), Set.of(
                    PayFundStatusEnum.SUCCESS.getCode()),
            PayFundStatusEnum.SUCCESS.getCode(), Set.of(),
            PayFundStatusEnum.CANCEL.getCode(), Set.of()
    );

    // ── 反向索引：to → 合法的来源集合（供 CAS expectFrom 校验参考） ────
    private static final Map<String, Set<String>> REVERSE = Map.of(
            PayFundStatusEnum.PROCESSING.getCode(), Set.of(
                    PayFundStatusEnum.INIT.getCode()),
            PayFundStatusEnum.SUCCESS.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode(),
                    PayFundStatusEnum.FAIL.getCode(),
                    PayFundStatusEnum.CLOSE.getCode()),
            PayFundStatusEnum.FAIL.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode()),
            PayFundStatusEnum.CLOSE.getCode(), Set.of(
                    PayFundStatusEnum.INIT.getCode(),
                    PayFundStatusEnum.PROCESSING.getCode()),
            PayFundStatusEnum.CANCEL.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode())
    );

    /// 校验状态转换是否合法，非法时抛出业务异常
    ///
    /// @param from 当前资金状态编码
    /// @param to   目标资金状态编码
    public static void assertLegal(String from, String to) {
        Set<String> allowed = FORWARD.get(from);
        if (allowed == null || !allowed.contains(to)) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR,
                    "pay.error.pay.illegalTransition", from, to);
        }
    }

    /// 判断状态转换是否合法
    public static boolean isLegal(String from, String to) {
        Set<String> allowed = FORWARD.get(from);
        return allowed != null && allowed.contains(to);
    }

    /// 返回能合法到达目标状态的所有来源状态集合
    ///
    /// 供 CAS 更新时构造 `expectFrom` 参数参考。调用方应根据自身业务路径
    /// 选择子集（如回调成功仅允许 PROCESSING/INIT，同步纠正才允许 FAIL/CLOSE）。
    ///
    /// @param to 目标资金状态编码
    /// @return 合法来源编码集合，空集表示目标状态不可达
    public static Set<String> allowedFrom(String to) {
        return REVERSE.getOrDefault(to, Set.of());
    }
}