package cn.daxpay.open.channel.mock.entity.credential;

import cn.daxpay.open.channel.mock.convert.credential.MockDirectCredentialConvert;
import cn.daxpay.open.channel.mock.result.credential.MockDirectCredentialResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟通道直连商户接入密钥(商户级拟真凭证)
///
/// 直连产品(mock_pay)的商户级独立接入配置, 每通道商户一行(channelMchNo 唯一),
/// 拟真真实通道「每商户一套密钥」形态:
/// - 密钥在模拟网关控制台按商户生成, 运营复制材料粘贴维护(本表不生成密钥);
/// - 商户私钥 + 平台公钥两件套均非空才算已配置(接入网关地址可空), 两段 PEM 留空保存保留原值;
/// - 装配链: 两件套齐时整行下发(主体编号取通道商户号, 由 one 侧随请求报文/查询串携带 key_owner_no,
///   模拟网关按主体编号定位登记公钥, gatewayBaseUrl 供 one 侧优先选址), 未配置直接报错;
/// - 主体级密钥已下线, 按主体编号 key_owner_no 取钥(主体编号取通道商户号), 装配按「商户私钥 + 平台公钥」两件套。
/// 由 [cn.daxpay.open.channel.mock.controller.admin.MockDirectCredentialController] 运营端维护,
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_pay_credential", autoResultMap = true)
public class MockPayCredential extends MpBaseEntity implements ToResult<MockDirectCredentialResult> {

    /// 通道商户号(通用通道商户主表 channel_mch_no, 一通道商户一行)
    private String channelMchNo;

    /// 商户私钥(PEM PKCS#8 多行原文, 加密存储; 直连产品请求签名用, 留空保存保留原值)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 平台公钥(PEM SPKI 多行原文, 加密存储; 直连产品网关响应/回调验签用, 留空保存保留原值)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    /// 商户接入网关地址(可空; one 侧非空优先选址, 空回落 yml base-url)
    private String gatewayBaseUrl;

    /// 转换(脱敏视图, 见 [MockDirectCredentialResult])
    @Override
    public MockDirectCredentialResult toResult() {
        return MockDirectCredentialConvert.CONVERT.toResult(this);
    }
}
