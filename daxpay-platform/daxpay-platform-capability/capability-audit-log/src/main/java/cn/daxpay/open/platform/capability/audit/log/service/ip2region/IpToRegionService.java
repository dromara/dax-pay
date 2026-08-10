package cn.daxpay.open.platform.capability.audit.log.service.ip2region;

import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.IPv4;
import org.lionsoul.ip2region.xdb.IPv6;
import org.lionsoul.ip2region.xdb.LongByteArray;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/// # IP归属地查询服务
///
/// 同时支持 IPv4 与 IPv6 查询。v4/v6 各自独立的 xdb 数据文件与 Searcher 缓存,
/// 根据 IP 格式分发到对应版本。v4/v6 xdb 字段格式完全一致（国家|省份|城市|ISP|国家码）,
/// 解析结果统一由 [IpRegion] 承载。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IpToRegionService {

    // ---- IPv4 缓存 ----
    /// IPv4 VectorIndex 缓存
    private static byte[] XDB_V4_INDEX;
    /// IPv4 整个 xdb 文件缓存
    private static LongByteArray XDB_V4_BUFF;

    // ---- IPv6 缓存 ----
    /// IPv6 VectorIndex 缓存
    private static byte[] XDB_V6_INDEX;
    /// IPv6 整个 xdb 文件缓存
    private static LongByteArray XDB_V6_BUFF;

    /// 参数配置
    private final PlatformStarterProperties platformStarterProperties;

    // ==================== IPv4 Searcher ====================

    /// IPv4 VectorIndex 索引
    private Searcher getV4SearcherByIndex() {
        String filePath = getV4FilePath();
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        try {
            if (Objects.isNull(XDB_V4_INDEX)) {
                XDB_V4_INDEX = Searcher.loadVectorIndexFromFile(filePath);
                log.info("IP归属地查询: IPv4 VectorIndex 缓存加载成功");
            }
            return Searcher.newWithVectorIndex(new IPv4(), filePath, XDB_V4_INDEX);
        } catch (Exception e) {
            log.debug("IP归属地查询: 创建IPv4 VectorIndex Searcher失败, error={}", e.getMessage());
            return null;
        }
    }

    /// IPv4 缓存整个 xdb 数据
    private Searcher getV4SearcherByCache() {
        String filePath = getV4FilePath();
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        try {
            if (Objects.isNull(XDB_V4_BUFF)) {
                XDB_V4_BUFF = Searcher.loadContentFromFile(filePath);
                log.info("IP归属地查询: IPv4 xdb数据缓存加载成功");
            }
            return Searcher.newWithBuffer(new IPv4(), XDB_V4_BUFF);
        } catch (Exception e) {
            log.debug("IP归属地查询: 创建IPv4缓存Searcher失败, error={}", e.getMessage());
            return null;
        }
    }

    // ==================== IPv6 Searcher ====================

    /// IPv6 VectorIndex 索引
    private Searcher getV6SearcherByIndex() {
        String filePath = getV6FilePath();
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        try {
            if (Objects.isNull(XDB_V6_INDEX)) {
                XDB_V6_INDEX = Searcher.loadVectorIndexFromFile(filePath);
                log.info("IP归属地查询: IPv6 VectorIndex 缓存加载成功");
            }
            return Searcher.newWithVectorIndex(new IPv6(), filePath, XDB_V6_INDEX);
        } catch (Exception e) {
            log.debug("IP归属地查询: 创建IPv6 VectorIndex Searcher失败, error={}", e.getMessage());
            return null;
        }
    }

    /// IPv6 缓存整个 xdb 数据
    private Searcher getV6SearcherByCache() {
        String filePath = getV6FilePath();
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        try {
            if (Objects.isNull(XDB_V6_BUFF)) {
                XDB_V6_BUFF = Searcher.loadContentFromFile(filePath);
                log.info("IP归属地查询: IPv6 xdb数据缓存加载成功");
            }
            return Searcher.newWithBuffer(new IPv6(), XDB_V6_BUFF);
        } catch (Exception e) {
            log.debug("IP归属地查询: 创建IPv6缓存Searcher失败, error={}", e.getMessage());
            return null;
        }
    }

    // ==================== 查询入口 ====================

    /// 根据IP获得地址信息
    /// 查询失败时返回 null，不抛出异常，保证主流程继续。
    /// IPv6 文件未配置或加载失败时静默返回 null（与旧版 IPv6 跳过行为一致）。
    public IpRegion getRegionByIp(String ip) {
        // 判断IP是否合法
        if (StrUtil.isBlank(ip)) {
            log.debug("IP归属地查询: IP为空");
            return null;
        }

        // 按 IP 格式分发到对应版本 Searcher
        Matcher ipv4Matcher = PatternPool.IPV4.matcher(ip);
        Matcher ipv6Matcher = PatternPool.IPV6.matcher(ip);
        boolean isV4 = ipv4Matcher.matches();
        boolean isV6 = ipv6Matcher.matches();
        if (!isV4 && !isV6) {
            log.debug("IP归属地查询: 非法IP地址, ip={}", ip);
            return null;
        }

        // 根据类型获取对应版本的 Searcher 对象
        Searcher searcher;
        String label;
        if (isV4) {
            label = "IPv4";
            searcher = switch (platformStarterProperties.getAuditLog().getIp2region().getSearchType()) {
                case VECTOR_INDEX -> getV4SearcherByIndex();
                case CACHE -> getV4SearcherByCache();
            };
        } else {
            label = "IPv6";
            searcher = switch (platformStarterProperties.getAuditLog().getIp2region().getSearchType()) {
                case VECTOR_INDEX -> getV6SearcherByIndex();
                case CACHE -> getV6SearcherByCache();
            };
        }

        // 无法进行查询（文件未配置或加载失败）
        if (Objects.isNull(searcher)) {
            log.debug("IP归属地查询: {} Searcher创建失败，无法进行查询, ip={}", label, ip);
            return null;
        }

        try {
            // xdb 定位信息字段格式由数据版本决定（开源版5段 / 商业版16-18段）
            String search = searcher.search(ip);
            List<String> ipInfo = StrUtil.split(search, "|");
            // v4/v6 可各自配置不同数据版本档次
            var ip2regionConfig = platformStarterProperties.getAuditLog().getIp2region();
            var dataVersion = isV4 ? ip2regionConfig.getDataVersion() : ip2regionConfig.getIpv6DataVersion();
            return IpRegion.init(ipInfo, dataVersion);
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
            // 未知归属地
            return I18nUtil.get("log.region.unknown");
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
            // 内网地址
            return I18nUtil.get("log.region.innerIp");
        }
        // 国外
        else {
            return StrUtil.format("{}/{}", region.getCountry(), region.getIsp());
        }
    }

    /// 获取 IPv4 xdb 文件路径
    private String getV4FilePath() {
        return platformStarterProperties.getAuditLog().getIp2region().getFilePath();
    }

    /// 获取 IPv6 xdb 文件路径
    private String getV6FilePath() {
        return platformStarterProperties.getAuditLog().getIp2region().getIpv6FilePath();
    }
}
