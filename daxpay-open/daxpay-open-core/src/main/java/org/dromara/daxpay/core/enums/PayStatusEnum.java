package org.dromara.daxpay.core.enums;

import cn.bootx.platform.core.exception.DataNotExistException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付状态
 * 字典: pay_status
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@RequiredArgsConstructor
public enum PayStatusEnum {
    /** 未指定通道和支付方式等信息, 通常只有网关支付方式创建的订单 */
    WAIT("wait","待支付"),
    /** 选中通道和支付后发起调用后的状态 */
    PROGRESS("progress","支付中"),
    SUCCESS("success","成功"),
    CLOSE("close","支付关闭"),
    CANCEL("cancel","支付撤销"),
    FAIL("fail","失败"),
    /** 订单到了超时时间, 被手动设置订单为这个状态 */
    TIMEOUT("timeout", "支付超时"),
    ;

    /** 编码 */
    private final String code;

    /** 名称 */
    private final String name;

    /**
     * 根据编码获取枚举
     */
    public static PayStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(payStatusEnum -> Objects.equals(payStatusEnum.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("该支付状态不存在"));
    }

}
