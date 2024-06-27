package cn.daxpay.single.service.core.system.config.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.daxpay.single.service.core.system.config.convert.PayApiConfigConvert;
import cn.daxpay.single.service.dto.system.config.PayApiConfigDto;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付开放接口管理
 * @author xxm
 * @since 2023/12/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付接口配置")
@TableName("pay_api_config")
public class PayApiConfig extends MpBaseEntity implements EntityBaseFunction<PayApiConfigDto> {

    @DbColumn(comment = "编码",length = 50, isNull = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    @DbColumn(comment = "接口地址", length = 200, isNull = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String api;

    @DbColumn(comment = "名称", length = 100, isNull = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    @DbColumn(comment = "是否启用", isNull = false)
    private boolean enable;

    @DbColumn(comment = "备注", length = 200)
    private String remark;

    /**
     * 转换
     */
    @Override
    public PayApiConfigDto toDto() {
        return PayApiConfigConvert.CONVERT.convert(this);
    }
}

