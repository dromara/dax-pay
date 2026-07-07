package cn.daxpay.open.channel.hkrt.entity.isv;

import cn.daxpay.open.channel.hkrt.convert.isv.HkrtIsvKeyConfigConvert;
import cn.daxpay.open.channel.hkrt.result.isv.HkrtIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 海科融通服务商密钥配置
///
/// 海科融通为收单机构服务商模式, 服务商密钥全局唯一(按 product 查询),
/// 子商户仅需商户号(merchNo) + SAAS 终端号(pn), 见 [HkrtIsvChannelMerchant]。
///
/// 签名算法: MD5 大写(参数字母升序 + 末尾拼 accessKey), 无需证书。
/// 敏感字段(accessKey)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "hkrt_isv_key_config", autoResultMap = true)
public class HkrtIsvKeyConfig extends MpBaseEntity implements ToResult<HkrtIsvKeyConfigResult> {

    /// 产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 服务商编号(agent_no)
    private String agentNo;

    /// 接入机构标识(access_id)
    private String accessId;

    /// 签名密钥(access_key, MD5 大写签名, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String accessKey;

    /// 转换
    @Override
    public HkrtIsvKeyConfigResult toResult() {
        return HkrtIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
