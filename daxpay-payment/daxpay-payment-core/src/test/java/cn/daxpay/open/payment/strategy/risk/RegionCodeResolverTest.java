package cn.daxpay.open.payment.strategy.risk;

import cn.daxpay.open.platform.system.dao.region.CityManager;
import cn.daxpay.open.platform.system.dao.region.ProvinceManager;
import cn.daxpay.open.platform.system.entity.region.City;
import cn.daxpay.open.platform.system.entity.region.Province;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/// # 行政区划编码解析器单元测试
///
/// 用 data.sql 真实行政区划数据 + ip2region v4 实证返回格式锁定映射规则:
/// 普通省/市全称(广东省/深圳市), 直辖市/自治区短名(北京/内蒙古), "0"段与港澳台 fail-open。
@ExtendWith(MockitoExtension.class)
class RegionCodeResolverTest {

    @Mock
    private ProvinceManager provinceManager;
    @Mock
    private CityManager cityManager;

    private RegionCodeResolver resolver;

    @BeforeEach
    void setUp() {
        // 省份: 普通省全称 / 直辖市全称 / 自治区全称
        Province guangdong = new Province();
        guangdong.setCode("44");
        guangdong.setName("广东省");
        Province beijing = new Province();
        beijing.setCode("11");
        beijing.setName("北京市");
        Province neimenggu = new Province();
        neimenggu.setCode("15");
        neimenggu.setName("内蒙古自治区");
        Province hebei = new Province();
        hebei.setCode("13");
        hebei.setName("河北省");
        when(provinceManager.findAll()).thenReturn(List.of(guangdong, beijing, neimenggu, hebei));
        // 城市: 普通市 / 盟 / 直辖市市辖区
        City shenzhen = new City();
        shenzhen.setCode("4403");
        shenzhen.setName("深圳市");
        shenzhen.setProvinceCode("44");
        City shijiazhuang = new City();
        shijiazhuang.setCode("1301");
        shijiazhuang.setName("石家庄市");
        shijiazhuang.setProvinceCode("13");
        City xingan = new City();
        xingan.setCode("1522");
        xingan.setName("兴安盟");
        xingan.setProvinceCode("15");
        City shixiaqu = new City();
        shixiaqu.setCode("1101");
        shixiaqu.setName("市辖区");
        shixiaqu.setProvinceCode("11");
        when(cityManager.findAll()).thenReturn(List.of(shenzhen, shijiazhuang, xingan, shixiaqu));
        resolver = new RegionCodeResolver(provinceManager, cityManager);
        resolver.init();
    }

    // ========== 省段解析 ==========

    @Test
    @DisplayName("普通省全称(实证主流格式): 精确匹配")
    void resolveProvinceCode_normalFullName_shouldMatch() {
        assertEquals("44", resolver.resolveProvinceCode("广东省"));
        assertEquals("13", resolver.resolveProvinceCode("河北省"));
    }

    @Test
    @DisplayName("直辖市短名(实证主流格式)与全称: 均可匹配")
    void resolveProvinceCode_directCity_shouldMatch() {
        // 短名为 v4 主流(219:2), 全称为 base_province 存储形态, 两者归一化后同 key
        assertEquals("11", resolver.resolveProvinceCode("北京"));
        assertEquals("11", resolver.resolveProvinceCode("北京市"));
    }

    @Test
    @DisplayName("自治区短名(实证主流格式)与全称: 均可匹配")
    void resolveProvinceCode_autoRegion_shouldMatch() {
        assertEquals("15", resolver.resolveProvinceCode("内蒙古"));
        assertEquals("15", resolver.resolveProvinceCode("内蒙古自治区"));
    }

    @Test
    @DisplayName("无归属地\"0\"段与空值: fail-open 返回 null")
    void resolveProvinceCode_zeroOrBlank_shouldReturnNull() {
        assertNull(resolver.resolveProvinceCode("0"));
        assertNull(resolver.resolveProvinceCode(""));
        assertNull(resolver.resolveProvinceCode(null));
        assertNull(resolver.resolveProvinceCode("  "));
    }

    @Test
    @DisplayName("港澳台全称: base_province 无对应行, fail-open 返回 null")
    void resolveProvinceCode_bigChina_shouldReturnNull() {
        assertNull(resolver.resolveProvinceCode("香港特别行政区"));
        assertNull(resolver.resolveProvinceCode("台湾省"));
        assertNull(resolver.resolveProvinceCode("澳门特别行政区"));
    }

    // ========== 市段解析 ==========

    @Test
    @DisplayName("普通地级市全称(实证主流格式): 精确匹配")
    void resolveCityCode_normalCity_shouldMatch() {
        assertEquals("4403", resolver.resolveCityCode("广东省", "深圳市"));
    }

    @Test
    @DisplayName("盟(带盟后缀): 精确匹配")
    void resolveCityCode_league_shouldMatch() {
        assertEquals("1522", resolver.resolveCityCode("内蒙古", "兴安盟"));
    }

    @Test
    @DisplayName("直辖市城市: 回落省编码(城市名单对直辖市存省编码, 市级语义等同省级)")
    void resolveCityCode_directCity_shouldReturnProvinceCode() {
        assertEquals("11", resolver.resolveCityCode("北京", "北京市"));
        assertEquals("11", resolver.resolveCityCode("北京市", "北京"));
    }

    @Test
    @DisplayName("城市\"0\"段(实证存在): fail-open 返回 null")
    void resolveCityCode_zeroCity_shouldReturnNull() {
        assertNull(resolver.resolveCityCode("广东省", "0"));
        assertNull(resolver.resolveCityCode("广东省", ""));
        assertNull(resolver.resolveCityCode("广东省", null));
    }

    @Test
    @DisplayName("未知城市/省解析失败: fail-open 返回 null")
    void resolveCityCode_unknown_shouldReturnNull() {
        assertNull(resolver.resolveCityCode("广东省", "不存在的城市"));
        assertNull(resolver.resolveCityCode("香港特别行政区", "0"));
        assertNull(resolver.resolveCityCode(null, "深圳市"));
    }
}
