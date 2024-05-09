package cn.bootx.platform.baseapi.core.region.service;

import cn.bootx.platform.baseapi.code.CachingCode;
import cn.bootx.platform.baseapi.code.ChinaRegionCode;
import cn.bootx.platform.baseapi.core.region.dao.*;
import cn.bootx.platform.baseapi.core.region.entity.*;
import cn.bootx.platform.baseapi.dto.region.RegionDto;
import cn.bootx.platform.common.core.util.TreeBuildUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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

    private final VillageManager villageManager;

    /**
     * 根据区划级别和上级区划代码获取当前行政区划的列表
     */
    @Cacheable(value = CachingCode.CHINA_REGION, key = "#parentCode")
    public List<RegionDto> findAllRegionByParentCode(String parentCode) {
        if (parentCode.length() == ChinaRegionCode.IMPORT_TYPE_PROVINCE.getLength()) {
            return cityManager.findAllByProvinceCode(parentCode).stream().map(City::toDto).collect(Collectors.toList());
        }
        else if (parentCode.length() == ChinaRegionCode.IMPORT_TYPE_CITY.getLength()) {
            return areaManager.findAllByCityCode(parentCode).stream().map(Area::toDto).collect(Collectors.toList());
        }
        else if (parentCode.length() == ChinaRegionCode.IMPORT_TYPE_AREA.getLength()) {
            return streetManager.findAllByAreaCode(parentCode).stream().map(Street::toDto).collect(Collectors.toList());
        }
        else if (parentCode.length() == ChinaRegionCode.IMPORT_TYPE_STREET.getLength()) {
            return villageManager.findAllByStreetCode(parentCode)
                .stream()
                .map(Village::toDto)
                .collect(Collectors.toList());
        }
        else {
            return new ArrayList<>(0);
        }
    }

    /**
     * 获取一级行政区
     */
    @Cacheable(value = CachingCode.CHINA_REGION, key = "'p'")
    public List<RegionDto> findAllProvince() {
        return provinceManager.findAll().stream().map(Province::toDto).collect(Collectors.toList());
    }

    /**
     * 获取省市联动列表
     */
    @Cacheable(value = CachingCode.CHINA_REGION, key = "'pc'")
    public List<RegionDto> findAllProvinceAndCity() {
        List<RegionDto> provinceList = provinceManager.findAll()
            .stream()
            .map(Province::toDto)
            .collect(Collectors.toList());
        List<RegionDto> regionList = cityManager.findAll().stream().map(City::toDto).collect(Collectors.toList());
        List<RegionDto> regions = new ArrayList<>(regionList.size() + regionList.size());
        regions.addAll(provinceList);
        regions.addAll(regionList);
        // 构建树
        return TreeBuildUtil.build(regions, null, RegionDto::getCode, RegionDto::getParentCode, RegionDto::setChildren);
    }

    /**
     * 获取省市区县联动列表
     */
    @Cacheable(value = CachingCode.CHINA_REGION, key = "'pca'")
    public List<RegionDto> findAllProvinceAndCityAndArea() {
        List<RegionDto> provinceList = provinceManager.findAll()
            .stream()
            .map(Province::toDto)
            .collect(Collectors.toList());
        List<RegionDto> regionList = cityManager.findAll().stream().map(City::toDto).collect(Collectors.toList());
        List<RegionDto> areaList = areaManager.findAll().stream().map(Area::toDto).collect(Collectors.toList());
        List<RegionDto> regions = new ArrayList<>(regionList.size() + regionList.size() + areaList.size());
        regions.addAll(provinceList);
        regions.addAll(regionList);
        regions.addAll(areaList);

        // 构建树
        return TreeBuildUtil.build(regions, null, RegionDto::getCode, RegionDto::getParentCode, RegionDto::setChildren);
    }

}
