package cn.bootx.platform.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户应用支付配置
 *
 * @author xxm
 * @date 2023-05-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "商户应用支付配置")
@Accessors(chain = true)
@TableName("pay_mch_app_config")
public class MchAppPayConfig extends MpBaseEntity {

    /** 关联应用ID */
    @DbColumn(comment = "关联应用ID")
    private Long appId;

    /** 关联配置ID */
    @DbColumn(comment = "关联配置ID")
    private Long configId;

    /** 支付通道编码 */
    @DbColumn(comment = "支付通道编码")
    private String channel;

    /** 状态 */
    @DbColumn(comment = "状态")
    private String state;

}
