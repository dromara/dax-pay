package cn.bootx.platform.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 商户应用支付配置
 * @author xxm
 * @date 2023/5/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@DbTable(comment = "商户应用支付配置")
@TableName(value = "pay_mch_app_config")
public class MchAppPayConfig  extends MpBaseEntity {

    @DbColumn(comment = "关联配置ID")
    private Long configId;

    /** 类型 */
    @DbColumn(comment = "支付通道类型")
    private String channel;

    /** 类型 */
    @DbColumn(comment = "支付通道名称")
    private String channelName;

    /** 状态 */
    @DbColumn(comment = "状态")
    private String state;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

}
