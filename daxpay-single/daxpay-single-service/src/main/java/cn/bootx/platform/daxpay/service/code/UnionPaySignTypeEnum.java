package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 云闪付签名类型
 * @author xxm
 * @since 2024/3/8
 */
@Getter
@AllArgsConstructor
public enum UnionPaySignTypeEnum {

    RSA2("RSA2","RSA2");


    private final String code;
    private final String name;
}
