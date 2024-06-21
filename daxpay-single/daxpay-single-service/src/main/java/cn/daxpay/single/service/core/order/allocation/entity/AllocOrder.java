package cn.daxpay.single.service.core.order.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.core.code.AllocOrderResultEnum;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.order.allocation.convert.AllocOrderConvert;
import cn.daxpay.single.service.dto.order.allocation.AllocOrderDto;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分账订单
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "分账订单")
@TableName("pay_alloc_order")
public class AllocOrder extends MpBaseEntity implements EntityBaseFunction<AllocOrderDto> {

    /**
     * 分账单号
     */
    @DbMySqlIndex(comment = "分账单号索引")
    @DbColumn(comment = "分账单号", length = 32, isNull = false)
    private String allocNo;

    /**
     * 商户分账单号
     */
    @DbMySqlIndex(comment = "商户分账单号索引")
    @DbColumn(comment = "商户分账单号", length = 100, isNull = false)
    private String bizAllocNo;

    /**
     * 通道分账号
     */
    @DbMySqlIndex(comment = "通道分账号索引")
    @DbColumn(comment = "通道分账号", length = 150)
    private String outAllocNo;

    /** 支付订单ID */
    @DbMySqlIndex(comment = "支付订单ID索引")
    @DbColumn(comment = "支付订单ID", isNull = false)
    private Long orderId;

    /** 支付订单号 */
    @DbMySqlIndex(comment = "支付订单号索引")
    @DbColumn(comment = "支付订单号", length = 32, isNull = false)
    private String orderNo;

    /** 商户支付订单号 */
    @DbMySqlIndex(comment = "商户支付订单号索引")
    @DbColumn(comment = "商户支付订单号", length = 100, isNull = false)
    private String bizOrderNo;

    /** 通道支付订单号 */
    @DbMySqlIndex(comment = "通道支付订单号索引")
    @DbColumn(comment = "通道支付订单号", length = 150, isNull = false)
    private String outOrderNo;

    /** 支付标题 */
    @DbColumn(comment = "支付标题", length = 100, isNull = false)
    private String title;

    /**
     * 所属通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "所属通道", length = 20, isNull = false)
    private String channel;

    /**
     * 总分账金额
     */
    @DbColumn(comment = "总分账金额", length = 8, isNull = false)
    private Integer amount;

    /**
     * 分账描述
     */
    @DbColumn(comment = "分账描述", length = 150)
    private String description;

    /**
     * 状态
     * @see AllocOrderStatusEnum
     */
    @DbColumn(comment = "状态", length = 30, isNull = false)
    private String status;

    /**
     * 处理结果
     * @see AllocOrderResultEnum
     */
    @DbColumn(comment = "处理结果", length = 20, isNull = false)
    private String result;

    /** 分账完成时间 */
    @DbColumn(comment = "分账完成时间")
    private LocalDateTime finishTime;

    /** 异步通知地址 */
    @DbColumn(comment = "异步通知地址", length = 200)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @DbColumn(comment = "商户扩展参数", length = 500)
    private String attach;

    /** 请求时间，时间戳转时间 */
    @DbColumn(comment = "请求时间，传输时间戳")
    private LocalDateTime reqTime;

    /** 终端ip */
    @DbColumn(comment = "支付终端ip", length = 64)
    private String clientIp;

    /** 错误码 */
    @DbColumn(comment = "错误码", length = 10)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息", length = 150)
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public AllocOrderDto toDto() {
        return AllocOrderConvert.CONVERT.convert(this);
    }
}
