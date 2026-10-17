package cn.daxpay.open.channel.xingyi.entity.direct;

import cn.daxpay.open.channel.xingyi.convert.direct.XingyiDirectKeyConfigConvert;
import cn.daxpay.open.channel.xingyi.result.direct.XingyiDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 星驿付直连配置
///
/// 直连商户维度的通道配置: 机构号(agetId)+星驿付商户号(custId)创建时录入,
/// 商户 RSA 公钥(请求签名与回调验签共用, 敏感字段加密存储)由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "xingyi_direct_key_config", autoResultMap = true)
public class XingyiDirectKeyConfig extends MchBaseEntity implements ToResult<XingyiDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 机构号(agetId, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String agetId;

    /// 星驿付商户号(custId, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String custId;

    /// 商户RSA公钥(Base64, 兼容 PEM 头尾; 请求签名与回调验签共用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPublicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public XingyiDirectKeyConfigResult toResult() {
        return XingyiDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
