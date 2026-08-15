package cn.daxpay.open.channel.alipay.entity.isv;

import cn.daxpay.open.channel.alipay.convert.isv.AlipayIsvAllocReceiverConvert;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAllocReceiverResult;
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

/// # 支付宝服务商分账接收方
///
/// 通道侧注册(绑定)的分账接收方档案, 挂通道商户([channelMchNo])维度。
/// 绑定调 alipay.trade.royalty.relation.bind(服务商代调用, app_auth_token 取自子商户授权绑定,
/// 凭证组装全自动无需存应用字段), 解绑调 unbind。
///
/// [receiverAccount] / [receiverName] 使用 AES-256-GCM 加密存储(复用 [DataEncryptTypeHandler]);
/// [accountHash] 存账号 SHA-256 明文哈希, 用于查重与等值定位(随机 IV 导致密文不可比较)。
///
/// 运营端(admin)不装载商户上下文, insert 时必须显式 [setMchNo]。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_isv_alloc_receiver", autoResultMap = true)
public class AlipayIsvAllocReceiver extends MchBaseEntity implements ToResult<AlipayIsvAllocReceiverResult> {

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
    public AlipayIsvAllocReceiverResult toResult() {
        return AlipayIsvAllocReceiverConvert.CONVERT.toResult(this);
    }
}
