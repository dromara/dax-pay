package cn.bootx.platform.daxpay.service.core.system.payinfo.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.core.system.payinfo.convert.PayWayInfoConvert;
import cn.bootx.platform.daxpay.service.dto.system.payinfo.PayMethodInfoDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付方式
 * 都是在代码中定义好的，界面上只做展示用，不可以进行增删相关的操作
 * @author xxm
 * @since 2024/1/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付方式")
@TableName("pay_method_info")
public class PayMethodInfo extends MpBaseEntity implements EntityBaseFunction<PayMethodInfoDto> {

    /** 需要与系统中配置的枚举一致 */
    @DbColumn(comment = "代码")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 需要与系统中配置的枚举一致 */
    @DbColumn(comment = "名称")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    public static PayMethodInfoDto convert(PayMethodInfo in) {
        return PayWayInfoConvert.CONVERT.convert(in);
    }

    /**
     * 转换
     */
    @Override
    public PayMethodInfoDto toDto() {
        return PayWayInfoConvert.CONVERT.convert(this);
    }
}
