package cn.daxpay.open.demo.ipregion.controller;

import cn.daxpay.open.demo.ipregion.result.IpRegionDemoResult;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpRegion;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # IP归属地查询演示接口
///
/// 基于 ip2region 离线库（与审计日志 location 字段同源），演示 IP 归属地查询能力。
///
/// 鉴权：URL 前缀 `/demo/**` 已在白名单，类上叠加 `@IgnoreAuth` 双保险。
@IgnoreAuth
@Tag(name = "IP归属地查询演示")
@RestController
@RequestMapping("/demo/ip-region")
@RequiredArgsConstructor
public class IpRegionDemoController {

    private final IpToRegionService ipToRegionService;

    /// 查询指定 IP 的归属地
    @Operation(summary = "根据 IP 查询归属地")
    @GetMapping("/query")
    public Result<IpRegionDemoResult> query(
            @Parameter(description = "IP 地址（支持 IPv4 / IPv6）") @RequestParam("ip") String ip) {
        return Res.ok(buildResult(ip));
    }

    /// 自动获取当前请求者的 IP 并查询归属地
    @Operation(summary = "查询当前请求者 IP 归属地")
    @GetMapping("/current")
    public Result<IpRegionDemoResult> current(HttpServletRequest request) {
        // getClientIP 会自动解析 X-Forwarded-For 等代理头
        String ip = JakartaServletUtil.getClientIP(request);
        return Res.ok(buildResult(ip));
    }

    /// 组装查询结果：先取格式化文本，再补结构化字段
    private IpRegionDemoResult buildResult(String ip) {
        IpRegionDemoResult result = new IpRegionDemoResult().setIp(ip);
        if (StrUtil.isBlank(ip)) {
            // 未拿到 IP（如本地无网络栈），统一回“未知”
            return result.setRegionStr("未知");
        }
        // 格式化文本（与审计日志写入逻辑同源）
        result.setRegionStr(ipToRegionService.getRegionStrByIp(ip));
        // 结构化字段（查询失败时 region 为 null，结构化字段保持空）
        IpRegion region = ipToRegionService.getRegionByIp(ip);
        if (Objects.nonNull(region)) {
            result.setCountry(region.getCountry())
                    .setProvince(region.getProvince())
                    .setCity(region.getCity())
                    .setIsp(region.getIsp())
                    .setCountryCode(region.getCountryCode())
                    .setInnerIp(region.isInnerIp())
                    .setChinaIp(region.isChinaIp());
        }
        return result;
    }
}
