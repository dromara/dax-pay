package cn.daxpay.open.channel.kuaiqian.entity.direct;

import cn.daxpay.open.channel.kuaiqian.convert.direct.KuaiqianDirectKeyConfigConvert;
import cn.daxpay.open.channel.kuaiqian.result.direct.KuaiqianDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 快钱直连配置
///
/// 直连商户维度的通道配置: 快钱会员号(memberCode, 人民币网网关商户账号 = memberCode + "01")、
/// 当面付商户/终端号与三份证书(商户 pfx / 平台 cer / SSL 客户端 p12, Base64 文本, 敏感字段加密存储)。
/// memberCode/vendorMemberCode/merchantId/terminalId 创建时录入, 证书由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "kuaiqian_direct_key_config", autoResultMap = true)
public class KuaiqianDirectKeyConfig extends MchBaseEntity implements ToResult<KuaiqianDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 快钱会员号/账户号(memberCode, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String memberCode;

    /// 服务商快钱子账户号(可空, 创建时录入; 设置后 head.memberCode=子账户号, vendorMemberCode=会员号)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String vendorMemberCode;

    /// 当面付商户号(merchantId)
    private String merchantId;

    /// 当面付终端号(terminalId)
    private String terminalId;

    /// 商户证书 pfx 内容(Base64, PKCS12, 签名与响应解密用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPfx;

    /// 商户证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPassword;

    /// 快钱平台证书 cer 内容(Base64/PEM, 报文加密与验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformCert;

    /// SSL 客户端证书 p12 内容(Base64, MAS 网关双向认证用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String sslCertP12;

    /// SSL 客户端证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String sslCertPassword;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public KuaiqianDirectKeyConfigResult toResult() {
        return KuaiqianDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
