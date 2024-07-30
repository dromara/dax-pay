package cn.bootx.platform.starter.audit.log.service.ip2region;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * IP对应地址区域信息
 * @author xxm
 * @since 2023/4/13
 */
@Data
@Accessors(chain = true)
public class IpRegion {
    private static final List<String> BIG_CHINA = List.of("香港","澳门","台湾");
    private static final List<String> PROVINCE_LEVEL_CITY = List.of("北京","上海","重庆","天津");

    /** 国家 */
    private String country;

    /** 区域 */
    private String region;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** ISP */
    private String isp;


    /** 是否内网地址 */
    public boolean isInnerIp(){
        return "内网IP".equals(isp);
    }

    /** 是否国内地址 */
    public boolean isChinaIp(){
        return "中国".equals(country);
    }

    /** 是否国内直辖市 */
    public boolean isProvinceLevel(){
        return "中国".equals(country)&&
                PROVINCE_LEVEL_CITY.contains(province);
    }

    /** 是否港澳台 */
    public boolean isBigChina(){
        return "中国".equals(country)&&
                BIG_CHINA.contains(province);
    }

    /**
     * 国家|区域|省份|城市|ISP
     */
    public static IpRegion init(List<String> ipInfo){
        IpRegion ipRegion = new IpRegion();
        if (CollUtil.isEmpty(ipInfo)){
            return ipRegion;
        }
        ipRegion.country = ipInfo.get(0);
        ipRegion.region = ipInfo.get(1);
        ipRegion.province = ipInfo.get(2);
        ipRegion.city = ipInfo.get(3);
        ipRegion.isp = ipInfo.get(4);

        return ipRegion;

    }
}
