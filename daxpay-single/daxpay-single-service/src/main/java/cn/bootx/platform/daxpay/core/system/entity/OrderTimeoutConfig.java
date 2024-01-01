package cn.bootx.platform.daxpay.core.system.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单超时配置
 * @author xxm
 * @since 2024/1/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "订单超时配置")
@TableName("pay_order_timeout_config")
public class OrderTimeoutConfig extends MpBaseEntity {

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * 是否开启定时任务
     */
    private boolean cron;

    /**
     *
     */
}
