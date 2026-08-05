package cn.daxpay.open.payment.trade.runtime.statemachine;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;

import java.util.Map;
import java.util.Set;

/// # 转账状态转换守卫
///
/// 集中定义转账单/转账凭证 `status` 的所有合法转换路径，与 [PayTradeTransition] 同级。
/// 转账为资金出款动作，一旦 SUCCESS 资金已出不可逆，因此 SUCCESS/CLOSE 均为终态；
/// 与支付不同，转账 CLOSE 后不做 SUCCESS 纠正（防止关闭后资金实际已出仍被回写为成功）。
///
/// 合法转换一览：
/// <pre>
/// INIT       → PROCESSING
/// PROCESSING → SUCCESS, FAIL, CLOSE
/// FAIL       → PROCESSING（重试, 复用原单）, SUCCESS（同步纠正：本地误判 FAIL 后由通道查询翻转）
/// SUCCESS    → （终态）
/// CLOSE      → （终态）
/// </pre>
///
/// 使用方式：
/// 1. 编程守卫：变更前调用 [assertLegal]，非法转换直接抛异常（防编程错误）
/// 2. CAS 前置态：调用方根据业务语义构造 `Set<String> expectFrom` 传入 CAS 更新
public final class TransferTransition {

    private TransferTransition() {
    }

    // ── 正向转换表：from → 允许到达的 to 集合 ──────────────────────────
    private static final Map<String, Set<String>> FORWARD = Map.of(
            PayFundStatusEnum.INIT.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode()),
            PayFundStatusEnum.PROCESSING.getCode(), Set.of(
                    PayFundStatusEnum.SUCCESS.getCode(),
                    PayFundStatusEnum.FAIL.getCode(),
                    PayFundStatusEnum.CLOSE.getCode()),
            PayFundStatusEnum.FAIL.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode(),
                    PayFundStatusEnum.SUCCESS.getCode()),
            PayFundStatusEnum.SUCCESS.getCode(), Set.of(),
            PayFundStatusEnum.CLOSE.getCode(), Set.of()
    );

    // ── 反向索引：to → 合法的来源集合（供 CAS expectFrom 校验参考） ────
    private static final Map<String, Set<String>> REVERSE = Map.of(
            PayFundStatusEnum.PROCESSING.getCode(), Set.of(
                    PayFundStatusEnum.INIT.getCode(),
                    PayFundStatusEnum.FAIL.getCode()),
            PayFundStatusEnum.SUCCESS.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode(),
                    PayFundStatusEnum.FAIL.getCode()),
            PayFundStatusEnum.FAIL.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode()),
            PayFundStatusEnum.CLOSE.getCode(), Set.of(
                    PayFundStatusEnum.PROCESSING.getCode())
    );

    /// 校验状态转换是否合法，非法时抛出业务异常
    ///
    /// @param from 当前资金状态编码
    /// @param to   目标资金状态编码
    public static void assertLegal(String from, String to) {
        Set<String> allowed = FORWARD.get(from);
        if (allowed == null || !allowed.contains(to)) {
            // 转账: 非法状态转换
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR,
                    "pay.error.transfer.illegalTransition", from, to);
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
    /// 选择子集（如回调成功仅允许 PROCESSING，同步纠正才允许 FAIL）。
    ///
    /// @param to 目标资金状态编码
    /// @return 合法来源编码集合，空集表示目标状态不可达
    public static Set<String> allowedFrom(String to) {
        return REVERSE.getOrDefault(to, Set.of());
    }
}
