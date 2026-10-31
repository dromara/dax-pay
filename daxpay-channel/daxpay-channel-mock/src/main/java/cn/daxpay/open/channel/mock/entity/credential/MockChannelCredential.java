package cn.daxpay.open.channel.mock.entity.credential;

import cn.daxpay.open.channel.mock.convert.credential.MockChannelCredentialConvert;
import cn.daxpay.open.channel.mock.result.credential.MockChannelCredentialResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟通道网关签名密钥配置
///
/// mock 通道与模拟网关侧成对一致的签名密钥对(全局单行, id 固定 1):
/// 商户私钥(请求签名) + 平台公钥(响应/回调验签)。
/// 参照支付宝直连应用密钥配置迁 DB, 运营端 Web 界面维护。
/// 密钥随凭证下发至 one 子应用 mock 模块(one 无状态), 由其签名请求/验签响应。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_channel_credential", autoResultMap = true)
public class MockChannelCredential extends MpBaseEntity implements ToResult<MockChannelCredentialResult> {

    /// 商户私钥(PEM PKCS#8 多行原文, 加密存储; 请求签名用, 与网关侧商户公钥成对)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 平台公钥(PEM SPKI 多行原文, 加密存储; 响应/回调验签用, 与网关侧平台私钥成对)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    /// 转换(附配置完整性标记: 两密钥均非空即已配置)
    @Override
    public MockChannelCredentialResult toResult() {
        MockChannelCredentialResult result = MockChannelCredentialConvert.CONVERT.toResult(this);
        boolean configured = StrUtil.isNotBlank(merchantPrivateKey) && StrUtil.isNotBlank(platformPublicKey);
        result.setConfigured(configured);
        return result;
    }
}
