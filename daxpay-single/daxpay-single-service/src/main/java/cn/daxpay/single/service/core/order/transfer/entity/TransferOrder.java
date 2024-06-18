package cn.daxpay.single.service.core.order.transfer.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.TransferPayeeTypeEnum;
import cn.daxpay.single.core.code.TransferStatusEnum;
import cn.daxpay.single.core.code.TransferTypeEnum;
import cn.daxpay.single.service.core.order.transfer.convert.TransferOrderConvert;
import cn.daxpay.single.service.dto.order.transfer.TransferOrderDto;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 转账订单
 * @author xxm
 * @since 2024/3/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "转账订单")
@TableName("pay_transfer_order")
public class TransferOrder extends MpBaseEntity implements EntityBaseFunction<TransferOrderDto> {

    /** 商户转账号 */
    @DbMySqlIndex(comment = "商户转账号索引")
    @DbColumn(comment = "商户转账号", length = 100, isNull = false)
    private String bizTransferNo;

    /** 转账号 */
    @DbMySqlIndex(comment = "转账号索引")
    @DbColumn(comment = "转账号", length = 150, isNull = false)
    private String transferNo;

    /** 通道转账号 */
    @DbMySqlIndex(comment = "通道转账号索引")
    @DbColumn(comment = "通道转账号", length = 150)
    private String outTransferNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "支付通道", length = 20, isNull = false)
    private String channel;

    /** 转账金额 */
    @DbColumn(comment = "转账金额", length = 15, isNull = false)
    private Integer amount;

    /** 标题 */
    @DbColumn(comment = "标题", length = 100)
    private String title;

    /** 转账原因/备注 */
    @DbColumn(comment = "转账原因/备注", length = 150)
    private String reason;

    /**
     * 转账类型, 微信使用
     * @see TransferTypeEnum
     */
    @DbColumn(comment = "转账类型, 微信使用", length = 20)
    private String transferType;

    /** 付款方 */
    @DbColumn(comment = "付款方", length = 100)
    private String payer;

    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    @DbColumn(comment = "收款人类型", length = 20)
    private String payeeType;

    /** 收款人账号 */
    @DbColumn(comment = "收款人账号", length = 100)
    private String payeeAccount;

    /** 收款人姓名 */
    @DbColumn(comment = "收款人姓名", length = 50)
    private String payeeName;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    @DbColumn(comment = "状态", length = 20, isNull = false)
    private String status;

    /** 成功时间 */
    @DbColumn(comment = "成功时间")
    private LocalDateTime successTime;


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

    @Override
    public TransferOrderDto toDto() {
        return TransferOrderConvert.CONVERT.convert(this);
    }
}
