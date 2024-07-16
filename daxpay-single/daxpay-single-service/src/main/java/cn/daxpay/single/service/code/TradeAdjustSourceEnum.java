package cn.daxpay.single.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易调整出发来源, 不同来源的调整类型要执行的逻辑不相同
 * @author xxm
 * @since 2023/12/29
 */
@Getter
@AllArgsConstructor
public enum TradeAdjustSourceEnum {

    SYNC("sync","同步"),
    CALLBACK("callback","回调"),
;
    private final String code;
    private final String name;
}
