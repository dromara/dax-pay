package cn.daxpay.open.channel.douyin.entity.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinDirectKeyConfigConvert;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连密钥配置
///
/// 直连商户维度的密钥配置，一个抖音商户号共享一套密钥，与具体应用无关，敏感字段加密存储。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "douyin_direct_key_config", autoResultMap = true)
public class DouyinDirectKeyConfig extends MchBaseEntity implements ToResult<DouyinDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 商户私钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 商家公钥证书序列号
    private String merchantSerialNumber;

    /// 接口加密密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String encryptKey;

    /// 转换
    @Override
    public DouyinDirectKeyConfigResult toResult() {
        return DouyinDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
