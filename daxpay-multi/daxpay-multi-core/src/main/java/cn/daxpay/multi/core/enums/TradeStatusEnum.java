package cn.daxpay.multi.core.enums;

import cn.daxpay.multi.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 交易状态
 * @author xxm
 * @since 2024/8/20
 */
@Getter
@AllArgsConstructor
public enum TradeStatusEnum {

    /** 成功 */
    SUCCESS("success","成功"),
    /** 失败 */
    FAIL("fail","失败"),
    /** 关闭 */
    CLOSED("closed","关闭"),
    /** 撤销 */
    REVOKED("revoked","撤销");
    private final String code;
    private final String name;

    public static TradeStatusEnum findByCode(String code) {
        return Arrays.stream(TradeStatusEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("交易状态不存在"));
    }
}
