package cn.daxpay.open.channel.jlpay.entity.direct;

import cn.daxpay.open.channel.jlpay.convert.direct.JlpayDirectKeyConfigConvert;
import cn.daxpay.open.channel.jlpay.result.direct.JlpayDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 嘉联直连配置
///
/// 直连商户维度的通道配置: 嘉联应用 appid、商户号(mch_id)、终端号(term_no)与
/// SM2 密钥对(商户私钥签名用 + 平台公钥验签用, hex 或 PEM 文本, 敏感字段加密存储)。
/// appId/mchId 创建时录入不可修改, 终端号与 SM2 密钥由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "jlpay_direct_key_config", autoResultMap = true)
public class JlpayDirectKeyConfig extends MchBaseEntity implements ToResult<JlpayDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 嘉联开放平台应用 appid(请求头 x-jlpay-appid, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 嘉联商户号(mch_id, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mchId;

    /// 嘉联终端号(term_no)
    private String termNo;

    /// 商户 SM2 私钥(hex 或 PEM 文本, 请求签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 嘉联平台 SM2 公钥(hex 或 PEM 文本, 响应/通知验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    /// 是否沙箱环境(true 走 UAT 网关 openapi-uat.jlpay.com)
    private Boolean sandbox;

    @Override
    public JlpayDirectKeyConfigResult toResult() {
        return JlpayDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
