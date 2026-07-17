package cn.daxpay.open.payment.trade.order.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 退款订单
///
/// 记录每笔退款交易（单表流水，不拆 Trade）。
/// 与原支付资金凭证通过 [tradeNo](= [PayTrade#getTradeNo]) 关联。
/// 支持普通支付与网关支付容器; 同一支付可多次部分退款, 每笔一条记录, refundNo 唯一。
/// 资金口径: 发起时预占 [PayTrade#getRefundableBalance], SUCCESS 不二次扣, FAIL/CLOSE 回滚。
///
/// 号段三元组:
/// - [refundNo] 平台身份
/// - [relationOrderNo] 实际上送通道的商户退款关联号（普通通道 = refundNo；特殊通道可变形）
/// - [outRefundNo] 通道返回的退款流水号
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_refund_order")
public class RefundOrder extends MchBaseEntity {

    // ===== 身份 =====

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 退款号(平台统一生成, 全局唯一)
    private String refundNo;

    /// 商户退款号(商户传入, 商户侧唯一)
    private String bizRefundNo;

    /// 实际上送通道的商户退款关联号（回调/同步反查权威）
    /// 普通通道 = refundNo；特殊通道（前缀/长度限制）= 变形号，可与 refundNo 不同
    /// 对称 [PayTrade#getRelationOrderNo]
    private String relationOrderNo;

    // ===== 关联原支付资金凭证 =====

    /// 原支付资金交易号(= [PayTrade#getTradeNo]，非容器 orderNo)
    private String tradeNo;

    /// 原支付交易形态(冗余自 [PayTrade#getTradeType]，列表/筛选免 JOIN)
    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    private String tradeType;

    /// 商户业务订单号(列表/查询冗余，权威在原支付容器)
    private String bizOrderNo;

    /// 通道支付订单号(三方通道返回的 trade_no)
    private String outOrderNo;

    /// 标题(列表展示冗余，继承自原支付容器)
    private String title;

    // ===== 本笔退款 =====

    /// 通道退款流水号(退款成功/受理后由通道返回)
    private String outRefundNo;

    /// 退款金额(最小货币单位, 分)
    private Long amount;

    /// 订单总金额(冗余自原支付, 通道 total_amount 用)
    private Long orderAmount;

    /// 币种
    private String currency;

    /// 退款原因
    private String reason;

    /// 退款状态
    /// @see RefundOrderStatusEnum
    private String status;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    // ===== 通道凭证快照（继承原支付，禁止二次路由）=====

    /// 支付通道
    private String channel;

    /// 支付产品编码(策略选型)
    private String product;

    /// 通道商户号(路由回填)
    private String channelMchNo;

    /// 支付能力编码（仅通道凭证组装运行时使用；管理端不展示。策略选型权威在 product）
    private String capability;

    /// 通道应用 AppId（继承自原支付单快照，退款组装凭证用）
    private String channelAppId;

    // ===== 商户出站 / 审计 =====

    /// 异步通知地址(出站商户通知用)
    private String notifyUrl;

    /// 商户附加参数(回调原样返回)
    private String attach;

    /// 客户端 IP
    private String clientIp;

    /// 门店号（继承自原支付容器，可空；对应 [MchStoreInfo#storeNo]）
    private String storeNo;
}
