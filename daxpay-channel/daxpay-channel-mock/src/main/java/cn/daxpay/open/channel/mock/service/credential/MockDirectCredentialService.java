package cn.daxpay.open.channel.mock.service.credential;

import cn.daxpay.open.channel.mock.convert.credential.MockDirectCredentialConvert;
import cn.daxpay.open.channel.mock.dao.credential.MockDirectCredentialManager;
import cn.daxpay.open.channel.mock.entity.credential.MockDirectCredential;
import cn.daxpay.open.channel.mock.param.credential.MockDirectCredentialParam;
import cn.daxpay.open.channel.mock.result.credential.MockDirectCredentialResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 模拟通道直连商户接入密钥服务(商户级拟真凭证, save/find 配置范式)
///
/// 直连产品(mock_pay)的商户级网关接入配置维护, 每通道商户一行(channelMchNo 定位 upsert):
/// - [#save]: channelMchNo 必填; 两段 PEM(商户私钥/平台公钥)各自独立维护——留空即保留原值,
///   可只换其中一段, 网关地址明文可单独改(两段方向不同, 商户私钥供网关侧验签, 平台公钥供我方验签);
/// - [#getMasked]: 脱敏视图, configured=两段 PEM 均非空(装配可用), 未保存过仅 configured=false;
/// - [#findUsable]: 装配链分流入口([cn.daxpay.open.channel.mock.service.MockCredentialSupport]),
///   两段 PEM 齐时返回整行(私钥/公钥/网关地址, 主体编号由装配侧取通道商户号填充), 否则 null。
///
/// 不做本侧生成(无 generate)与明文出口(无 plain): 运营端工作台是接入方纯配置表单;
/// channelMchNo 真实性由调用方(通道商户管理页)保证, 本服务不反查通道商户主数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockDirectCredentialService {

    private final MockDirectCredentialManager mockDirectCredentialManager;

    /// 保存直连商户接入密钥配置(按 channelMchNo upsert: 无行插入, 有行覆盖; 网关地址可单独维护)
    ///
    /// 两段 PEM 各自独立: 留空即不修改, 允许只换其中一段。商户私钥与平台公钥方向不同
    /// (前者供网关侧按商户公钥验签, 后者供我方验网关签名), 无成对依赖, 故不做齐交约束;
    /// 仅换私钥时需运营自行确认与网关侧当前密钥一致(前端二次确认提示, 不在本侧阻断)。
    @Transactional(rollbackFor = Exception.class)
    public void save(MockDirectCredentialParam param) {
        String channelMchNo = param.getChannelMchNo();
        if (StrUtil.isBlank(channelMchNo)) {
            // 通道商户号不可为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.channelMerchantNo.notBlank");
        }
        MockDirectCredential existing = mockDirectCredentialManager.findByChannelMchNo(channelMchNo);
        MockDirectCredential entity = existing != null ? existing : new MockDirectCredential();
        entity.setChannelMchNo(channelMchNo);
        MockDirectCredentialConvert.CONVERT.copy(param, entity);
        if (existing != null) {
            mockDirectCredentialManager.updateById(entity);
        } else {
            try {
                mockDirectCredentialManager.save(entity);
            } catch (DuplicateKeyException e) {
                // 并发双插兜底: 唯一索引冲突说明他方已建行, 回查转覆盖更新
                MockDirectCredential concurrent = mockDirectCredentialManager.findByChannelMchNo(channelMchNo);
                if (concurrent == null) {
                    // 直连商户接入密钥保存失败
                    throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                            "error.channel.mock.credentialSaveFailed", channelMchNo);
                }
                // 父类 @Setter 非链式, 主键/乐观锁单独赋值
                entity.setId(concurrent.getId());
                entity.setVersion(concurrent.getVersion());
                mockDirectCredentialManager.updateById(entity);
            }
        }
    }

    /// 查询脱敏视图(未保存过时仅 configured=false, 其余字段为空)
    public MockDirectCredentialResult getMasked(String channelMchNo) {
        MockDirectCredential entity = this.findByChannelMchNoOrNull(channelMchNo);
        if (entity == null) {
            return new MockDirectCredentialResult().setChannelMchNo(channelMchNo).setConfigured(false);
        }
        MockDirectCredentialResult result = entity.toResult();
        result.setConfigured(this.isConfigured(entity));
        if (result.getUpdateTime() == null) {
            // 插入当刻 lastModifiedTime 尚未回填时回落创建时间
            result.setUpdateTime(entity.getCreateTime());
        }
        return result;
    }

    /// 装配链入口: 取「两件套齐」可整行下发的商户级配置(channelMchNo 空白/无行/任一 PEM 为空返回 null)
    public MockDirectCredential findUsable(String channelMchNo) {
        MockDirectCredential entity = this.findByChannelMchNoOrNull(channelMchNo);
        if (entity == null || !this.isConfigured(entity)) {
            return null;
        }
        return entity;
    }

    /// 已配置口径: 商户私钥/平台公钥均非空(脱敏视图与装配分流共用, 防口径漂移; 主体级密钥已下线, 取钥按主体编号 key_owner_no)
    private boolean isConfigured(MockDirectCredential entity) {
        return StrUtil.isNotBlank(entity.getMerchantPrivateKey())
                && StrUtil.isNotBlank(entity.getPlatformPublicKey());
    }

    /// 按通道商户号查询(无行返回 null, channelMchNo 空白不查库)
    private MockDirectCredential findByChannelMchNoOrNull(String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            return null;
        }
        return mockDirectCredentialManager.findByChannelMchNo(channelMchNo);
    }
}
