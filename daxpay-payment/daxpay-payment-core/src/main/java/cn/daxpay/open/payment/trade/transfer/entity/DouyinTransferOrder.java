package cn.daxpay.open.payment.trade.transfer.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 抖音转账单
///
/// 抖音通道独立的转账记录表，承载抖音特有收款人字段。
/// 与公共转账凭证 [TransferTrade] 通过 containerId+containerChannel 关联。
///
/// 号段三元组：
/// - [transferNo] 平台身份
/// - [bizTransferNo] 商户转账号（幂等键）
/// - [outTransferNo] 通道返回的转账单号(抖音 orderId)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "pay_transfer_order_douyin", autoResultMap = true)
public class DouyinTransferOrder extends MchBaseEntity {

    // ===== 身份 =====

    /// 平台转账单号
    private String transferNo;

    /// 商户转账号(商户传入, 商户侧唯一)
    private String bizTransferNo;

    /// 通道商户号(路由确定后写入, 凭证组装用)
    private String channelMchNo;

    /// 通道转账单号(抖音 orderId)
    private String outTransferNo;

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

    // ===== 抖音特有 =====

    /// 收款人账号类型
    private String payeeType;

    /// 收款人账号(敏感字段, AES-256-GCM 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String payeeAccount;

    /// 收款人姓名(敏感字段, AES-256-GCM 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String payeeName;

    /// 转账场景ID(发起转账时由前端选择的主数据枚举, 如1001-1007)
    private String transferScene;

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


