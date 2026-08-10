package cn.daxpay.open.payment.trade.runtime.statemachine;

import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;

import java.util.Map;
import java.util.Set;

/// # 分账状态转换守卫
///
/// 集中定义分账单 `status` 的所有合法转换路径, 与 [TransferTransition] 同级。
/// 分账为单次操作(发起即完结), 一旦 SUCCESS/PARTIAL/FAIL 均为终态。
///
/// 合法转换一览：
/// <pre>
/// PROCESSING → SUCCESS, PARTIAL, FAIL
/// SUCCESS    → （终态, 全部成功）
/// PARTIAL    → （终态, 部分成功）
/// FAIL       → （终态, 全部失败）
/// </pre>
///
/// 使用方式：
/// 1. 编程守卫：变更前调用 [assertLegal], 非法转换直接抛异常（防编程错误）
/// 2. CAS 前置态：调用方根据业务语义构造 `Set<String> expectFrom` 传入 CAS 更新
public final class AllocTransition {

    private AllocTransition() {
    }

    // ── 正向转换表：from → 允许到达的 to 集合 ──────────────────────────
    private static final Map<String, Set<String>> FORWARD = Map.of(
            AllocOrderStatusEnum.PROCESSING.getCode(), Set.of(
                    AllocOrderStatusEnum.SUCCESS.getCode(),
                    AllocOrderStatusEnum.PARTIAL.getCode(),
                    AllocOrderStatusEnum.FAIL.getCode()),
            AllocOrderStatusEnum.SUCCESS.getCode(), Set.of(),
            AllocOrderStatusEnum.PARTIAL.getCode(), Set.of(),
            AllocOrderStatusEnum.FAIL.getCode(), Set.of()
    );

    // ── 反向索引：to → 合法的来源集合（供 CAS expectFrom 校验参考） ────
    private static final Map<String, Set<String>> REVERSE = Map.of(
            AllocOrderStatusEnum.SUCCESS.getCode(), Set.of(
                    AllocOrderStatusEnum.PROCESSING.getCode()),
            AllocOrderStatusEnum.PARTIAL.getCode(), Set.of(
                    AllocOrderStatusEnum.PROCESSING.getCode()),
            AllocOrderStatusEnum.FAIL.getCode(), Set.of(
                    AllocOrderStatusEnum.PROCESSING.getCode())
    );

    /// 校验状态转换是否合法, 非法时抛出业务异常
    ///
    /// @param from 当前分账状态编码
    /// @param to   目标分账状态编码
    public static void assertLegal(String from, String to) {
        Set<String> allowed = FORWARD.get(from);
        if (allowed == null || !allowed.contains(to)) {
            // 分账: 非法状态转换
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR,
                    "pay.error.alloc.illegalTransition", from, to);
        }
    }

    /// 判断状态转换是否合法
    public static boolean isLegal(String from, String to) {
        Set<String> allowed = FORWARD.get(from);
        return allowed != null && allowed.contains(to);
    }

    /// 返回能合法到达目标状态的所有来源状态集合
    ///
    /// 供 CAS 更新时构造 `expectFrom` 参数参考。调用方应根据自身业务路径选择子集。
    ///
    /// @param to 目标分账状态编码
    /// @return 合法来源编码集合, 空集表示目标状态不可达
    public static Set<String> allowedFrom(String to) {
        return REVERSE.getOrDefault(to, Set.of());
    }
}
