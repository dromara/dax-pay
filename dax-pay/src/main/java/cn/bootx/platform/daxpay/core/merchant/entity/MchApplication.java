package cn.bootx.platform.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.mybatis.table.modify.mybatis.mysq.constants.MySqlIndexType;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.merchant.convert.MchApplicationConvert;
import cn.bootx.platform.daxpay.dto.merchant.MchApplicationDto;
import cn.bootx.platform.daxpay.param.merchant.MchApplicationParam;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 商户应用
 *
 * @author xxm
 * @since 2023-05-19
 */
@DbTable(comment = "商户应用")
@EqualsAndHashCode(callSuper = true)
@DbMySqlIndex(fields = MchApplication.Fields.code, type = MySqlIndexType.UNIQUE, comment = "应用编码唯一索引")
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_mch_app")
public class MchApplication extends MpBaseEntity implements EntityBaseFunction<MchApplicationDto> {

    /** 应用编码 */
    @DbColumn(comment = "应用编码")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 名称 */
    @DbColumn(comment = "名称")
    private String name;

    /** 商户号 */
    @DbColumn(comment = "商户号")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mchCode;

    /**
     * 状态
     * @see cn.bootx.platform.daxpay.code.MchAndAppCode
     */
    @DbColumn(comment = "状态")
    private String state;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /** 创建对象 */
    public static MchApplication init(MchApplicationParam in) {
        return MchApplicationConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public MchApplicationDto toDto() {
        return MchApplicationConvert.CONVERT.convert(this);
    }

}
