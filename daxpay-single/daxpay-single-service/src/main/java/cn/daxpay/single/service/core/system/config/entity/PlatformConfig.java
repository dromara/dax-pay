package cn.daxpay.single.service.core.system.config.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.code.PaySignTypeEnum;
import cn.daxpay.single.service.core.system.config.convert.PlatformConfigConvert;
import cn.daxpay.single.service.dto.system.config.PlatformConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付平台配置
 * @author xxm
 * @since 2023/12/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付平台配置")
@TableName("pay_platform_config")
public class PlatformConfig extends MpBaseEntity implements EntityBaseFunction<PlatformConfigDto> {

    @DbColumn(comment = "网站地址")
    private String websiteUrl;

    /**
     * @see PaySignTypeEnum
     */
    @DbColumn(comment = "签名方式")
    private String signType;

    @DbColumn(comment = "签名秘钥")
    private String signSecret;

    @DbColumn(comment = "支付通知地址")
    private String notifyUrl;

    @DbColumn(comment = "同步支付跳转地址")
    private String returnUrl;

    @DbColumn(comment = "支付限额")
    private Integer limitAmount;

    @DbColumn(comment = "订单默认超时时间(分钟)")
    private Integer orderTimeout;

    /**
     * 转换
     */
    @Override
    public PlatformConfigDto toDto() {
        return PlatformConfigConvert.CONVERT.convert(this);
    }
}
