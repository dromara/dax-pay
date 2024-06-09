package cn.daxpay.single.service.core.system.config.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.service.core.system.config.convert.PayChannelConfigConvert;
import cn.daxpay.single.service.dto.system.config.PayChannelConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付通道配置
 * 有哪些通道都是在代码中定义好的，界面上只做展示和部分信息的修改用，不可以进行增删相关的操作
 * @author xxm
 * @since 2024/1/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付通道配置")
@TableName("pay_channel_config")
public class PayChannelConfig extends MpBaseEntity implements EntityBaseFunction<PayChannelConfigDto> {

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

    /**
     * 转换
     */
    @Override
    public PayChannelConfigDto toDto() {
        return PayChannelConfigConvert.CONVERT.convert(this);
    }

}
