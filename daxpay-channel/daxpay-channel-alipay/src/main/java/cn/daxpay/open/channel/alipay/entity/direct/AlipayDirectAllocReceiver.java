package cn.daxpay.open.channel.alipay.entity.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectAllocReceiverConvert;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAllocReceiverResult;
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

/// # 支付宝直连分账接收方
///
/// 通道侧注册(绑定)的分账接收方档案, 挂通道商户([channelMchNo])维度。
/// 绑定调 alipay.trade.royalty.relation.bind(out_request_no=记录id 幂等), 解绑调 unbind。
///
/// [receiverAccount] / [receiverName] 使用 AES-256-GCM 加密存储(复用 [DataEncryptTypeHandler]);
/// [accountHash] 存账号 SHA-256 明文哈希, 用于查重与等值定位(随机 IV 导致密文不可比较)。
///
/// 运营端(admin)不装载商户上下文, insert 时必须显式 [setMchNo]。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_direct_alloc_receiver", autoResultMap = true)
public class AlipayDirectAllocReceiver extends MchBaseEntity implements ToResult<AlipayDirectAllocReceiverResult> {

    /// 通道商户号(关联通用通道商户主表)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 接收方类型
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String receiverType;

    /// 接收方账号(AES-256-GCM 加密存储; userId 为 2088 开头, loginName 为手机号/邮箱)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverAccount;

    /// 接收方账号 SHA-256 哈希(查重与等值定位)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String accountHash;

    /// 接收方名称(AES-256-GCM 加密存储, 可空)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverName;

    /// 发起绑定的支付宝应用引用(alipay_direct_app 主键, 重新绑定复用)
    private Long directAppRefId;

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
    public AlipayDirectAllocReceiverResult toResult() {
        return AlipayDirectAllocReceiverConvert.CONVERT.toResult(this);
    }
}
