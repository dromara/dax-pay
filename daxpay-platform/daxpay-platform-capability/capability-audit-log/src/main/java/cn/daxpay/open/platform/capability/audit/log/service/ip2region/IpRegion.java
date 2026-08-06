package cn.daxpay.open.platform.capability.audit.log.service.ip2region;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # IP对应地址区域信息
///
/// 官方 ip2region v4.xdb 数据格式: `国家|省份|城市|ISP|国家码(iso-alpha2)`, 五段定长。
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

    /// 归一化地区名: 去首尾空格, 去掉结尾一个"市"或"省"
    private static String normalizeName(String name) {
        if (name == null) {
            return "";
        }
        String result = name.trim();
        if (result.endsWith("市") || result.endsWith("省")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
