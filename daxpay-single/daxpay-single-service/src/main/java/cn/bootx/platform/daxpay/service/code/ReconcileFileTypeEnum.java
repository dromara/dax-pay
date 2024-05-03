package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对账单文件
 * @author xxm
 * @since 2024/5/4
 */
@Getter
@AllArgsConstructor
public enum ReconcileFileTypeEnum {

    TOTAL("total", "汇总"),
    DETAIL("detail", "明细"),
    ZIP("zip", "压缩包"),
    ;

    private final String code;
    private final String name;
}
