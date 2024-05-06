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

    /** 本地交易号 */
    @DbColumn(comment = "本地交易号")
    private String tradeNo;

    /** 通道交易号 */
    @DbColumn(comment = "通道交易号")
    private String outTradeNo;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @DbColumn(comment = "支付通道")
    private String channel;

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


    /** 修复号 */
    @Schema(description = "修复号")
    private String repairOrderNo;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

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
