package cn.daxpay.open.platform.baseapi.service.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/// # 系统基础接口
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemBaseService {

    private final Environment env;

    /// 系统信息
    public Map<String, Object> info(){
        var buildProperties = SpringUtil.getBeansOfType(BuildProperties.class);
        // 只有jar运行时才可以获取
        Optional<BuildProperties> optional = Opt.ofNullable(new ArrayList<>(buildProperties.values()))
                .filter(CollUtil::isNotEmpty)
                .map(ArrayList::getFirst)
                .toOptional();
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
