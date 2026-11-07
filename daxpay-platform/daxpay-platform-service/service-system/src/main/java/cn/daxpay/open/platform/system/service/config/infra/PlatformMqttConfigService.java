package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.system.convert.config.infra.PlatformMqttConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMqttConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformMqttConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformMqttConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台 MQTT 接入配置服务
///
/// 管理设备/终端 MQTT 接入地址与账号(纯 DB 存储、明文回显, 不读 yml);
/// 设备(虚拟设备收款播报 APP)激活时经 [#getMqttConfig] 取当前配置随凭证下发,
/// 修改即时生效(仅影响新激活设备)。
/// 数据源正典: 配置类型 [PlatformConfigTypeEnum#DEVICE_MQTT](`device_mqtt`)。
/// 本服务无缓存(每次直查库), 运维面板直接改库后即时生效。
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformMqttConfigService {

    /// 凭据版本长度(摘要前 12 位, 48 bit, 碰撞概率可忽略)
    private static final int CREDENTIAL_VERSION_LENGTH = 12;

    private final SystemPlatformConfigService systemConfigService;

    /// 获取 MQTT 接入配置(不存在时创建默认配置)
    public PlatformMqttConfig getMqttConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.DEVICE_MQTT,
                PlatformMqttConfig.class,
                new PlatformMqttConfig());
    }

    /// 获取 MQTT 接入配置
    public PlatformMqttConfigResult findMqttConfig() {
        return PlatformMqttConfigConvert.CONVERT.toMqttResult(this.getMqttConfig());
    }

    /// 更新 MQTT 接入配置
    public void updateMqttConfig(PlatformMqttConfigParam param) {
        PlatformMqttConfig data = this.getMqttConfig();
        PlatformMqttConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.DEVICE_MQTT, data);
    }

    /// 当前凭据版本(账号名与密码的摘要前 12 位)
    ///
    /// 终端激活时取一份持久化, 后续心跳上报本地值; 与当前值不一致即说明凭据已被轮换
    /// (改名会删除旧账号, 同样必须重新激活), 后端据此下发 needReactivate。
    public String currentCredentialVersion() {
        return credentialVersion(this.getMqttConfig());
    }

    /// 按配置计算凭据版本(账号名或密码任一变化, 版本即变化)
    public static String credentialVersion(PlatformMqttConfig config) {
        return credentialVersion(config.getMqttUsername(), config.getMqttPassword());
    }

    /// 按账号名与密码计算凭据版本
    ///
    /// 采用派生值而非独立存储列: 运维面板直接改写数据库时无需额外维护版本字段,
    /// 也不会出现"密码已换、版本未加"的错位。
    public static String credentialVersion(String username, String password) {
        String raw = StrUtil.nullToEmpty(username) + ":" + StrUtil.nullToEmpty(password);
        return DigestUtil.sha256Hex(raw).substring(0, CREDENTIAL_VERSION_LENGTH);
    }
}
