package cn.daxpay.multi.core.context;

import lombok.experimental.UtilityClass;

/**
 * 商户租户信息获取工具
 * @author xxm
 * @since 2024/6/26
 */
@UtilityClass
public class MchTenantContextHolder {

    /**
     *  TODO 获取商户号
     */
    public String getMchNo() {
        return "test";
    }

    /**
     *  TODO 临时设置商户号, 该次会话有效
     */
    public void setMchNo(String mchNo) {
    }
}
