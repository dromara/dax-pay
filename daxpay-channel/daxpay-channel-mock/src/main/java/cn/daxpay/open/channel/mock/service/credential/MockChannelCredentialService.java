package cn.daxpay.open.channel.mock.service.credential;

import cn.daxpay.open.channel.mock.convert.credential.MockChannelCredentialConvert;
import cn.daxpay.open.channel.mock.dao.credential.MockChannelCredentialManager;
import cn.daxpay.open.channel.mock.entity.credential.MockChannelCredential;
import cn.daxpay.open.channel.mock.param.credential.MockChannelCredentialParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 模拟通道网关签名密钥配置服务
///
/// 全局单行配置(id 固定 1)的 get-or-init / 空值合并保存:
/// - 查询不存在则插空行(返回后由前端脱敏回显, 空行即「未配置」态);
/// - 保存时空值不覆盖(密钥留空保留原值, 见 [MockChannelCredentialConvert#copy]);
/// - 两字段(商户私钥/平台公钥)均非空才算已配置(见实体 [MockChannelCredential#toResult]),
///   装配链经 [#getRequiredConfigured] fail-fast。
///
/// 密钥与模拟网关侧成对一致, 口径见工作区 mock 网关协议契约文档。
/// 直连产品按商户级密钥装配([cn.daxpay.open.channel.mock.service.credential.MockDirectCredentialService]),
/// 服务商产品按服务商级密钥装配([cn.daxpay.open.channel.mock.service.credential.MockIsvCredentialService]),
/// 全局单行是其未配置时的回落兜底(也服务无通道商户号场景)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockChannelCredentialService {

    private final MockChannelCredentialManager mockChannelCredentialManager;

    /// 查询单行配置(不存在则初始化空行)
    @Transactional(rollbackFor = Exception.class)
    public MockChannelCredential getOrInit() {
        var existing = mockChannelCredentialManager.findSingleton();
        if (existing.isPresent()) {
            return existing.get();
        }
        // 单行表 id 固定 1(父类 @Setter 非链式, 单独赋值)
        var config = new MockChannelCredential();
        config.setId(MockChannelCredentialManager.SINGLETON_ID);
        try {
            mockChannelCredentialManager.save(config);
        } catch (DuplicateKeyException e) {
            // 并发首查双插兜底: 主键冲突说明他方已建行, 回查即可
            return mockChannelCredentialManager.findSingleton()
                    // Mock 通道密钥配置行初始化失败
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                            "error.channel.mock.credentialInitFailed"));
        }
        return config;
    }

    /// 保存密钥配置(空值不覆盖: 留空字段保留原值)
    @Transactional(rollbackFor = Exception.class)
    public void save(MockChannelCredentialParam param) {
        var config = this.getOrInit();
        MockChannelCredentialConvert.CONVERT.copy(param, config);
        mockChannelCredentialManager.updateById(config);
    }

    /// 查询已配置完整的网关密钥(服务商级未配置回落时的兜底, 两字段任一为空即 fail-fast)
    ///
    /// 服务商产品(mock_isv)在服务商级两件套不齐时回落本全局单行;
    /// 全局也未配置时支付链路直接报错, 避免把空密钥下发给子应用。
    public MockChannelCredential getRequiredConfigured() {
        var config = this.getOrInit();
        if (StrUtil.isBlank(config.getMerchantPrivateKey()) || StrUtil.isBlank(config.getPlatformPublicKey())) {
            // 模拟通道接入密钥未配置, 请先在运营端完成配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.globalCredentialNotConfigured");
        }
        return config;
    }
}
