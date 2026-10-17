package cn.daxpay.open.channel.yizhifubj.entity.direct;

import cn.daxpay.open.channel.yizhifubj.convert.direct.YizhifubjDirectKeyConfigConvert;
import cn.daxpay.open.channel.yizhifubj.result.direct.YizhifubjDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 一直付直连配置
///
/// 直连商户维度的通道配置: 一直付商户编号(merchantId, 创建时录入)与服务端ID/报备序列号/商户 pfx 证书
/// (敏感字段加密存储; 5upay 平台证书可空, 空时子应用回退内置默认证书)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "yizhifubj_direct_key_config", autoResultMap = true)
public class YizhifubjDirectKeyConfig extends MchBaseEntity implements ToResult<YizhifubjDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 一直付商户编号(merchantId, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merchantId;

    /// 服务端ID(partnerId, 可空, 机构服务端形态下下发)
    private String partnerId;

    /// 报备序列号(reportSerialNo, 可空, 下单上送通道侧报备用)
    private String reportSerialNo;

    /// 商户 pfx 证书内容(Base64, PKCS12, 请求签名与 encryptKey 解密用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String certPfx;

    /// 商户证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String certPassword;

    /// 5upay 平台证书内容(Base64/DER, 可空, AES 密钥加密与验签用, 空时用子应用内置默认证书, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformCert;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public YizhifubjDirectKeyConfigResult toResult() {
        return YizhifubjDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
