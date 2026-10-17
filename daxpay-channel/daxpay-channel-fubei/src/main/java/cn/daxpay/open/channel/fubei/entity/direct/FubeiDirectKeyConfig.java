package cn.daxpay.open.channel.fubei.entity.direct;

import cn.daxpay.open.channel.fubei.convert.direct.FubeiDirectKeyConfigConvert;
import cn.daxpay.open.channel.fubei.result.direct.FubeiDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 付呗直连配置
///
/// 直连商户维度的通道配置: 付呗开放平台应用ID(app_id)、接口密钥(app_secret)、
/// 商户ID(merchant_id, 服务商形态)与门店ID(store_id), 敏感密钥加密存储。
/// app_id/merchant_id/store_id 创建时录入, app_secret 由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "fubei_direct_key_config", autoResultMap = true)
public class FubeiDirectKeyConfig extends MchBaseEntity implements ToResult<FubeiDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 付呗开放平台应用ID(app_id, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 付呗商户ID(merchant_id, 服务商形态时必传)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merchantId;

    /// 付呗门店ID(store_id, 交易必传)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String storeId;

    /// 接口密钥(app_secret, MD5 签名与回调验签共用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 是否沙箱环境(付呗无独立沙箱网关, 预留双环境密钥并存)
    private Boolean sandbox;

    @Override
    public FubeiDirectKeyConfigResult toResult() {
        return FubeiDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
