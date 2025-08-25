package org.dromara.daxpay.service.device.param.terminal;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.TerminalDeviceTypeEnum;
import org.dromara.daxpay.service.pay.common.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付终端查询参数
 * @author xxm
 * @since 2025/3/8
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TerminalDeviceQuery extends MchQuery {

    /** 终端名称 */
    @Schema(description = "终端名称")
    private String name;

    /** 终端编码 */
    @Schema(description = "终端编码")
    private String terminalNo;

    /**
     * 终端类型
     * @see TerminalDeviceTypeEnum
     */
    @Schema(description = "终端类型")
    private String type;

    /** 终端序列号 */
    @Schema(description = "终端序列号")
    private String serialNum;

    /** 终端机具型号 */
    @Schema(description = "终端机具型号")
    private String machineType;

}
