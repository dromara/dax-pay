package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对账差异类型
 * @author xxm
 * @since 2024/3/1
 */
@Getter
@AllArgsConstructor
public enum ReconcileDiffTypeEnum {

    /** 本地订单不存在 */
    LOCAL_NOT_EXISTS("local_not_exists","本地订单不存在"),
    /** 远程订单不存在 */
    REMOTE_NOT_EXISTS("remote_not_exists","远程订单不存在"),
    /** 订单信息不一致 */
    NOT_MATCH("not_match","订单信息不一致");

    final String code;
    final String name;
}
