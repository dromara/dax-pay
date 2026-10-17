package cn.daxpay.open.channel.duolabao.entity.direct;

import cn.daxpay.open.channel.duolabao.convert.direct.DuolabaoDirectKeyConfigConvert;
import cn.daxpay.open.channel.duolabao.result.direct.DuolabaoDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 哆啦宝直连配置
///
/// 直连商户维度的通道配置: 商户编号(customerNum)/店铺编号(shopNum)/代理商编号(agentNum)
/// 创建时录入, accessKey/secretKey 密钥对(开放平台 API 凭证, 敏感字段加密存储)由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "duolabao_direct_key_config", autoResultMap = true)
public class DuolabaoDirectKeyConfig extends MchBaseEntity implements ToResult<DuolabaoDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 商户编号(customerNum, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String customerNum;

    /// 店铺编号(shopNum, 可空, 创建时录入)
    private String shopNum;

    /// 代理商编号(agentNum, 可空, 非代理商不需要填写)
    private String agentNum;

    /// 公钥(accessKey, Header 上送的身份标识, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String accessKey;

    /// 私钥(secretKey, token 签名与回调验签用, 不上送, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secretKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public DuolabaoDirectKeyConfigResult toResult() {
        return DuolabaoDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
