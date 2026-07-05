package cn.daxpay.open.channel.lakala.entity.isv;

import cn.daxpay.open.channel.lakala.convert.isv.LakalaIsvKeyConfigConvert;
import cn.daxpay.open.channel.lakala.result.isv.LakalaIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 拉卡拉服务商密钥配置
///
/// 拉卡拉为收单机构服务商模式, 服务商密钥全局唯一(按 product 查询),
/// 子商户仅需商户号(merchantNo) + 终端号(termNo), 见 [LakalaIsvChannelMerchant]。
///
/// 签名算法: RSA2(SHA256withRSA), 私钥签名 / 公钥验签。
/// 敏感字段(私钥/公钥/sm4Key)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "lakala_isv_key_config", autoResultMap = true)
public class LakalaIsvKeyConfig extends MpBaseEntity implements ToResult<LakalaIsvKeyConfigResult> {

    /// 产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 拉卡拉应用编号(lkl_app_id)
    private String lklAppId;

    /// 商户证书序列号
    private String mchSerialNo;

    /// 商户RSA私钥(PEM, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 拉卡拉RSA公钥(PEM, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// SM4对称密钥(进件敏感字段加密用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String sm4Key;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 转换
    @Override
    public LakalaIsvKeyConfigResult toResult() {
        return LakalaIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
