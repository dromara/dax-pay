package cn.daxpay.single.service.core.system.config.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.code.PaySignTypeEnum;
import cn.daxpay.single.service.code.TradeNotifyTypeEnum;
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

    /** 签名秘钥 */
    @DbColumn(comment = "签名秘钥")
    private String signSecret;

    /** 是否对请求进行验签 */
    @DbColumn(comment = "是否对请求进行验签")
    private boolean reqSign;

    /**
     * 请求有效时长(秒)
     * 如果传输的请求时间早于当前服务时间, 而且差值超过配置的时长, 将会请求失败
     * 如果传输的请求时间比服务时间大于配置的时长(超过一分钟), 将会请求失败
     */
    @DbColumn(comment = "请求有效时长(秒)")
    private Integer reqTimeout;

    /**
     * 消息通知方式, 目前只支持http
     * @see TradeNotifyTypeEnum
     */
    @DbColumn(comment = "消息通知方式")
    private String notifyType;

    /** 消息通知地址 */
    @DbColumn(comment = "消息通知地址")
    private String notifyUrl;

    /** 同步支付跳转地址 */
    @DbColumn(comment = "同步支付跳转地址")
    private String returnUrl;

    /** 支付限额 */
    @DbColumn(comment = "支付限额")
    private Integer limitAmount;

    /** 订单默认超时时间(分钟) */
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
