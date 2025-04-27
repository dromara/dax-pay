package org.dromara.daxpay.service.param.termina;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.TerminalDeviceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * 支付终端设备参数
 * @author xxm
 * @since 2025/3/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付终端设备参数")
public class TerminalDeviceParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /** 终端名称 */
    @Schema(description = "终端名称")
    private String name;

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

    @NotBlank(message = "商户AppId不能为空")
    @Schema(description = "商户应用AppId")
    private String appId;
}
