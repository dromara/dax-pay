package org.dromara.daxpay.service.common.local;

import lombok.experimental.UtilityClass;

/**
 * 商户信息获取工具
 * @author xxm
 * @since 2024/6/26
 */
@UtilityClass
public class MchContextLocal {
    private final ThreadLocal<String> MCH_NO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     *  获取商户号
     */
    public String getMchNo() {
        return MCH_NO_THREAD_LOCAL.get();
    }

    /**
     *  临时设置商户号, 该次会话有效
     */
    public void setMchNo(String mchNo) {
        MCH_NO_THREAD_LOCAL.set(mchNo);
    }

    /**
     *  清除商户号
     */
    public void clearMchNo() {
        MCH_NO_THREAD_LOCAL.remove();
    }
}
