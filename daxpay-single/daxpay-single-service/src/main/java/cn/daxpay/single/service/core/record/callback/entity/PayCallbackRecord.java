package cn.daxpay.single.service.core.record.callback.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.core.record.callback.convert.PayCallbackRecordConvert;
import cn.daxpay.single.service.dto.record.callback.PayCallbackRecordDto;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @DbMySqlIndex(comment = "本地交易号索引")
    @DbColumn(comment = "本地交易号", length = 32, isNull = false)
    private String tradeNo;

    /** 通道交易号 */
    @DbMySqlIndex(comment = "通道交易号索引")
    @DbColumn(comment = "通道交易号", length = 150)
    private String outTradeNo;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @DbColumn(comment = "支付通道", length = 20, isNull = false)
    private String channel;

    /**
     * 回调类型
     * @see TradeTypeEnum
     */
    @DbColumn(comment = "回调类型", length = 20, isNull = false)
    private String callbackType;

    /** 通知消息内容 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "通知消息", isNull = false)
    private String notifyInfo;

    /**
     * 回调处理状态
     * @see PayCallbackStatusEnum
     */
    @DbColumn(comment = "回调处理状态", length = 20, isNull = false)
    private String status;

    /** 错误码 */
    @DbColumn(comment = "错误码", length = 10)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息", length = 2048)
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public PayCallbackRecordDto toDto() {
        return PayCallbackRecordConvert.CONVERT.convert(this);
    }
}
