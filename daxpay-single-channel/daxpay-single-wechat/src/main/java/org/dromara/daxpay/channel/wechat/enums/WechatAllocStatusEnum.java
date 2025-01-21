package org.dromara.daxpay.channel.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.exception.ConfigNotExistException;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author xxm
 * @since 2024/12/16
 */
@Getter
@AllArgsConstructor
public enum WechatAllocStatusEnum {

    //处理中
    PENDING("PENDING", AllocDetailResultEnum.PENDING),
    //分账完成
    SUCCESS("SUCCESS", AllocDetailResultEnum.SUCCESS),
    //分账完成
    CLOSED("CLOSED", AllocDetailResultEnum.FAIL),
    ;

    private final String code;

    private final AllocDetailResultEnum result;

    /**
     * 根据编码获取枚举
     */
    public static WechatAllocStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("该微信分账状态不存在"));
    }
}
