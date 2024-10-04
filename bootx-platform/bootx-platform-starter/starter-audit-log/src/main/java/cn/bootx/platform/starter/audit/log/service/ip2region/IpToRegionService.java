package cn.bootx.platform.starter.audit.log.service.ip2region;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.starter.audit.log.properties.AuditLogProperties;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 *
 * @author xxm
 * @since 2023/4/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IpToRegionService {
    /** VectorIndex 缓存 */
    private static  byte[] XDB_INDEX;
    /** 整个 xdb 文件缓存 */
    private static  byte[] XDB_BUFF;
    /** 参数配置 */
    private final AuditLogProperties auditLogProperties;

    /**
     * 获取文件查询方式
     */
    private Searcher getSearcherByFile(){
        try {
            return Searcher.newWithFileOnly(auditLogProperties.getIp2region().getFilePath());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * VectorIndex 索引
     */
    private Searcher getSearcherByIndex(){
        try {
            String filePath = auditLogProperties.getIp2region()
                    .getFilePath();
            if (Objects.isNull(XDB_INDEX)) {
                XDB_INDEX = Searcher.loadVectorIndexFromFile(filePath);
            }
            return Searcher.newWithVectorIndex(filePath, XDB_INDEX);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 缓存整个 xdb 数据
     */
    private Searcher getSearcherByCache(){
        try {
            if (Objects.isNull(XDB_BUFF)) {
                String filePath = auditLogProperties.getIp2region()
                        .getFilePath();
                XDB_BUFF = Searcher.loadContentFromFile(filePath);
            }
            return Searcher.newWithBuffer(XDB_BUFF);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 根据IP获得地址信息
     */
    public IpRegion getRegionByIp(String ip){
        // 判断IP是否合法
        Matcher matcher = PatternPool.IPV4.matcher(ip);
        Matcher ipV6Matcher =PatternPool.IPV6.matcher(ip);
        if (!matcher.matches()) {
            if (ipV6Matcher.matches()){
                log.warn("IpV6地址: {}",ip);

            }else {
                log.warn("非法IPv4地址: {}",ip);

            }
            return null;
        }

        // 根据类型获取 Searcher 对象
        Searcher searcher = switch (auditLogProperties.getIp2region()
                .getSearchType()) {
            case FILE -> getSearcherByFile();
            case VECTOR_INDEX -> getSearcherByIndex();
            case CACHE -> getSearcherByCache();
        };
        // 无法进行查询
        if (Objects.isNull(searcher)){
            log.warn("");
            return null;
        }

        try {
            // 城市Id|国家|区域|省份|城市|ISP
            String search = searcher.search(ip);
            List<String> ipInfo = StrUtil.split(search, "|");
            return IpRegion.init(ipInfo);
        } catch (Exception e) {
            throw new BizException("IP查询失败");
        } finally {
            try {
                searcher.close();
            } catch (IOException e) {
                // 这句不会执行, finally中try无效
                log.error("关闭Ip地址库失败");
            }
        }
    }

    /**
     * 获取默认格式的地址文本
     */
    public String getRegionStrByIp(String ip){
        String location;
        // ip信息
        IpRegion region = this.getRegionByIp(ip);
        // 未查询到
        if (Objects.isNull(region)){
            return  "未知";
        }
        // 中国 港澳台
        if (region.isBigChina()){
            location = StrUtil.format("{}/{}/{}",region.getCountry(),region.getProvince(),region.getIsp());
        }
        // 中国 直辖市
        else if (region.isProvinceLevel()){
            location = StrUtil.format("{}/{}",region.getProvince(),region.getIsp());
        }
        // 普通中国城市
        else if (region.isChinaIp()){
            location = StrUtil.format("{}/{}/{}",region.getProvince(),region.getCity(),region.getIsp());
        }
        // 内网
        else if (region.isInnerIp()){
            location = "内网地址";
        }
        // 国外
        else {
            location = StrUtil.format("{}/{}",region.getCountry(),region.getIsp());
        }
        return location.replaceAll("/0","");
    }
}
