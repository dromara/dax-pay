package cn.bootx.platform.daxpay.service.core.channel.cash.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import cn.bootx.platform.daxpay.service.core.channel.cash.convert.CashPayConfigConvert;
import cn.bootx.platform.daxpay.service.dto.channel.cash.CashPayConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 现金支付配置类
 * @author xxm
 * @since 2024/2/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "现金支付配置")
@TableName(value = "pay_cash_config",autoResultMap = true)
public class CashConfig extends MpBaseEntity implements EntityBaseFunction<CashPayConfigDto> {

    /** 是否启用 */
    @DbColumn(comment = "是否启用")
    private Boolean enable;

    /** 单次支付最多多少金额 */
    @DbColumn(comment = "单次支付最多多少金额 ")
    private Integer singleLimit;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    @DbMySqlFieldType(MySqlFieldTypeEnum.VARCHAR)
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> payWays;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public CashPayConfigDto toDto() {
        return CashPayConfigConvert.CONVERT.convert(this);
    }
}
