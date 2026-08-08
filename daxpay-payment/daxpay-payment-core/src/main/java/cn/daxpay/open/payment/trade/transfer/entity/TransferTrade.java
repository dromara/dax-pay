package cn.daxpay.open.payment.trade.transfer.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 转账资金凭证
///
/// 公共转账记录表（跨通道统一查询/统计），仿资金交易凭证 [PayTrade] 设计：
/// 只保留资金固有属性 + 通道反查命脉字段，业务上下文留在各通道转账单（容器）。
///
/// - [containerId] + [containerChannel] 关联对应通道转账单主键
/// - [relationNo] 实际上送通道的商户转账号（回调/同步反查权威）
/// - [outTransferNo] 通道返回的转账单号
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_transfer_trade")
public class TransferTrade extends MchBaseEntity {

    /// 平台转账交易号(全局唯一)
    private String tradeNo;

    /// 商户转账号(冗余自容器, 同步记录/日志免回容器; 权威在容器 bizTransferNo)
    private String bizTransferNo;

    /// 关联通道转账单ID(容器主键)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long containerId;

    /// 所属通道(wechat/alipay/douyin)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String containerChannel;

    /// 通道编码(冗余, 跨通道统计)
    private String channel;

    /// 钱包渠道(wechat/alipay/douyin)
    private String provider;

    /// 转账金额(最小货币单位, 分)
    private Long amount;

    /// 币种
    private String currency;

    /// 转账状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    private String status;

    /// 通道转账单号
    private String outTransferNo;

    /// 实际上送通道的商户转账号(反查权威)
    private String relationNo;

    /// 转账完成时间
    private OffsetDateTime finishTime;

    /// 转账标题
    private String title;
}
