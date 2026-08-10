package cn.daxpay.open.platform.capability.audit.log.service.ip2region;

import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties.AuditLog.Ip2regionDataVersion;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # IpRegion 解析单元测试
///
/// 锁定官方 ip2region v4.xdb 五段格式 `国家|省份|城市|ISP|国家码` 的字段映射,
/// 防止再次退化为旧版 v2/v3 的 `国家|区域|省份|城市|ISP` 错位解析。
class IpRegionTest {

    @Test
    @DisplayName("v4 标准五段记录: 字段映射正确")
    void init_standardV4Record_shouldMapFields() {
        // 国家|省份|城市|ISP|国家码
        IpRegion region = IpRegion.init(List.of("中国", "山东省", "济南市", "电信", "CN"));

        assertEquals("中国", region.getCountry());
        assertEquals("山东省", region.getProvince());
        assertEquals("济南市", region.getCity());
        assertEquals("电信", region.getIsp());
        assertEquals("CN", region.getCountryCode());
    }

    @Test
    @DisplayName("空记录: 返回空对象不抛异常")
    void init_emptyList_shouldReturnBlank() {
        IpRegion region = IpRegion.init(List.of());

        assertNull(region.getCountry());
        assertNull(region.getProvince());
        assertNull(region.getCity());
        assertNull(region.getIsp());
        assertNull(region.getCountryCode());
    }

    @Test
    @DisplayName("短记录(3段): 防御取值, 后段为 null 不抛异常")
    void init_shortRecord_shouldNotThrow() {
        IpRegion region = IpRegion.init(List.of("中国", "山东省", "济南市"));

        assertEquals("中国", region.getCountry());
        assertEquals("山东省", region.getProvince());
        assertEquals("济南市", region.getCity());
        assertNull(region.getIsp());
        assertNull(region.getCountryCode());
    }

    @Test
    @DisplayName("国内普通城市: isChinaIp=true, isProvinceLevel=false")
    void isChinaIp_normalCity_shouldPass() {
        IpRegion region = IpRegion.init(List.of("中国", "山东省", "济南市", "电信", "CN"));

        assertTrue(region.isChinaIp());
        assertFalse(region.isProvinceLevel());
    }

    @Test
    @DisplayName("直辖市(省名带市后缀): isProvinceLevel 归一化后识别")
    void isProvinceLevel_directCityWithSuffix_shouldDetect() {
        // v4 数据省名可能带"市"后缀
        IpRegion region = IpRegion.init(List.of("中国", "北京市", "北京", "电信", "CN"));

        assertTrue(region.isProvinceLevel());
    }

    @Test
    @DisplayName("直辖市(省名短名): isProvinceLevel 归一化后识别")
    void isProvinceLevel_directCityShortName_shouldDetect() {
        // v4 数据直辖市省段主流格式为短名(实证: 北京219条 vs 北京市2条)
        IpRegion region = IpRegion.init(List.of("中国", "北京", "北京市", "电信", "CN"));

        assertTrue(region.isProvinceLevel());
    }

    @Test
    @DisplayName("自治区(全称后缀): 归一化后可识别为普通中国省份")
    void isChinaIp_autonomousRegion_shouldNotBeProvinceLevel() {
        // v4 数据自治区省段为短名, 归一化补丁支持全称形态兜底
        IpRegion region = IpRegion.init(List.of("中国", "内蒙古自治区", "呼和浩特市", "联通", "CN"));

        assertTrue(region.isChinaIp());
        assertFalse(region.isProvinceLevel());
    }

    @Test
    @DisplayName("港澳台(全称后缀): isBigChina 归一化后识别")
    void isBigChina_hkFullName_shouldDetect() {
        // v4 数据港澳台省段为全称(实证: 香港特别行政区), 迭代去后缀后与短名列表对齐
        IpRegion region = IpRegion.init(List.of("中国", "香港特别行政区", "0", "HKT", "HK"));

        assertTrue(region.isBigChina());
    }

    @Test
    @DisplayName("港澳台: isBigChina 识别")
    void isBigChina_hk_shouldDetect() {
        IpRegion region = IpRegion.init(List.of("中国", "香港", "香港", "HKT", "HK"));

        assertTrue(region.isBigChina());
    }

    // ==================== 商业版（付费数据）下标映射 ====================

