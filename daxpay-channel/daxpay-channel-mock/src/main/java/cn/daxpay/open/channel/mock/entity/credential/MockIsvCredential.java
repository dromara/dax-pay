package cn.daxpay.open.channel.mock.entity.credential;

import cn.daxpay.open.channel.mock.convert.credential.MockIsvCredentialConvert;
import cn.daxpay.open.channel.mock.result.credential.MockIsvCredentialResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商接入密钥(服务商级独立密钥, save/get 配置范式)
///
/// 服务商产品(mock_isv)的网关接入配置, 每服务商一行(isv_no 唯一), 拟真服务商级独立密钥范式:
/// - 运营从模拟网关控制台复制材料粘贴维护(密钥在控制台生成, 本表不生成密钥);
/// - 商户私钥 + 平台公钥两件套 + 接入网关地址; 两段 PEM 留空保存保留原值;
/// - 装配链: isvNo 非空且两件套齐时整行下发(主体编号取服务商编号, 由 one 侧随请求报文/查询串
///   携带 key_owner_no, 模拟网关按主体编号定位登记公钥, gatewayBaseUrl 供 one 侧优先选址),
///   否则回落全局单行([MockChannelCredential]);
/// - 主体级密钥已下线, 按主体编号 key_owner_no 取钥(主体编号取服务商编号), 装配按「商户私钥 + 平台公钥」两件套。
/// 由 [cn.daxpay.open.channel.mock.controller.admin.MockIsvCredentialController] 运营端维护,
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_isv_credential", autoResultMap = true)
public class MockIsvCredential extends MpBaseEntity implements ToResult<MockIsvCredentialResult> {

    /// 服务商编号(指向 mock_isv_info.isv_no, 一服务商一行)
    private String isvNo;

    /// 商户私钥(PEM PKCS#8 多行原文, 加密存储; 服务商产品请求签名用, 留空保存保留原值)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 平台公钥(PEM SPKI 多行原文, 加密存储; 服务商产品网关响应/回调验签用, 留空保存保留原值)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    /// 服务商接入网关地址(可空; one 侧非空优先选址, 空回落 yml base-url)
    private String gatewayBaseUrl;

    /// 转换(脱敏视图, 见 [MockIsvCredentialResult])
    @Override
    public MockIsvCredentialResult toResult() {
        return MockIsvCredentialConvert.CONVERT.toResult(this);
    }
}
