package cn.daxpay.open.platform.capability.audit.log.service.ip2region;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # IP对应地址区域信息
///
/// 官方 ip2region v4.xdb 数据格式: `国家|省份|城市|ISP|国家码(iso-alpha2)`, 五段定长。
///
/// v4 名称格式规律(实证): 普通省/市返回全称(广东省/深圳市); 直辖市与自治区返回短名(北京/内蒙古);
/// 港澳台返回全称(香港特别行政区/台湾省); 无归属地返回 "0"。
@Data
@Accessors(chain = true)
public class IpRegion {
    private static final List<String> BIG_CHINA = List.of("香港","澳门","台湾");
    private static final List<String> PROVINCE_LEVEL_CITY = List.of("北京","上海","重庆","天津");

    /// 国家
    private String country;

    /// 省份
    private String province;

    /// 城市
    private String city;

    /// ISP 运营商
    private String isp;

    /// 国家码(iso-alpha2, v4 末尾段, 如 CN/HK/US)
    private String countryCode;

    /// 是否内网地址
    public boolean isInnerIp(){
        return "内网IP".equals(isp);
    }

    /// 是否国内地址
    public boolean isChinaIp(){
        return "中国".equals(country);
    }

    /// 是否国内直辖市
    ///
    /// v4 数据省名可能带"市"后缀(如"北京市"), 归一化后比对, 与 GeoFenceUtil.normalizeRegionName 口径一致
    public boolean isProvinceLevel(){
        return "中国".equals(country)&&
                PROVINCE_LEVEL_CITY.contains(normalizeName(province));
    }

    /// 是否港澳台
    public boolean isBigChina(){
        return "中国".equals(country)&&
                BIG_CHINA.contains(normalizeName(province));
    }

    /// 官方 v4.xdb 格式: 国家|省份|城市|ISP|国家码
    ///
    /// 防御性取值: 记录段数可能不足(国外/内网等短记录), 逐段判空, 不足不抛异常
    public static IpRegion init(List<String> ipInfo){
        IpRegion ipRegion = new IpRegion();
        if (CollUtil.isEmpty(ipInfo)){
            return ipRegion;
        }
        ipRegion.country = safeGet(ipInfo, 0);
        ipRegion.province = safeGet(ipInfo, 1);
        ipRegion.city = safeGet(ipInfo, 2);
        ipRegion.isp = safeGet(ipInfo, 3);
        ipRegion.countryCode = safeGet(ipInfo, 4);

        return ipRegion;

    }

    /// 安全取值: 越界返回 null 而非抛异常
    private static String safeGet(List<String> ipInfo, int index) {
        return index < ipInfo.size() ? ipInfo.get(index) : null;
    }

    /// 归一化地区名: 去首尾空格, 迭代去掉行政区划常见后缀
    ///
    /// 后缀由长到短迭代去除, 覆盖 省/市/自治区/特别行政区 等, 与 GeoFenceUtil.normalizeRegionName 口径一致。
    /// 例: "北京市"→"北京", "内蒙古自治区"→"内蒙古", "香港特别行政区"→"香港"; 自治州/盟保留全名(已知限制)。
    private static String normalizeName(String name) {
        if (name == null) {
            return "";
        }
        String result = name.trim();
        // 后缀由长到短, 保证 "新疆维吾尔自治区"→"新疆"、"香港特别行政区"→"香港"
        String[] suffixes = {"特别行政区", "维吾尔自治区", "回族自治区", "壮族自治区", "自治区", "省", "市"};
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String suffix : suffixes) {
                if (result.endsWith(suffix)) {
                    result = result.substring(0, result.length() - suffix.length());
                    changed = true;
                    break;
                }
            }
        }
        return result;
    }
}
