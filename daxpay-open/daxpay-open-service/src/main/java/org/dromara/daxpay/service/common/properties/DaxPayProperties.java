package org.dromara.daxpay.service.common.properties;

import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * daxpay配置类
 * @author xxm
 * @since 2024/2/8
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dax-pay")
public class DaxPayProperties {

    /** 通常为两位内 机器码, 用于区分不同机器生成的流水号 */
    private String machineNo = "58";

    /**  当前环境，会影响订单号的生成 */
    private String env = "";

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
        TradeNoGenerateUtil.setMachineNo(machineNo);
    }

    public void setEnv(String env) {
        this.env = env;
        TradeNoGenerateUtil.setEnv(env);
    }
}
