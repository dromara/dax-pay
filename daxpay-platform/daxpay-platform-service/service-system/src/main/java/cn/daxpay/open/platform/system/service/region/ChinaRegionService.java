package cn.daxpay.open.platform.system.service.region;

import cn.daxpay.open.platform.system.dao.region.AreaManager;
import cn.daxpay.open.platform.system.dao.region.CityManager;
import cn.daxpay.open.platform.system.dao.region.ProvinceManager;
import cn.daxpay.open.platform.system.dao.region.StreetManager;
import cn.daxpay.open.platform.system.entity.region.Area;
import cn.daxpay.open.platform.system.entity.region.City;
import cn.daxpay.open.platform.system.entity.region.Province;
import cn.daxpay.open.platform.system.entity.region.Street;
import cn.daxpay.open.platform.system.enums.ChinaRegionEnum;
import cn.daxpay.open.platform.system.result.region.GeoFencePreviewResult;
import cn.daxpay.open.platform.system.result.region.RegionResult;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.TreeBuildUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/// # 中国行政区划
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChinaRegionService {

    private final ProvinceManager provinceManager;

    private final CityManager cityManager;

    private final AreaManager areaManager;

    private final StreetManager streetManager;

    private final ChinaRegionAdjacencyService chinaRegionAdjacencyService;

    /// 根据区划代码获取下级行政区划的列表
    public List<RegionResult> findAllRegionByParentCode(String parentCode) {
        if (parentCode.length() == ChinaRegionEnum.IMPORT_TYPE_PROVINCE.getLength()) {
            return cityManager.findAllByProvinceCode(parentCode).stream().map(City::toResult).collect(Collectors.toList());
        }
        else if (parentCode.length() == ChinaRegionEnum.IMPORT_TYPE_CITY.getLength()) {
            return areaManager.findAllByCityCode(parentCode).stream().map(Area::toResult).collect(Collectors.toList());
        }
        else if (parentCode.length() == ChinaRegionEnum.IMPORT_TYPE_AREA.getLength()) {
            return streetManager.findAllByAreaCode(parentCode).stream().map(Street::toResult).collect(Collectors.toList());
        }
        else {
            return new ArrayList<>(0);
        }
    }

    /// 获取一级行政区
    public List<RegionResult> findAllProvince() {
        return provinceManager.findAll().stream().map(Province::toResult).collect(Collectors.toList());
    }

    /// 获取省市联动列表
    public List<RegionResult> findAllProvinceAndCity() {
        List<RegionResult> provinceList = provinceManager.findAll()
            .stream()
            .map(Province::toResult)
            .toList();
        List<RegionResult> regionList = cityManager.findAll().stream().map(City::toResult).toList();
        List<RegionResult> regions = new ArrayList<>(regionList.size() + regionList.size());
        regions.addAll(provinceList);
        regions.addAll(regionList);
        // 构建树
        return TreeBuildUtil.build(regions, null, RegionResult::getCode, RegionResult::getParentCode, RegionResult::setChildren);
    }

    /// 获取省市区县联动列表
    public List<RegionResult> findAllProvinceAndCityAndArea() {
        List<RegionResult> provinceList = provinceManager.findAll()
            .stream()
            .map(Province::toResult)
            .toList();
        List<RegionResult> regionList = cityManager.findAll().stream().map(City::toResult).toList();
        List<RegionResult> areaList = areaManager.findAll().stream().map(Area::toResult).toList();
        List<RegionResult> regions = new ArrayList<>(regionList.size() + regionList.size() + areaList.size());
        regions.addAll(provinceList);
        regions.addAll(regionList);
        regions.addAll(areaList);

        // 构建树
        return TreeBuildUtil.build(regions, null, RegionResult::getCode, RegionResult::getParentCode, RegionResult::setChildren);
    }

    /// 地理围栏策略预览: 查询指定城市的交界邻市与同省全部城市, 供演示页面模拟三级策略放行范围
    ///
    /// @param cityCode 城市编码(base_city.code, 4位)
    /// @return 预览结果, 含选中市、交界市、同省全部市
    public GeoFencePreviewResult previewGeoFence(String cityCode) {
        // 选中城市本身
        City city = cityManager.findById(cityCode)
                .orElseThrow(DataNotExistException::new);
        // 省份编码 → 名称 映射(交界市可能跨省, 需按各自省份解析名称)
        Map<String, String> provinceNameMap = provinceManager.findAll().stream()
                .collect(Collectors.toMap(Province::getCode, Province::getName, (a, b) -> a));

        // 交界邻市编码集合 → 批量解析为城市实体(交界关系可能跨省)
        Set<String> adjacentCodes = chinaRegionAdjacencyService.findAdjacentCityCodes(cityCode);
        List<City> adjacentCities = cityManager.findAllByIds(adjacentCodes);

        // 同省全部城市(loose 策略范围, 含自身)
        List<City> provinceCities = cityManager.findAllByProvinceCode(city.getProvinceCode());

        GeoFencePreviewResult result = new GeoFencePreviewResult();
        result.setCity(toCityInfo(city, provinceNameMap));
        result.setAdjacentCities(adjacentCities.stream()
                .map(c -> toCityInfo(c, provinceNameMap))
                .toList());
        result.setProvinceCities(provinceCities.stream()
                .map(c -> toCityInfo(c, provinceNameMap))
                .toList());
        return result;
    }

    /// 将城市实体转换为预览用的城市信息(附带省份名称)
    private GeoFencePreviewResult.CityInfo toCityInfo(City c, Map<String, String> provinceNameMap) {
        return new GeoFencePreviewResult.CityInfo()
                .setCode(c.getCode())
                .setName(c.getName())
                .setProvinceCode(c.getProvinceCode())
                .setProvinceName(provinceNameMap.get(c.getProvinceCode()));
    }

}
