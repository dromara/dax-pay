package org.dromara.daxpay.service.device.result.terminal;

import org.dromara.daxpay.core.enums.TerminalDeviceTypeEnum;
import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * 支付终端设备参数
 * @author xxm
 * @since 2025/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付终端设备参数")
public class TerminalDeviceResult extends MchResult {

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

    /** 省市区编码 */
    @Schema(description = "省市区编码")
    private List<String> areaCode;

    /** 终端发放地址 */
    @Schema(description = "终端发放地址")
    private String address;

    /** 终端厂商名称 */
    @Schema(description = "终端厂商名称")
    private String companyName;

    /** 发放日期 */
    @Schema(description = "发放日期")
    private LocalDate putDate;

    /** 支持终端定位 */
    @Schema(description = "支持终端定位")
    private Boolean gps;

    /** 终端机具型号 */
    @Schema(description = "终端机具型号")
    private String machineType;

    /**
     * 经度，浮点型, 小数点后最多保留6位
     */
    @Schema(description = "经度，浮点型, 小数点后最多保留6位")
    private String longitude;
    /**
     * 纬度，浮点型,小数点后最多保留6位
     */
    @Schema(description = "纬度，浮点型,小数点后最多保留6位")
    private String latitude;

    /** 设备 IP 地址 */
    @Schema(description = "设备 IP 地址")
    private String ip;

    /** 银行卡受理终端产品入网认证编号 */
    @Schema(description = "银行卡受理终端产品入网认证编号")
    private String networkLicense;
}
