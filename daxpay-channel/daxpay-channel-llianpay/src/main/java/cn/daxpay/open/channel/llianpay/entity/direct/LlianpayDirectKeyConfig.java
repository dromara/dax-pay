package cn.daxpay.open.channel.llianpay.entity.direct;

import cn.daxpay.open.channel.llianpay.convert.direct.LlianpayDirectKeyConfigConvert;
import cn.daxpay.open.channel.llianpay.result.direct.LlianpayDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 连连支付直连配置
///
/// 直连商户维度的通道配置: 连连商户编号(mch_id, 创建时录入不可修改)、
/// 商户私钥与连连平台公钥(Base64 裸串, 敏感字段加密存储, 由密钥配置维护)。
/// sub_mchid/payee_uid/busi_type 为平台商/收款方业务参数, 可选维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "llianpay_direct_key_config", autoResultMap = true)
public class LlianpayDirectKeyConfig extends MchBaseEntity implements ToResult<LlianpayDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 连连商户编号(mch_id, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mchId;

    /// 二级商户号(可空; 平台商模式上送 sub_mchid)
    private String subMchid;

    /// 收款方用户ID(可空; 留空为商户自身收款, 配置后用户收款)
    private String payeeUid;

    /// 业务属性(busi_type, 如 100001 虚拟商品购买)
    private String busiType;

    /// 商户私钥(Base64 裸串, 兼容 PKCS1/PKCS8, 请求签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 连连平台公钥(Base64 裸串, X509 格式, 响应/回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String llianpayPublicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public LlianpayDirectKeyConfigResult toResult() {
        return LlianpayDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
