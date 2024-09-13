package cn.daxpay.single.service.core.order.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.core.code.AllocDetailResultEnum;
import cn.daxpay.single.core.code.AllocReceiverTypeEnum;
import cn.daxpay.single.service.common.typehandler.DecryptTypeHandler;
import cn.daxpay.single.service.core.order.allocation.convert.AllocOrderConvert;
import cn.daxpay.single.service.dto.order.allocation.AllocOrderDetailDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分账订单明细
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "分账订单明细")
@TableName(value = "pay_alloc_order_detail",autoResultMap = true)
public class AllocOrderDetail extends MpBaseEntity implements EntityBaseFunction<AllocOrderDetailDto> {

    /** 分账订单ID */
    @DbMySqlIndex(comment = "分账订单ID索引")
    @DbColumn(comment = "分账订单ID", isNull = false)
    private Long allocationId;

    /** 接收者ID */
    @DbColumn(comment = "接收者ID", isNull = false)
    private Long receiverId;

    /** 分账接收方编号 */
    @DbColumn(comment = "分账接收方编号", length = 20, isNull = false)
    private String receiverNo;

    /** 分账比例 */
    @DbColumn(comment = "分账比例(万分之多少)", length = 5, isNull = false)
    private Integer rate;

    /** 分账金额 */
    @DbColumn(comment = "分账金额", length = 8, isNull = false)
    private Integer amount;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    @DbColumn(comment = "分账接收方类型", length = 20, isNull = false)
    private String receiverType;

    /** 接收方账号 */
    @DbColumn(comment = "接收方账号", length = 100, isNull = false)
    @TableField(typeHandler = DecryptTypeHandler.class)
    private String receiverAccount;

    /** 接收方姓名 */
    @DbColumn(comment = "接收方姓名", length = 100)
    private String receiverName;

    /**
     * 分账结果
     * @see AllocDetailResultEnum
     */
    @DbColumn(comment = "分账结果", length = 20, isNull = false)
    private String result;

    /** 错误代码 */
    @DbColumn(comment = "错误代码", length = 10)
    private String errorCode;

    /** 错误原因 */
    @DbColumn(comment = "错误原因", length = 150)
    private String errorMsg;

    /** 分账完成时间 */
    @DbColumn(comment = "分账完成时间")
    private LocalDateTime finishTime;

    /**
     * 转换
     */
    @Override
    public AllocOrderDetailDto toDto() {
        return AllocOrderConvert.CONVERT.convert(this);
    }
}
