package cn.bootx.platform.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.impl.mysql.annotation.MySqlIndex;
import cn.bootx.mybatis.table.modify.impl.mysql.constants.MySqlIndexType;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 商户应用
 * @author xxm
 * @date 2023/5/17
 */
@EqualsAndHashCode(callSuper = true)
@MySqlIndex(columns = "app_no",type = MySqlIndexType.UNIQUE,comment = "应用编码唯一索引")
@Data
@FieldNameConstants
@Accessors(chain = true)
@DbTable(comment = "商户应用")
@TableName("pay_application")
public class MchApplication extends MpBaseEntity {

    /** 应用编码 */
    @DbColumn(comment = "应用编码")
    private String appNo;

    /** 名称 */
    @DbColumn(comment = "名称")
    private String name;

    /** 商户号 */
    @DbColumn(comment = "商户号")
    private String mchNo;

    /** 状态类型 */
    @DbColumn(comment = "状态类型")
    private String state;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

}
