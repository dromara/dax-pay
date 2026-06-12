package org.dromara.daxpay.platform.core.enums.pay.pay;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 支付状态
///
/// 字典: pay_status
@Getter
@RequiredArgsConstructor
public enum PayStatusEnum implements I18nSupport {
    /// 未指定通道和支付方式等信息, 通常是一些特殊的支付方式才会存在
    WAIT("wait"),
    /// 选中通道和支付后发起调用后的状态
    PROGRESS("progress"),
    /// 成功
    SUCCESS("success"),
    /// 支付关闭
    CLOSE("close"),
    /// 支付撤销
    CANCEL("cancel"),
    /// 失败
    FAIL("fail"),
    /// 订单到了超时时间, 被手动设置订单为这个状态
    TIMEOUT("timeout"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_status";
    }

    /// 根据编码获取枚举
    public static PayStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(payStatusEnum -> Objects.equals(payStatusEnum.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.payStatusNotExist"));
    }

}
