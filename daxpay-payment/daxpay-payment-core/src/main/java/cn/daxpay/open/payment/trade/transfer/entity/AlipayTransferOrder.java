package cn.daxpay.open.payment.trade.transfer.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付宝转账单
///
/// 支付宝通道独立的转账记录表，承载支付宝特有字段（三种收款人类型 user_id/open_id/login_name）。
/// 与公共转账凭证 [TransferTrade] 通过 containerId+containerChannel 关联。
///
/// 号段三元组：
/// - [transferNo] 平台身份
/// - [bizTransferNo] 商户转账号（幂等键）
/// - [outTransferNo] 通道返回的转账单号(支付宝 order_id)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_transfer_order_alipay")
public class AlipayTransferOrder extends MchBaseEntity {

    // ===== 身份 =====

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 平台转账单号
    private String transferNo;

    /// 商户转账号(商户传入, 商户侧唯一)
    private String bizTransferNo;

    /// 通道商户号(路由确定后写入, 凭证组装用)
    private String channelMchNo;

    /// 通道转账单号(支付宝 order_id)
    private String outTransferNo;

    /// 支付宝资金流水号(pay_fund_order_id, 财务对账, 区别于 outTransferNo)
    private String payFundOrderId;

    // ===== 转账要素 =====

    /// 转账金额(最小货币单位, 分)
    private Long amount;

    /// 币种
    private String currency;

    /// 转账标题
    private String title;

    /// 转账原因/备注
    private String reason;

    /// 转账状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    private String status;

    /// 转账完成时间
    private OffsetDateTime finishTime;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    // ===== 支付宝特有 =====

    /// 收款人账号类型(user_id/open_id/login_name)
    /// @see cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum
    private String payeeType;

    /// 收款人账号
    private String payeeAccount;

    /// 收款人姓名
    private String payeeName;

    /// 转账场景配置ID(支付宝专用, FAIL重试时恢复场景用)
    private String transferSceneConfigId;

    /// 转账场景报备信息(JSON序列化, FAIL重试时恢复报备用)
    private String reportInfos;

    // ===== 商户出站 / 审计 =====

    /// 异步通知地址(出站商户通知用)
    private String notifyUrl;

    /// 商户附加参数(回调原样返回)
    private String attach;

    /// 请求时间
    private OffsetDateTime reqTime;
}

