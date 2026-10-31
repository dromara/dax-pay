package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账完结模式
///
/// pay_alloc_order.finish_mode 的取值集合, 仅 alloc 类型分账单有意义。
/// - auto: 分账即完结(发起时随单解冻剩余资金, 微信 unfreeze_unsplit=true / 支付宝 royalty_finish=true),
///   兼容存量单次分账语义; 不支持随后追加分账
/// - manual: 分账不完结(剩余资金保持冻结, 可继续多次分账, 最终需显式完结单解冻)
///
/// 默认 auto: 存量调用方行为零变化; 多次分账调用方显式传 manual, 最后一笔传 auto 或调用完结接口。
@Getter
@RequiredArgsConstructor
public enum AllocFinishModeEnum implements I18nSupport {

    /// 分账即完结(发起时随单解冻剩余资金)
    AUTO("auto"),
    /// 分账不完结(剩余资金保持冻结, 需显式完结)
    MANUAL("manual"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_finish_mode";
    }

    /// 根据编码获取枚举
    public static AllocFinishModeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 该分账完结模式不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.allocFinishModeNotExist", code));
    }
}
