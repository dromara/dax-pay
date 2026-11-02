package cn.daxpay.open.channel.mock.service.credential;

import cn.daxpay.open.channel.mock.convert.credential.MockIsvCredentialConvert;
import cn.daxpay.open.channel.mock.dao.credential.MockIsvCredentialManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvInfoManager;
import cn.daxpay.open.channel.mock.entity.credential.MockIsvCredential;
import cn.daxpay.open.channel.mock.param.credential.MockIsvCredentialParam;
import cn.daxpay.open.channel.mock.result.credential.MockIsvCredentialResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 模拟服务商接入密钥服务(服务商级独立密钥, save/get 配置范式)
///
/// 服务商产品(mock_isv)的网关接入配置维护, 每服务商一行, 拟真服务商级独立密钥范式:
/// - 密钥在模拟网关控制台生成, 运营复制材料粘贴进来([#save] 按 isvNo upsert,
///   两段 PEM(商户私钥/平台公钥)各自独立维护——留空即保留原值, 可只换其中一段,
///   语义对齐 [MockDirectCredentialService#save]);
/// - [#getMasked]: 脱敏视图, configured=两段 PEM 均非空(装配可用), 未保存过仅 configured=false;
/// - [#findUsable]: 装配链分流入口([cn.daxpay.open.channel.mock.service.MockCredentialSupport]),
///   isvNo 非空且两段 PEM 齐时返回整行(私钥/公钥/网关地址, 主体编号由装配侧取服务商编号填充),
///   否则 null 回落全局单行密钥。
///
/// 不做本侧生成(无 generate)与明文出口(无 plain): 运营端工作台是接入方纯配置表单。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvCredentialService {

    private final MockIsvCredentialManager mockIsvCredentialManager;
    private final MockIsvInfoManager mockIsvInfoManager;

    /// 保存接入密钥配置(按 isvNo upsert: 无行插入, 有行覆盖; 两段 PEM 留空保留原值)
    ///
    /// 两段 PEM 各自独立: 留空即不修改, 允许只换其中一段。商户私钥与平台公钥方向不同
    /// (前者供网关侧按商户公钥验签, 后者供我方验网关签名), 无成对依赖, 故不做齐交约束;
    /// 仅换私钥时需运营自行确认与网关侧当前密钥一致(前端二次确认提示, 不在本侧阻断)。
    @Transactional(rollbackFor = Exception.class)
    public void save(MockIsvCredentialParam param) {
        String isvNo = param.getIsvNo();
        if (StrUtil.isBlank(isvNo)) {
            // 服务商编号不可为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.isvNo.notBlank");
        }
        // 服务商主数据必须存在(密钥行强归属服务商)
        mockIsvInfoManager.findByIsvNo(isvNo)
                // mock: 模拟服务商不存在
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.mock.isvNotExist", isvNo));
        MockIsvCredential existing = mockIsvCredentialManager.findByIsvNo(isvNo);
        MockIsvCredential entity = existing != null ? existing : new MockIsvCredential();
        entity.setIsvNo(isvNo);
        MockIsvCredentialConvert.CONVERT.copy(param, entity);
        if (existing != null) {
            mockIsvCredentialManager.updateById(entity);
        } else {
            try {
                mockIsvCredentialManager.save(entity);
            } catch (DuplicateKeyException e) {
                // 并发双插兜底: 唯一索引冲突说明他方已建行, 回查转覆盖更新
                MockIsvCredential concurrent = mockIsvCredentialManager.findByIsvNo(isvNo);
                if (concurrent == null) {
                    // 模拟服务商接入密钥保存失败
                    throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                            "error.channel.mock.isvCredentialSaveFailed", isvNo);
                }
                // 父类 @Setter 非链式, 主键/乐观锁单独赋值
                entity.setId(concurrent.getId());
                entity.setVersion(concurrent.getVersion());
                mockIsvCredentialManager.updateById(entity);
            }
        }
    }

    /// 查询脱敏视图(未保存过时仅 configured=false, 其余字段为空)
    public MockIsvCredentialResult getMasked(String isvNo) {
        MockIsvCredential entity = this.findByIsvNoOrNull(isvNo);
        if (entity == null) {
            return new MockIsvCredentialResult().setIsvNo(isvNo).setConfigured(false);
        }
        MockIsvCredentialResult result = entity.toResult();
        result.setConfigured(this.isConfigured(entity));
        if (result.getUpdateTime() == null) {
            // 插入当刻 lastModifiedTime 尚未回填时回落创建时间
            result.setUpdateTime(entity.getCreateTime());
        }
        return result;
    }

    /// 装配链分流入口: 取「两件套齐」的可整行下发的接入配置(isvNo 空白/无行/任一 PEM 为空返回 null)
    public MockIsvCredential findUsable(String isvNo) {
        MockIsvCredential entity = this.findByIsvNoOrNull(isvNo);
        if (entity == null || !this.isConfigured(entity)) {
            return null;
        }
        return entity;
    }

    /// 已配置口径: 商户私钥/平台公钥均非空(脱敏视图与装配分流共用, 防口径漂移; 主体级密钥已下线, 取钥按主体编号 key_owner_no)
    private boolean isConfigured(MockIsvCredential entity) {
        return StrUtil.isNotBlank(entity.getMerchantPrivateKey())
                && StrUtil.isNotBlank(entity.getPlatformPublicKey());
    }

    /// 按服务商编号查询(无行返回 null, isvNo 空白不查库)
    private MockIsvCredential findByIsvNoOrNull(String isvNo) {
        if (StrUtil.isBlank(isvNo)) {
            return null;
        }
        return mockIsvCredentialManager.findByIsvNo(isvNo);
    }
}
