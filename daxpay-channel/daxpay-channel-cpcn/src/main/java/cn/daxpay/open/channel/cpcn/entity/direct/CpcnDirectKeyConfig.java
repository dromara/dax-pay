package cn.daxpay.open.channel.cpcn.entity.direct;

import cn.daxpay.open.channel.cpcn.convert.direct.CpcnDirectKeyConfigConvert;
import cn.daxpay.open.channel.cpcn.result.direct.CpcnDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 中金直连配置
///
/// 直连商户维度的通道配置: 中金机构编号(InstitutionID)、网关地址(开户下发)、
/// 指定分账子商户号与两份证书材料(商户 pfx(SM2 私钥) + 中金平台证书, Base64 文本, 敏感字段加密存储)。
/// institutionId 创建时录入, 证书与网关地址由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "cpcn_direct_key_config", autoResultMap = true)
public class CpcnDirectKeyConfig extends MchBaseEntity implements ToResult<CpcnDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 中金机构编号(InstitutionID, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String institutionId;

    /// 中金网关地址(开户开通后下发, 无统一公开网关)
    private String gatewayUrl;

    /// 指定分账子商户号(可空, 多个用英文逗号分隔, 上送 Extension.AppointMerchantInfos)
    private String appointMerchantIds;

    /// 商户证书 pfx 内容(Base64, PKCS12, 国密 SM2 私钥, 请求签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPfx;

    /// 商户证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPassword;

    /// 中金平台证书 cer 内容(Base64/PEM, 响应/通知验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformCert;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public CpcnDirectKeyConfigResult toResult() {
        return CpcnDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
