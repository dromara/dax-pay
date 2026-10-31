package cn.daxpay.open.channel.mock.entity.alloc;

import cn.daxpay.open.channel.mock.convert.alloc.MockAllocReceiverConvert;
import cn.daxpay.open.channel.mock.result.alloc.MockAllocReceiverResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # Mock 通道分账接收方
///
/// 通道侧注册(绑定)的分账接收方档案, 挂通道商户([channelMchNo])维度, 拟真微信/支付宝/抖音的接收方报备。
/// 新增/重新绑定时同步调通道子应用 `/channel/mock/alloc-receiver/bind`, 解绑调 `unbind`。
///
/// 两产品共用一张表: mock 零资料接入、无应用维度, 仅以 [product] 区分 mock_pay/mock_isv,
/// 不照抄真实通道"每模式一表"(那是通道字段差异导致, mock 无此约束)。
///
/// [receiverAccount] / [receiverName] 使用 AES-256-GCM 加密存储(复用 [DataEncryptTypeHandler]);
/// [accountHash] 存账号 SHA-256 明文哈希, 用于查重与等值定位(随机 IV 导致密文不可比较)。
///
/// 运营端(admin)不装载商户上下文, insert 时必须显式 [setMchNo]。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_alloc_receiver", autoResultMap = true)
public class MockAllocReceiver extends MchBaseEntity implements ToResult<MockAllocReceiverResult> {

    /// 通道商户号(关联通用通道商户主表)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 所属支付产品(mock_pay/mock_isv, 冗余自通道商户, 建行后不可改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String product;

    /// 接收方类型
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String receiverType;

    /// 接收方账号(AES-256-GCM 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverAccount;

    /// 接收方账号 SHA-256 哈希(查重与等值定位)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String accountHash;

    /// 接收方名称(AES-256-GCM 加密存储; MERCHANT_ID 时必填商户全称)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverName;

    /// 接收方别名(本地备注, 不上送通道, 可随时修改)
    /// updateStrategy=ALWAYS 保证清空别名(null)可落库
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String alias;

    /// 绑定状态
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum
    private String status;

    /// 最近一次绑定/解绑失败原因
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /// 绑定成功时间
    private OffsetDateTime bindTime;

    /// 解绑成功时间
    private OffsetDateTime unbindTime;

    /// 转换
    @Override
    public MockAllocReceiverResult toResult() {
        return MockAllocReceiverConvert.CONVERT.toResult(this);
    }
}
