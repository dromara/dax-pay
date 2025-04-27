package org.dromara.daxpay.service.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对账差异类型
 * 字典: reconcile_discrepancy_type
 * @author xxm
 * @since 2024/3/1
 */
@Getter
@AllArgsConstructor
public enum ReconcileDiscrepancyTypeEnum {

    /** 一致 */
    CONSISTENT("consistent","一致"),
    /** 本地订单不存在 */
    LOCAL_NOT_EXISTS("local_not_exists","本地订单不存在"),
    /** 远程订单不存在 */
    REMOTE_NOT_EXISTS("remote_not_exists","远程订单不存在"),
    /** 订单信息不一致 */
    NOT_MATCH("not_match","订单信息不一致");


    private final String code;
    private final String name;


    public static ReconcileDiscrepancyTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("未找到对应的支付类型"));
    }
}
