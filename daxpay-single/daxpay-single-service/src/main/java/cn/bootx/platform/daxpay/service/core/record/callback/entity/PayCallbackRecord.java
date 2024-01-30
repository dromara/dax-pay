package cn.bootx.platform.daxpay.service.core.record.callback.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.core.record.callback.convert.PayCallbackRecordConvert;
import cn.bootx.platform.daxpay.service.dto.record.callback.PayCallbackRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 回调通知
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "网关回调通知")
@TableName("pay_callback_record")
public class PayCallbackRecord extends MpCreateEntity implements EntityBaseFunction<PayCallbackRecordDto> {

    /** 本地订单id */
    @DbColumn(comment = "本地订单id")
    private Long orderId;

    /** 支付网关订单号 */
    @DbColumn(comment = "支付网关订单号")
    private String gatewayOrderNo;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @DbColumn(comment = "支付通道")
    private String payChannel;

    /**
     * 回调类型
     * @see PaymentTypeEnum
     */
    @DbColumn(comment = "回调类型")
    private String callbackType;

    /** 通知消息内容 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "通知消息")
    private String notifyInfo;

    /**
     * @see PayCallbackStatusEnum
     */
    @DbColumn(comment = "回调处理状态")
    private String status;


    @Schema(description = "支付单修复ID")
    private Long repairOrderId;

    /** 提示信息 */
    @DbColumn(comment = "提示信息")
    private String msg;

    /**
     * 转换
     */
    @Override
    public PayCallbackRecordDto toDto() {
        return PayCallbackRecordConvert.CONVERT.convert(this);
    }
}
