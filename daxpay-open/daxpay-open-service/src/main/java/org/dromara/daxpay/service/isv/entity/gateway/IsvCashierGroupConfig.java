package org.dromara.daxpay.service.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import org.dromara.daxpay.service.isv.convert.gateway.IsvCashierGroupConfigConvert;
import org.dromara.daxpay.service.isv.param.gateway.IsvCashierGroupConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvCashierGroupConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关收银台分组配置
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_isv_cashier_group_config")
public class IsvCashierGroupConfig extends MpBaseEntity implements ToResult<IsvCashierGroupConfigResult> {

    /**
     * 收银台类型 web/h5/小程序
     * @see GatewayCashierTypeEnum
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String cashierType;

    /** 名称 */
    private String name;

    /** 图标 */
    private String icon;

    /** 背景色 */
    private String bgColor;

    /** 是否推荐 */
    private boolean recommend;

    /** 排序 */
    private Double sortNo;


    public static IsvCashierGroupConfig init(IsvCashierGroupConfigParam param){
        return IsvCashierGroupConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public IsvCashierGroupConfigResult toResult() {
        return IsvCashierGroupConfigConvert.CONVERT.toResult(this);
    }
}
