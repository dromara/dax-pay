package cn.daxpay.single.service.configuration;

import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 演示模块配置类
 * @author xxm
 * @since 2024/2/8
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dax-pay")
public class DaxPayProperties {

    /** 前端地址(h5) */
    private String frontH5Url;

    /** 机器码, 用于区分不同机器生成的流水号 */
    private String machineNo = "56";

    /** 当前环境，会影响订单号的生成 */
    private String env = "";

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
        TradeNoGenerateUtil.setMachineNo(machineNo);
    }

    public void setEnv(String env) {
        this.env = env;
        TradeNoGenerateUtil.setEnv(env);
    }

    public String getFrontH5Url() {
        return StrUtil.removeSuffix(frontH5Url, "/");
    }
}
