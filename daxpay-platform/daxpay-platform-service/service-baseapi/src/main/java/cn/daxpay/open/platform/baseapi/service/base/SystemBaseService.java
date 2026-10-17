package cn.daxpay.open.platform.baseapi.service.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/// # 系统基础接口
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemBaseService {

    private final Environment env;

    /// 构建信息(仅 jar 运行时才存在的可选 bean, IDE 直跑时缺失, 经 [ObjectProvider] 可选注入)
    private final ObjectProvider<BuildProperties> buildPropertiesProvider;

    /// 系统信息
    public Map<String, Object> info(){
        // 只有jar运行时才可以获取
        Optional<BuildProperties> optional = Optional.ofNullable(buildPropertiesProvider.getIfAvailable());
        // 构建时间
        String buildTime = optional
                .map(BuildProperties::getTime)
                .map(o-> o.atZone(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .orElse("");
        // 项目版本
        String version = optional
                .map(BuildProperties::getVersion)
                .orElse("");
        // 项目名称
        String projectName = optional
                .map(BuildProperties::getName)
                .orElse("");;

        return Map.of(
                "buildTime", buildTime,
                "projectName", projectName,
                "version", version
        );
    }
}
