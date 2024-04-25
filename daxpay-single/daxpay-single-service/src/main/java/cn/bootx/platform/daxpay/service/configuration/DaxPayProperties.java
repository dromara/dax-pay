package cn.bootx.platform.daxpay.service.configuration;

import cn.bootx.platform.daxpay.util.OrderNoGenerateUtil;
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
    /** 服务地址 */
    private String serverUrl;

    /** 前端地址(h5) */
    private String frontH5Url;

    /** 前端地址(web) */
    private String frontWebUrl;

    /** 机器码, 用于区分不同机器生成的流水号 */
    private String machineNo = "56";

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
        OrderNoGenerateUtil.setMachineNo(machineNo);
    }
}
