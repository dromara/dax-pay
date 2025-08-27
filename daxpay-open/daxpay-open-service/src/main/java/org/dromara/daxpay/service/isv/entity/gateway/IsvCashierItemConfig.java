package org.dromara.daxpay.service.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.GatewayPayEnvTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.isv.convert.gateway.IsvCashierItemConfigConvert;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierItemConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierItemConfigResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_isv_cashier_item_config",autoResultMap = true)
public class IsvCashierItemConfig extends MpBaseEntity implements ToResult<IsvCashierItemConfigResult> {

    /** 类目配置Id */
    private Long groupId;

    /** 名称 */
    private String name;

    /** 是否推荐 */
    private boolean recommend;

    /** 背景色 */
    private String bgColor;

    /** 边框色 */
    private String borderColor;

    /** 字体颜色 */
    private String fontColor;

    /** 图标 */
    private String icon;

    /**
     * 生效的支付环境列表
     * @see GatewayPayEnvTypeEnum
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> payEnvTypes;

    /** 排序 */
    private Double sortNo;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    private String callType;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    private String payMethod;

    /**
     * 其他支付方式
     */
    private String otherMethod;


    /**
     * 构造
     */
    public static IsvCashierItemConfig init(IsvCashierItemConfigParam param) {
        return IsvCashierItemConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public IsvCashierItemConfigResult toResult() {
        return IsvCashierItemConfigConvert.CONVERT.toResult(this);
    }
}
