package cn.bootx.platform.daxpay.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付修复来源, 不同来源的修复类型要执行的逻辑不相同
 * @author xxm
 * @since 2023/12/29
 */
@Getter
@AllArgsConstructor
public enum PayRepairSourceEnum{

    SYNC("sync","同步"),
    CALLBACK("callback","回调"),
    RECONCILE("reconcile","对账"),;

    private final String code;
    private final String name;
}
