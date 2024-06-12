package cn.daxpay.single.service.core.record.sync.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.code.RefundSyncStatusEnum;
import cn.daxpay.single.service.code.PaymentTypeEnum;
import cn.daxpay.single.service.core.record.sync.convert.PaySyncRecordConvert;
import cn.daxpay.single.service.dto.record.sync.PaySyncRecordDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付同步订单
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付同步订单")
@Accessors(chain = true)
@TableName("pay_sync_record")
public class PaySyncRecord extends MpCreateEntity implements EntityBaseFunction<PaySyncRecordDto> {

    /** 本地交易号 */
    @DbMySqlIndex(comment = "本地交易号索引")
    @DbColumn(comment = "本地交易号", length = 32, isNull = false)
    private String tradeNo;

    /** 商户交易号 */
    @DbMySqlIndex(comment = "商户交易号索引")
    @DbColumn(comment = "商户交易号", length = 100, isNull = false)
    private String bizTradeNo;

    /** 通道交易号 */
    @DbMySqlIndex(comment = "通道交易号索引")
    @DbColumn(comment = "通道交易号", length = 150, isNull = false)
    private String outTradeNo;

    /**
     * 三方支付返回状态
     * @see PaySyncStatusEnum
     * @see RefundSyncStatusEnum
     */
    @DbColumn(comment = "网关返回状态", length = 20, isNull = false)
    private String outTradeStatus;

    /**
     * 同步类型 支付/退款
     * @see PaymentTypeEnum
     */
    @DbColumn(comment = "同步类型", length = 20, isNull = false)
    private String syncType;

    /**
     * 同步的异步通道
     * @see PayChannelEnum#getCode()
     */
    @DbColumn(comment = "同步的异步通道", length = 20, isNull = false)
    private String channel;

    /** 网关返回的同步消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "同步消息", isNull = false)
    private String syncInfo;

    /**
     * 支付单如果状态不一致, 是否进行修复
     */
    @DbColumn(comment = "是否进行修复", isNull = false)
    private boolean repair;

    /** 修复单号 */
    @DbColumn(comment = "修复单号", length = 32)
    private String repairNo;

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
    public PaySyncRecordDto toDto() {
        return PaySyncRecordConvert.CONVERT.convert(this);
    }
}
