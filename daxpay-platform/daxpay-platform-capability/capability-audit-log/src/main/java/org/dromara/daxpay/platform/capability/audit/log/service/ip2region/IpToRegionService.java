package org.dromara.daxpay.platform.capability.audit.log.service.ip2region;

import org.dromara.daxpay.platform.common.config.properties.PlatformStarterProperties;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.IPv4;
import org.lionsoul.ip2region.xdb.LongByteArray;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/// # IP归属地查询服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IpToRegionService {

    /// VectorIndex 缓存
    private static byte[] XDB_INDEX;
    /// 整个 xdb 文件缓存
    private static LongByteArray XDB_BUFF;
    /// 参数配置
    private final PlatformStarterProperties platformStarterProperties;

    /// VectorIndex 索引
    private Searcher getSearcherByIndex() {
        try {
            String filePath = platformStarterProperties.getAuditLog().getIp2region().getFilePath();
            if (StrUtil.isBlank(filePath)) {
                log.warn("IP归属地查询: 文件路径未配置");
                return null;
            }
            if (Objects.isNull(XDB_INDEX)) {
                XDB_INDEX = Searcher.loadVectorIndexFromFile(filePath);
                log.info("IP归属地查询: VectorIndex 缓存加载成功");
            }
            return Searcher.newWithVectorIndex(new IPv4(), filePath, XDB_INDEX);
        } catch (Exception e) {
            log.warn("IP归属地查询: 创建VectorIndex Searcher失败, error={}", e.getMessage());
            return null;
        }
    }

    /// 缓存整个 xdb 数据
    private Searcher getSearcherByCache() {
        try {
            String filePath = platformStarterProperties.getAuditLog().getIp2region().getFilePath();
            if (StrUtil.isBlank(filePath)) {
                log.warn("IP归属地查询: 文件路径未配置");
                return null;
            }
            if (Objects.isNull(XDB_BUFF)) {
                XDB_BUFF = Searcher.loadContentFromFile(filePath);
                log.info("IP归属地查询: xdb数据缓存加载成功");
            }
            return Searcher.newWithBuffer(new IPv4(), XDB_BUFF);
        } catch (Exception e) {
            log.warn("IP归属地查询: 创建缓存Searcher失败, error={}", e.getMessage());
            return null;
        }
    }

    /// 根据IP获得地址信息
    /// 查询失败时返回 null，不抛出异常，保证主流程继续
    public IpRegion getRegionByIp(String ip) {
        // 判断IP是否合法
        if (StrUtil.isBlank(ip)) {
            log.debug("IP归属地查询: IP为空");
            return null;
        }

        Matcher matcher = PatternPool.IPV4.matcher(ip);
        Matcher ipV6Matcher = PatternPool.IPV6.matcher(ip);
        if (!matcher.matches()) {
            if (ipV6Matcher.matches()) {
                log.debug("IP归属地查询: IPv6地址跳过, ip={}", ip);
            } else {
                log.debug("IP归属地查询: 非法IPv4地址, ip={}", ip);
            }
            return null;
        }

        // 根据类型获取 Searcher 对象
        Searcher searcher = switch (platformStarterProperties.getAuditLog().getIp2region().getSearchType()) {
            case VECTOR_INDEX -> getSearcherByIndex();
            case CACHE -> getSearcherByCache();
        };

        // 无法进行查询
        if (Objects.isNull(searcher)) {
            log.warn("IP归属地查询: Searcher创建失败，无法进行查询, ip={}", ip);
            return null;
        }

        try {
            // 城市Id|国家|区域|省份|城市|ISP
            String search = searcher.search(ip);
            List<String> ipInfo = StrUtil.split(search, "|");
            return IpRegion.init(ipInfo);
        } catch (Exception e) {
            // 查询异常不抛出不阻塞主流程，仅记录日志
            log.warn("IP归属地查询异常: ip={}, error={}", ip, e.getMessage());
            return null;
        } finally {
            // Searcher 使用完后尝试关闭（部分实现可能不支持）
            try {
                searcher.close();
            } catch (Exception closeEx) {
                log.trace("IP归属地查询: Searcher关闭异常, error={}", closeEx.getMessage());
            }
        }
    }

    /// 获取默认格式的地址文本
    public String getRegionStrByIp(String ip) {
        // ip信息
        IpRegion region = this.getRegionByIp(ip);
        // 未查询到
        if (Objects.isNull(region)) {
            return "未知";
        }
        // 中国 港澳台
        if (region.isBigChina()) {
            return StrUtil.format("{}/{}/{}", region.getCountry(), region.getProvince(), region.getIsp());
        }
        // 中国 直辖市
        else if (region.isProvinceLevel()) {
            return StrUtil.format("{}/{}", region.getProvince(), region.getIsp());
        }
        // 普通中国城市
        else if (region.isChinaIp()) {
            return StrUtil.format("{}/{}/{}", region.getProvince(), region.getCity(), region.getIsp());
        }
        // 内网
        else if (region.isInnerIp()) {
            return "内网地址";
        }
        // 国外
        else {
            return StrUtil.format("{}/{}", region.getCountry(), region.getIsp());
        }
    }
}

