package cn.daxpay.open.platform.iam.service.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.daxpay.open.platform.iam.convert.social.SocialConfigConvert;
import cn.daxpay.open.platform.iam.dao.social.SocialConfigManager;
import cn.daxpay.open.platform.iam.entity.social.SocialConfig;
import cn.daxpay.open.platform.iam.param.social.SocialConfigParam;
import cn.daxpay.open.platform.iam.result.social.SocialConfigResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 第三方平台登录配置服务
///
/// 配置页采用"枚举驱动 + 惰性初始化"模式: 平台清单来源于 [SocialSourceEnum] 枚举.
/// [findAll] 仅内存合并展示(不落库), 未配置平台返回无 id 的瞬态展示项;
/// [findBySource] 在平台记录不存在时按需初始化占位记录(`configured=false`), 用户保存配置后置 `configured=true`.
/// 数据访问全部委托 [SocialConfigManager], 本层不直接使用 lambdaQuery.
/// 配置表 source 全局唯一, 占位记录仅插入一次.
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialConfigService {

    private final SocialConfigManager socialConfigManager;

    /// 全量查询平台配置(枚举驱动, 内存合并, 不落库)
    /// 已配置平台返回库表记录, 未配置平台返回 `configured=false` 的瞬态展示项(无 id).
    /// 真正的初始化落库延迟到 [findBySource] 按需触发, 避免读操作产生写副作用.
    public List<SocialConfigResult> findAll() {
        // 库表已有配置按 source 索引
        Map<String, SocialConfig> configMap = socialConfigManager.listAll().stream()
            .collect(Collectors.toMap(SocialConfig::getSource, c -> c, (a, b) -> a));
        // 按枚举声明顺序合并
        List<SocialConfigResult> result = new ArrayList<>();
        Arrays.stream(SocialSourceEnum.values()).forEach(source -> {
            SocialConfig config = configMap.get(source.getCode());
            if (config != null) {
                result.add(config.toResult());
            } else {
                // 未配置平台构建瞬态展示项, 不落库(无 id)
                result.add(new SocialConfigResult()
                        .setSource(source.getCode())
                        .setConfigured(false)
                        .setEnabled(false));
            }
        });
        return result;
    }

    /// 根据平台编码查询, 记录不存在则按需初始化占位记录(configured=false)
    /// 前端点击未配置平台"配置"按钮时调用, 触发该平台的首次落库.
    public SocialConfigResult findBySource(String source) {
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (socialSource == null) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        SocialConfig config = socialConfigManager.findBySource(source)
            .orElseGet(() -> this.createDefaultConfig(source));
        return config.toResult();
    }

    /// 修改配置(按 source 查后更新, 保存即标记为已配置)
    /// clientSecret 采用"加密存储 + 脱敏返回 + 前端 diffForm"模式:
    /// 前端未修改时不传该字段(undefined), copy 后 entity.clientSecret 暂为 null,
    /// 默认 NOT_NULL 策略下 null 字段不参与 UPDATE, 数据库原值(密文)保持不变, 无需 Service 层兜底.
    public void update(SocialConfigParam param) {
        SocialConfig entity = socialConfigManager.findBySource(param.getSource())
            // 社交登录: 平台配置不存在
            .orElseThrow(() -> new OperationFailException("error.social.configNotExist"));
        SocialConfigConvert.CONVERT.copy(param, entity);
        // 保存配置即标记为已配置
        entity.setConfigured(true);
        socialConfigManager.updateById(entity);
    }

    /// 切换启用状态(按 source 查后更新, 仅已配置平台可启停)
    public void updateEnabled(String source, Boolean enabled) {
        SocialConfig entity = socialConfigManager.findBySource(source)
            // 社交登录: 平台配置不存在
            .orElseThrow(() -> new OperationFailException("error.social.configNotExist"));
        // 社交登录: 未配置平台不允许启停
        if (!entity.isConfigured()) {
            throw new OperationFailException("error.social.notConfigured");
        }
        entity.setEnabled(enabled);
        socialConfigManager.updateById(entity);
    }

    /// 根据平台来源查询已配置且启用的配置(供 SocialAuthRequestFactory 使用)
    public SocialConfig findEnabledBySource(String source) {
        return socialConfigManager.findEnabledBySource(source).orElse(null);
    }

    /// 查询所有已配置且启用的平台(登录页公开接口使用)
    /// 仅返回平台编码(source), 不暴露任何敏感字段(clientId/clientSecret/redirectUri/extra 等).
    public List<SocialEnabledPlatformResult> findEnabledList() {
        return socialConfigManager.findAllEnabled().stream()
            .map(c -> new SocialEnabledPlatformResult().setSource(c.getSource()))
            .toList();
    }

    /// 将配置实体转换为授权配置
    /// @param redirectUri 完整回调地址, 由调用方根据 mode 和 source 拼接(render 阶段传入, exchange 阶段可不传)
    public SocialAuthConfig buildAuthConfig(SocialConfig entity, String redirectUri) {
        // 企业微信 agentId 等平台特有参数从 extra(jsonb) 读取
        String agentId = null;
        if (StrUtil.isNotBlank(entity.getExtra())) {
            agentId = JSONUtil.parseObj(entity.getExtra()).getStr("agentId");
        }
        return new SocialAuthConfig()
            .setClientId(entity.getClientId())
            .setClientSecret(entity.getClientSecret())
            .setRedirectUri(redirectUri)
            .setAgentId(agentId);
    }

    /// 创建并保存占位记录(configured=false), 业务字段留空
    private SocialConfig createDefaultConfig(String source) {
        SocialConfig config = new SocialConfig()
            .setSource(source);
        socialConfigManager.save(config);
        return config;
    }
}
