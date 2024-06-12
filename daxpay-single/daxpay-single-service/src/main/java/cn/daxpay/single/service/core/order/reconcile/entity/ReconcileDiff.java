package cn.daxpay.single.service.core.order.reconcile.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.ReconcileTradeEnum;
import cn.daxpay.single.service.code.ReconcileDiffTypeEnum;
import cn.daxpay.single.service.common.typehandler.ReconcileDiffTypeHandler;
import cn.daxpay.single.service.core.order.reconcile.conver.ReconcileConvert;
import cn.daxpay.single.service.core.payment.reconcile.domain.ReconcileDiffDetail;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileDiffDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 对账差异单
 * @author xxm
 * @since 2024/2/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "对账差异单")
@TableName(value = "pay_reconcile_diff_record",autoResultMap = true)
public class ReconcileDiff extends MpBaseEntity implements EntityBaseFunction<ReconcileDiffDto> {

    /** 对账单ID */
    @DbColumn(comment = "对账单ID", isNull = false)
    private Long reconcileId;

    /** 对账号 */
    @DbColumn(comment = "对账号", length = 32, isNull = false)
    private String reconcileNo;

    /** 对账单明细ID */
    @DbColumn(comment = "对账单明细ID", isNull = false)
    private Long detailId;

    /** 对账日期 */
    @DbColumn(comment = "对账日期", isNull = false)
    private LocalDate reconcileDate;

    /** 本地交易号 */
    @DbColumn(comment = "本地交易号", length = 32, isNull = false)
    private String tradeNo;

    /** 通道交易号 */
    @DbColumn(comment = "通道交易号", length = 150, isNull = false)
    private String outTradeNo;

    /** 交易时间 */
    @DbColumn(comment = "交易时间", isNull = false)
    private LocalDateTime tradeTime;

    /** 订单标题 */
    @DbColumn(comment = "订单标题", length = 100)
    private String title;

    /**
     * 通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "通道", length = 20, isNull = false)
    private String channel;

    /** 本地交易金额 */
    @DbColumn(comment = "本地交易金额", length = 15)
    private Integer amount;


    /** 通道交易金额 */
    @DbColumn(comment = "通道交易金额", length = 15)
    private Integer outAmount;

    /**
     * 交易类型
     * @see ReconcileTradeEnum
     */
    @DbColumn(comment = "交易类型", length = 20)
    private String tradeType;

    /**
     * 差异类型
     * @see ReconcileDiffTypeEnum
     */
    @DbColumn(comment = "差异类型", length = 20)
    private String diffType;

    /**
     * 差异内容, 存储json字符串, 格式为
     * {属性: '标题', 本地字段值:'标题1', 网关字段值: '标题2'}
     * @see ReconcileDiffDetail
     */
    @DbColumn(comment = "差异内容")
    @TableField(typeHandler = ReconcileDiffTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private List<ReconcileDiffDetail> diffs;

    @Override
    public ReconcileDiffDto toDto() {
        return ReconcileConvert.CONVERT.convert(this);
    }
}
