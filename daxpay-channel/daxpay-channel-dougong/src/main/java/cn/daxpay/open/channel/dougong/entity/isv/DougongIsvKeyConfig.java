package cn.daxpay.open.channel.dougong.entity.isv;

import cn.daxpay.open.channel.dougong.convert.isv.DougongIsvKeyConfigConvert;
import cn.daxpay.open.channel.dougong.result.isv.DougongIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 斗拱服务商密钥配置
///
/// 斗拱(汇付天下)为服务商模式, 服务商密钥全局唯一(按 product 查询),
/// 子商户仅需商户号(merchantNo) + appId, 见 [DougongIsvChannelMerchant]。
///
/// 签名算法: RSA(汇付 SDK 内部处理), 私钥签名 / 公钥验签。
/// 敏感字段(私钥/公钥)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "dougong_isv_key_config", autoResultMap = true)
public class DougongIsvKeyConfig extends MpBaseEntity implements ToResult<DougongIsvKeyConfigResult> {

    /// 产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 服务商系统ID(sysId)
    private String sysId;

    /// 产品号(productId)
    private String productId;

    /// 商户 RSA 私钥(PEM, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 斗拱 RSA 公钥(PEM, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String dgPublicKey;

    /// 转换
    @Override
    public DougongIsvKeyConfigResult toResult() {
        return DougongIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
