package cn.daxpay.open.payment.trade.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 资金交易状态
///
/// pay_trade 表的资金态，与容器业务状态分离
/// 字典: pay_fund_status
@Getter
@RequiredArgsConstructor
public enum PayFundStatusEnum implements I18nSupport {

    /// 初始化（待支付）
    INIT("init"),
    /// 处理中（已调通道，等待结果）
    PROCESSING("processing"),
    /// 成功
    SUCCESS("success"),
    /// 失败
    FAIL("fail"),
    /// 已关闭
    CLOSE("close"),
    /// 已撤销
    CANCEL("cancel"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_fund_status";
    }

    public static PayFundStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.payFundStatusNotExist", code));
    }
}
