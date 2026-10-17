package cn.daxpay.open.channel.jeepay.entity;

import cn.daxpay.open.channel.jeepay.convert.JeepayKeyConfigConvert;
import cn.daxpay.open.channel.jeepay.result.JeepayKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Jeepay通道密钥配置
///
/// 通道商户维度的 Jeepay 计全聚合平台对接配置, 含平台地址/商户号/应用ID/应用密钥,
/// 敏感字段(应用密钥)通过 [DataEncryptTypeHandler] 加密入库。
/// channelMchNo 创建时录入不可修改; 密钥字段由密钥配置维护(空值不覆盖)。
///
/// 签名算法: MD5(appSecret 参与待签串, 请求签名与回调验签共用)。
/// Jeepay 不提供集测环境, 不设沙箱字段, 对接配置中的平台地址。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "jee_pay_key_config", autoResultMap = true)
public class JeepayKeyConfig extends MchBaseEntity implements ToResult<JeepayKeyConfigResult> {

    /// 通道商户号(关联 mch_channel_merchant, 创建后不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// Jeepay平台网关地址(如 https://pay.jeepay.vip)
    private String serverUrl;

    /// Jeepay商户号(mchNo, 上游平台分配; 注意与平台商户号[mchNo]区分)
    private String jeepayMchNo;

    /// 应用ID(appId, Jeepay 商户后台的应用标识)
    private String appId;

    /// 应用密钥(appSecret, MD5 签名钥, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    @Override
    public JeepayKeyConfigResult toResult() {
        return JeepayKeyConfigConvert.CONVERT.toResult(this);
    }
}
