package cn.daxpay.open.channel.wechat.entity.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectAllocReceiverConvert;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAllocReceiverResult;
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

/// # 微信直连分账接收方
///
/// 通道侧注册(绑定)的分账接收方档案, 挂通道商户([channelMchNo])维度。
/// 新增/重新绑定时同步调通道 V3 profitsharing/receivers/add, 解绑调 delete。
///
/// [receiverAccount] / [receiverName] 使用 AES-256-GCM 加密存储(复用 [DataEncryptTypeHandler]);
/// [accountHash] 存账号 SHA-256 明文哈希, 用于查重与等值定位(随机 IV 导致密文不可比较)。
///
/// 运营端(admin)不装载商户上下文, insert 时必须显式 [setMchNo]。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_direct_alloc_receiver", autoResultMap = true)
public class WechatDirectAllocReceiver extends MchBaseEntity implements ToResult<WechatDirectAllocReceiverResult> {

    /// 通道商户号(关联通用通道商户主表)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 接收方类型
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String receiverType;

    /// 接收方账号(AES-256-GCM 加密存储; openid 为 channelAppId 维度)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverAccount;

    /// 接收方账号 SHA-256 哈希(查重与等值定位)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String accountHash;

    /// 接收方名称(AES-256-GCM 加密存储; MERCHANT_ID 时必填商户全称)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String receiverName;

    /// 分账关系类型(平台小写, 通道适配层转微信原生大写)
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocRelationTypeEnum
    private String relationType;

    /// 自定义分账关系名(relationType=CUSTOM 时必填)
    private String customRelation;

    /// 绑定时所用商户档微信应用 appid(重新绑定复用)
    private String channelAppId;

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
    public WechatDirectAllocReceiverResult toResult() {
        return WechatDirectAllocReceiverConvert.CONVERT.toResult(this);
    }
}
