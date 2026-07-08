package cn.daxpay.open.channel.ums.entity.direct;

import cn.daxpay.open.channel.ums.convert.direct.UmsDirectKeyConfigConvert;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 银联商务直连配置
///
/// 直连商户维度的通道配置, 含商户身份(mid/tid)与签名密钥, 敏感字段(appKey/secretKey)加密存储。
/// merchantNo(mid) 创建时录入不可修改, terminalNo(tid)/umsAppId/appKey/secretKey 由密钥配置维护。
/// 银联商务签名无需证书, 仅依赖 appKey(HmacSHA256) 与 secretKey(回调验签)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "ums_direct_key_config", autoResultMap = true)
public class UmsDirectKeyConfig extends MchBaseEntity implements ToResult<UmsDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 银联商务商户号(mid, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merchantNo;

    /// 终端号(tid)
    private String terminalNo;

    /// 银联商务应用 AppId
    private String umsAppId;

    /// 应用密钥(HmacSHA256 签名密钥, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appKey;

    /// 通讯密钥(回调验签 MD5/SHA256 拼接密钥, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secretKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public UmsDirectKeyConfigResult toResult() {
        return UmsDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
