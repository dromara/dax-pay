package cn.bootx.platform.baseapi.service.region;

import cn.bootx.platform.baseapi.dao.region.AreaManager;
import cn.bootx.platform.baseapi.dao.region.CityManager;
import cn.bootx.platform.baseapi.dao.region.ProvinceManager;
import cn.bootx.platform.baseapi.dao.region.StreetManager;
import cn.bootx.platform.baseapi.entity.region.Area;
import cn.bootx.platform.baseapi.entity.region.City;
import cn.bootx.platform.baseapi.entity.region.Province;
import cn.bootx.platform.baseapi.entity.region.Street;
import cn.bootx.platform.baseapi.enums.ChinaRegionEnum;
import cn.bootx.platform.baseapi.result.region.RegionResult;
import cn.bootx.platform.core.util.TreeBuildUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 中国行政区划
 *
 * @author xxm
 * @since 2022/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChinaRegionService {

    private final ProvinceManager provinceManager;

    private final CityManager cityManager;

    private final AreaManager areaManager;

    private final StreetManager streetManager;

    /**
     * 根据区划代码获取下级行政区划的列表
     */
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

    /**
     * 获取一级行政区
     */
    public List<RegionResult> findAllProvince() {
        return provinceManager.findAll().stream().map(Province::toResult).collect(Collectors.toList());
    }

    /**
     * 获取省市联动列表
     */
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

    /**
     * 获取省市区县联动列表
     */
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

}
