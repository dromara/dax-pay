package cn.daxpay.multi.core.enums;

import cn.daxpay.multi.core.exception.StatusNotExistException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付状态
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@RequiredArgsConstructor
public enum PayStatusEnum {
    PROGRESS("progress"),
    SUCCESS("success"),
    CLOSE("close"),
    CANCEL("cancel"),
    FAIL("fail");

    /** 编码 */
    private final String code;

    /**
     * 根据编码获取枚举
     */
    public static PayStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(payStatusEnum -> Objects.equals(payStatusEnum.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new StatusNotExistException("该枚举不存在"));
    }

}
