package cn.daxpay.open.platform.capability.audit.log.service.ip2region;

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
    @DisplayName("港澳台: isBigChina 识别")
    void isBigChina_hk_shouldDetect() {
        IpRegion region = IpRegion.init(List.of("中国", "香港", "香港", "HKT", "HK"));

        assertTrue(region.isBigChina());
    }
}
