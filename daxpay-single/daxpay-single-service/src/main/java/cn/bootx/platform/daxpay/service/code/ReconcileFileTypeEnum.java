package cn.bootx.platform.daxpay.service.code;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对账单文件
 * @author xxm
 * @since 2024/5/4
 */
@Getter
@AllArgsConstructor
public enum ReconcileFileTypeEnum {

    FUNDS("funds", "资金对账"),
    TRADE("trade", "交易对账"),
    ZIP("zip", "压缩包"),
    ;

    private final String code;
    private final String name;

    public static ReconcileFileTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("未知的对账文件类型"));
    }
}