    @Test
    @DisplayName("商业基础版(16段): 下标映射正确, 不取到大洲/区县")
    void init_baseVersion_shouldMapFields() {
        // 基础版16段: 大洲|国家|省份|城市|区县|ISP|经度|纬度|行政区码|电话区号|邮编|时区|货币|海拔|气象站|国家码
        String record = "亚洲|中国|广东省|深圳市|宝安区|电信|113.88|22.55|440306|0755|518100|Asia/Shanghai|CNY|11|CHXX0120|CN";
        List<String> ipInfo = StrUtil.split(record, "|");
        IpRegion region = IpRegion.init(ipInfo, Ip2regionDataVersion.BASE);

        assertEquals("中国", region.getCountry());
        assertEquals("广东省", region.getProvince());
        assertEquals("深圳市", region.getCity());
        assertEquals("电信", region.getIsp());
        assertEquals("CN", region.getCountryCode());
    }

    @Test
    @DisplayName("商业高级版(17段): 基础版+ASN, 下标映射不变")
    void init_highVersion_shouldMapFields() {
        // 高级版17段: 基础版在货币(12)后插入ASN(13), 国家码仍在末段
        String record = "亚洲|中国|广东省|深圳市|宝安区|电信|113.88|22.55|440306|0755|518100|Asia/Shanghai|CNY|AS4134|11|CHXX0120|CN";
        List<String> ipInfo = StrUtil.split(record, "|");
        IpRegion region = IpRegion.init(ipInfo, Ip2regionDataVersion.HIGH);

        assertEquals("中国", region.getCountry());
        assertEquals("广东省", region.getProvince());
        assertEquals("深圳市", region.getCity());
        assertEquals("电信", region.getIsp());
        assertEquals("CN", region.getCountryCode());
    }

    @Test
    @DisplayName("商业专业版(18段): 高级版+应用场景, 下标映射不变")
    void init_proVersion_shouldMapFields() {
        // 专业版18段: 高级版在ASN后插入应用场景, 国家码仍在末段
        String record = "亚洲|中国|广东省|深圳市|宝安区|电信|113.88|22.55|440306|0755|518100|Asia/Shanghai|CNY|AS4134|MOB|11|CHXX0120|CN";
        List<String> ipInfo = StrUtil.split(record, "|");
        IpRegion region = IpRegion.init(ipInfo, Ip2regionDataVersion.PRO);

        assertEquals("中国", region.getCountry());
        assertEquals("广东省", region.getProvince());
        assertEquals("深圳市", region.getCity());
        assertEquals("电信", region.getIsp());
        assertEquals("CN", region.getCountryCode());
    }

    @Test
    @DisplayName("商业版国外记录(含缺失空段): 下标映射正确")
    void init_proVersion_overseasWithMissingFields() {
        // 专业版国外示例: 缺失字段用空字符串占位
        String record = "北美洲|美国|California|Los Angeles||美国电话电报公司|-118.24|34.05|||90009|America/Los_Angeles|USD|||US";
        List<String> ipInfo = StrUtil.split(record, "|");
        IpRegion region = IpRegion.init(ipInfo, Ip2regionDataVersion.PRO);

        assertEquals("美国", region.getCountry());
        assertEquals("California", region.getProvince());
        assertEquals("Los Angeles", region.getCity());
        assertEquals("美国电话电报公司", region.getIsp());
        assertEquals("US", region.getCountryCode());
        // 国外地址判定方法正常
        assertFalse(region.isChinaIp());
    }

    @Test
    @DisplayName("商业版仍能正确识别国内地址判定(isChinaIp/isProvinceLevel)")
    void init_baseVersion_chinaDetection() {
        // 北京直辖市: 商业基础版格式
        String record = "亚洲|中国|北京|北京市|朝阳区|电信|116.4|39.9|110105|010|100020|Asia/Shanghai|CNY|11|CHXX0099|CN";
        List<String> ipInfo = StrUtil.split(record, "|");
        IpRegion region = IpRegion.init(ipInfo, Ip2regionDataVersion.BASE);

        assertTrue(region.isChinaIp());
        assertTrue(region.isProvinceLevel());
    }

    @Test
    @DisplayName("开源版默认重载与显式传 OPEN_SOURCE 结果一致")
    void init_defaultOverload_equalsExplicitOpenSource() {
        List<String> ipInfo = List.of("中国", "山东省", "济南市", "电信", "CN");
        IpRegion byDefault = IpRegion.init(ipInfo);
        IpRegion explicit = IpRegion.init(ipInfo, Ip2regionDataVersion.OPEN_SOURCE);

        assertEquals(explicit.getCountry(), byDefault.getCountry());
        assertEquals(explicit.getProvince(), byDefault.getProvince());
        assertEquals(explicit.getCity(), byDefault.getCity());
        assertEquals(explicit.getIsp(), byDefault.getIsp());
        assertEquals(explicit.getCountryCode(), byDefault.getCountryCode());
    }
}
