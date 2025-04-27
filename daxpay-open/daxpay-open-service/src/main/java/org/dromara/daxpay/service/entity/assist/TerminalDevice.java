package org.dromara.daxpay.service.entity.assist;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import org.dromara.daxpay.core.enums.TerminalDeviceTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.assist.TerminalDeviceConvert;
import org.dromara.daxpay.service.param.termina.TerminalDeviceParam;
import org.dromara.daxpay.service.result.termina.TerminalDeviceResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 支付终端设备管理
 * @author xxm
 * @since 2025/3/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_terminal_device",autoResultMap = true)
public class TerminalDevice extends MchAppBaseEntity implements ToResult<TerminalDeviceResult> {

    /** 终端名称 */
    private String name;

    /** 终端编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String terminalNo;

    /**
     * 终端类型
     * @see TerminalDeviceTypeEnum
     */
    private String type;

    /** 终端序列号 */
    private String serialNum;

    /** 省市区编码 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = StringListTypeHandler.class)
    private List<String> areaCode;

    /** 终端发放地址 */
    private String address;

    /** 终端厂商名称 */
    private String companyName;

    /** 发放日期 */
    private LocalDate putDate;

    /** 支持终端定位 */
    private Boolean gps;

    /** 终端机具体型号 */
    private String machineType;

    /**
     * 经度，浮点型, 小数点后最多保留6位
     */
    private String longitude;
    /**
     * 纬度，浮点型,小数点后最多保留6位
     */
    private String latitude;

    /** 设备 IP 地址 */
    private String ip;

    /** 银行卡受理终端产品入网认证编号 */
    private String networkLicense;

    public Boolean getGps() {
        return Objects.equals(gps, true);
    }

    /**
     * 初始化对象
     */
    public static TerminalDevice init(TerminalDeviceParam param){
        return TerminalDeviceConvert.CONVERT.toEntity(param);
    }

    @Override
    public TerminalDeviceResult toResult() {
        return TerminalDeviceConvert.CONVERT.toResult(this);
    }
}
