package cn.daxpay.open.payment.trade.alloc.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 分账订单
///
/// 记录每笔分账交易(单表流水), 继承原支付通道快照, 禁止二次路由。
/// 与原支付资金凭证通过 [tradeNo](= [PayTrade#getTradeNo]) 关联。
/// 同一支付订单支持多次分账(每次一条记录), allocNo 唯一;
/// [pay_trade.alloc_status] 在分账完成后标记为 done, 防止重复分账。
///
/// 号段三元组:
/// - [allocNo] 平台身份(平台分账单号)
/// - [bizAllocNo] 幂等键(商户分账单号, 商户侧唯一)
/// - [outAllocNo] 通道分账单号(通道返回, 如支付宝 settle_no / 微信 transaction_id / 抖音 orderId)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_alloc_order")
public class AllocOrder extends MchBaseEntity {

    // ===== 身份 =====

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 分账单号(平台统一生成, 全局唯一)
    private String allocNo;

    /// 商户分账单号(商户传入, 商户侧唯一, 幂等键)
    private String bizAllocNo;

    // ===== 关联原支付资金凭证 =====

    /// 原支付资金交易号(= [PayTrade#getTradeNo])
    private String tradeNo;

    /// 原支付交易形态(冗余自 [PayTrade#getTradeType], 列表/筛选免 JOIN)
    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    private String tradeType;

    /// 商户业务订单号(列表/查询冗余, 权威在原支付容器)
    private String bizOrderNo;

    /// 通道支付订单号(三方通道返回的 trade_no, 分账上送通道用)
    private String outOrderNo;

    /// 标题(列表展示冗余, 继承自原支付容器)
    private String title;

    /// 分账描述
    private String description;

    // ===== 本笔分账 =====

    /// 通道分账单号(通道返回)
    private String outAllocNo;

    /// 分账总金额(各接收方金额之和, 分)
    private Long amount;

    /// 原订单总金额(冗余, 分)
    private Long orderAmount;

    /// 币种
    private String currency;

    /// 分账状态
    /// @see AllocOrderStatusEnum
    private String status;

    /// 分账完成时间
    private OffsetDateTime finishTime;

    /// 错误码
    private String errorCode;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    // ===== 通道凭证快照（继承原支付，禁止二次路由）=====

    /// 支付通道
    private String channel;

    /// 支付渠道
    private String provider;

    /// 支付产品编码(策略选型)
    private String product;

    /// 通道商户号(继承自原支付)
    private String channelMchNo;

    /// 支付能力编码(继承自原支付)
    private String capability;

    /// 通道应用 AppId(继承自原支付单快照)
    private String channelAppId;

    // ===== 商户出站 / 审计 =====

    /// 异步通知地址(出站商户通知用)
    private String notifyUrl;

    /// 商户附加参数(回调原样返回)
    private String attach;

    /// 客户端 IP
    private String clientIp;
}
