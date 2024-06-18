package cn.daxpay.single.service.core.record.close.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.code.PayCloseTypeEnum;
import cn.daxpay.single.service.core.record.close.convert.PayCloseRecordConvert;
import cn.daxpay.single.service.dto.record.close.PayCloseRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付关闭记录")
@TableName("pay_close_record")
public class PayCloseRecord extends MpCreateEntity implements EntityBaseFunction<PayCloseRecordDto> {

    /** 支付订单号 */
    @DbMySqlIndex(comment = "支付订单号索引")
    @DbColumn(comment = "支付订单号", length = 32, isNull = false)
    private String orderNo;

    /** 商户支付订单号 */
    @DbMySqlIndex(comment = "商户支付订单号索引")
    @DbColumn(comment = "商户支付订单号", length = 100, isNull = false)
    private String bizOrderNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "支付通道", length = 20, isNull = false)
    private String channel;

    /**
     * 关闭类型 关闭/撤销
     * @see PayCloseTypeEnum
     */
    @DbColumn(comment = "关闭类型", length = 20, isNull = false)
    private String closeType;

    /**
     * 是否关闭成功
     */
    @DbColumn(comment = "是否关闭成功", isNull = false)
    private boolean closed;

    /** 错误码 */
    @DbColumn(comment = "错误码", length = 10)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息", length = 150)
    private String errorMsg;

    /** 终端ip */
    @DbColumn(comment = "支付终端ip", length = 64)
    private String clientIp;

    /**
     * 转换
     */
    @Override
    public PayCloseRecordDto toDto() {
        return PayCloseRecordConvert.CONVERT.convert(this);
    }
}
