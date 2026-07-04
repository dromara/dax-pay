package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvKeyConfigConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商密钥配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_key_config", autoResultMap = true)
public class WechatIsvKeyConfig extends MpBaseEntity implements ToResult<WechatIsvKeyConfigResult> {

    /// 产品编码
    private String product;

    /// 微信服务商商户号
    private String wxMchId;

    /// API V3密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String apiKeyV3;

    /// 支付公钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 支付公钥ID
    private String publicKeyId;

    /// apiclient_key证书(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// apiclient_cert证书(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateCert;

    /// 证书序列号
    private String certSerialNo;

    /// 转换
    @Override
    public WechatIsvKeyConfigResult toResult() {
        return WechatIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
