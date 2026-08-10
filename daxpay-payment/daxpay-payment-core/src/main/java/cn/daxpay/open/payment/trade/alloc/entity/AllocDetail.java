package cn.daxpay.open.payment.trade.alloc.entity;

import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 分账明细
///
/// 每个接收方一行, 记录单笔分账中各接收方的金额与结果。
/// 与分账单通过 [allocNo](= [AllocOrder#getAllocNo]) 关联, 不存 FK。
///
/// 子表不参与 mch 行级隔离(继承 [MpBaseEntity] 而非 MchBaseEntity),
/// 通过 [allocNo] 挂主表外键, 租户隔离由主表 [AllocOrder] 承载。
///
/// [receiverAccount] / [receiverName] 使用 AES-256-GCM 加密存储(复用 [DataEncryptTypeHandler])。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "pay_alloc_detail", autoResultMap = true)
public class AllocDetail extends MpBaseEntity {

    /// 分账单号(关联 [AllocOrder#getAllocNo])
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String allocNo;

    /// 接收方类型
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
    private String receiverType;

    /// 接收方账号(AES-256-GCM 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverAccount;

    /// 接收方姓名(AES-256-GCM 加密存储, 可空)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverName;

    /// 分账金额(分)
    private Long amount;

    /// 明细结果
    /// @see AllocDetailResultEnum
    private String result;

    /// 通道侧明细ID(同步/回调时回填)
    private String outDetailId;

    /// 错误码
    private String errorCode;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /// 明细完成时间
    private OffsetDateTime finishTime;
}
