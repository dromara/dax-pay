package cn.bootx.platform.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户
 * @author xxm
 * @date 2023/5/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "商户")
@TableName("pay_merchant")
@Accessors(chain = true)
public class MerchantInfo extends MpBaseEntity {

    /** 商户号 */
    @DbColumn("商户号")
    private String mchNo;

    /** 商户名称 */
    @DbColumn("商户名称")
    private String mchName;

    /** 商户简称 */
    @DbColumn("商户简称")
    private String mchShortName;

    /** 类型 */
    @DbColumn("类型")
    private String type;

    /** 联系人姓名 */
    @DbColumn("联系人姓名")
    private String contactName;

    /** 联系人手机号 */
    @DbColumn("联系人手机号")
    private String contactTel;

    /** 是否停用 */
    @DbColumn("是否停用")
    private String deactivate;

    /** 商户备注 */
    @DbColumn("商户备注")
    private String remark;

}
