package cn.daxpay.open.payment.strategy.risk;

import cn.daxpay.open.platform.system.dao.region.CityManager;
import cn.daxpay.open.platform.system.dao.region.ProvinceManager;
import cn.daxpay.open.platform.system.entity.region.City;
import cn.daxpay.open.platform.system.entity.region.Province;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/// # 行政区划编码解析器
///
/// 将 ip2region 返回的地区名称解析为行政区划编码(省2位/市4位), 供省市两级地区黑名单按编码匹配。
///
/// 背景: 黑名单存储使用行政区划编码(code)而非名称, 而 ip2region 返回的是名称文本。
/// v4 xdb 名称格式规律(实证): 普通省/市返回全称(广东省/深圳市); 直辖市与自治区返回短名(北京/内蒙古);
/// 港澳台返回全称但 base_province 无对应行; 无归属地返回 "0"。
///
/// 映射策略:
/// - 普通省/市: 按 base_province/base_city 全称精确匹配, 归一化名称兜底
/// - 直辖市/自治区: 短名→省编码 硬编码补丁(与全称归一化结果幂等)
/// - 直辖市城市: base_city 仅存"市辖区", 城市名单对直辖市存省编码, 解析直接回落省编码(省级语义)
/// - 港澳台 / "0"段 / 未知: 无法映射返回 null(fail-open)
///
/// 启动时一次性加载行政区划数据(省31条+市约340条), 运行时纯内存查找, 不查库。
@Component
@RequiredArgsConstructor
public class RegionCodeResolver {

    private final ProvinceManager provinceManager;
    private final CityManager cityManager;

    /// 直辖市省份编码(其城市名单存省编码, 无独立市级语义)
    private static final Set<String> DIRECT_CITY_CODES = Set.of("11", "12", "31", "50");

    /// 直辖市短名→省编码 硬编码补丁(ip2region v4 省段返回短名, 与 base_province 全称无法归一化对齐)
    private static final Map<String, String> DIRECT_CITY_SHORT_TO_CODE = Map.of(
            "北京", "11", "上海", "31", "天津", "12", "重庆", "50");

    /// 自治区短名→省编码 硬编码补丁(ip2region v4 省段返回短名, 与 base_province 全称无法归一化对齐)
    private static final Map<String, String> AUTO_REGION_SHORT_TO_CODE = Map.of(
            "内蒙古", "15", "广西", "45", "新疆", "65", "西藏", "54", "宁夏", "64");

    /// 省份全称→省编码索引
    private volatile Map<String, String> provinceNameToCode = Map.of();
    /// 省份归一化名称(含短名补丁)→省编码索引
    private volatile Map<String, String> provinceNormToCode = Map.of();
    /// 城市全称→市编码索引
    private volatile Map<String, String> cityNameToCode = Map.of();
    /// 城市归一化名称→市编码索引
    private volatile Map<String, String> cityNormToCode = Map.of();

    /// 启动构建内存索引
    @PostConstruct
    public void init() {
        Map<String, String> pNameToCode = new HashMap<>();
        Map<String, String> pNormToCode = new HashMap<>();
        for (Province province : provinceManager.findAll()) {
            pNameToCode.put(province.getName(), province.getCode());
            pNormToCode.putIfAbsent(GeoFenceUtil.normalizeRegionName(province.getName()), province.getCode());
        }
        // 直辖市/自治区短名补丁: 与全称归一化结果幂等覆盖("北京市"→"北京"与短名"北京"同 key)
        pNormToCode.putAll(DIRECT_CITY_SHORT_TO_CODE);
        pNormToCode.putAll(AUTO_REGION_SHORT_TO_CODE);

        Map<String, String> cNameToCode = new HashMap<>();
        Map<String, String> cNormToCode = new HashMap<>();
        for (City city : cityManager.findAll()) {
            cNameToCode.put(city.getName(), city.getCode());
            cNormToCode.putIfAbsent(GeoFenceUtil.normalizeRegionName(city.getName()), city.getCode());
        }

        this.provinceNameToCode = Map.copyOf(pNameToCode);
        this.provinceNormToCode = Map.copyOf(pNormToCode);
        this.cityNameToCode = Map.copyOf(cNameToCode);
        this.cityNormToCode = Map.copyOf(cNormToCode);
    }

    /// 解析省份编码; 无法映射(港澳台/无归属"0"/未知)返回 null
    ///
    /// 匹配顺序: 全称精确 → 归一化(含直辖市/自治区短名补丁)。空白与 "0" 段直接返回 null(fail-open)。
    public String resolveProvinceCode(String provinceName) {
        if (StrUtil.isBlank(provinceName) || "0".equals(provinceName.trim())) {
            return null;
        }
        String name = provinceName.trim();
        String code = provinceNameToCode.get(name);
        if (code != null) {
            return code;
        }
        return provinceNormToCode.get(GeoFenceUtil.normalizeRegionName(name));
    }

    /// 解析城市编码; 无法映射返回 null
    ///
    /// 直辖市: city 段返回"北京市"而 base_city 仅存"市辖区", 城市名单对直辖市存省编码,
    /// 因此直接回落为省编码(省级语义), 由调用方查市级名单。其余按 base_city 全称/归一化匹配。
    public String resolveCityCode(String provinceName, String cityName) {
        if (StrUtil.isBlank(provinceName)) {
            return null;
        }
        String provinceCode = resolveProvinceCode(provinceName);
        if (provinceCode == null) {
            return null;
        }
        // 直辖市: 城市名单存省编码, 回落省编码
        if (DIRECT_CITY_CODES.contains(provinceCode)) {
            return provinceCode;
        }
        if (StrUtil.isBlank(cityName) || "0".equals(cityName.trim())) {
            return null;
        }
        String name = cityName.trim();
        String code = cityNameToCode.get(name);
        if (code != null) {
            return code;
        }
        return cityNormToCode.get(GeoFenceUtil.normalizeRegionName(name));
    }
}
