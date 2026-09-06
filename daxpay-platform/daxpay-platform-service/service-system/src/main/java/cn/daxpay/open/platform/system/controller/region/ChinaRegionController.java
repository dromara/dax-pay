package cn.daxpay.open.platform.system.controller.region;

import cn.daxpay.open.platform.system.result.region.RegionResult;
import cn.daxpay.open.platform.system.service.region.ChinaRegionService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 行政区划控制器
///
@Validated
@IgnoreAuth
@Tag(name = "行政区划控制器")
@RestController
@RequestMapping("/china/region")
@RequiredArgsConstructor
public class ChinaRegionController {

    private final ChinaRegionService chinaRegionService;

    /// 获取所有省份
    @Operation(summary = "获取省份")
    @GetMapping("/all-province")
    public Result<List<RegionResult>> findAllProvince() {
        return Res.ok(chinaRegionService.findAllProvince());
    }

    /// 获取省市联动列表
    ///
    /// @return 省市联动列表
    @Operation(summary = "获取省市联动列表")
    @GetMapping("/all-province-and-city")
    public Result<List<RegionResult>> findAllProvinceAndCity() {
        return Res.ok(chinaRegionService.findAllProvinceAndCity());
    }

    /// 获取省市区县联动列表
    ///
    /// @return 省市区县联动列表
    @Operation(summary = "获取省市区县联动列表")
    @GetMapping("/all-province-and-city-and-area")
    public Result<List<RegionResult>> findAllProvinceAndCityAndArea() {
        return Res.ok(chinaRegionService.findAllProvinceAndCityAndArea());
    }

    /// 根据区划代码获取下级行政区划的列表
    ///
    /// @param code 区划代码
    /// @return 下级行政区划列表
    @Operation(summary = "根据区划代码获取下级行政区划的列表")
    @GetMapping("/all-region-by-parent-code")
    public Result<List<RegionResult>> findAllRegionByParentCode(@NotBlank(message = "{validation.field.regionCode.notBlank}") String code) {
        return Res.ok(chinaRegionService.findAllRegionByParentCode(code));
    }
}

