package cn.bootx.platform.baseapi.controller.region;

import cn.bootx.platform.baseapi.result.region.RegionResult;
import cn.bootx.platform.baseapi.service.region.ChinaRegionService;
import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行政区划控制器
 * @author xxm
 * @since 2024/11/12
 */
@Validated
@IgnoreAuth
@Tag(name = "行政区划控制器")
@RestController
@RequestMapping("/china/region")
@RequiredArgsConstructor
public class ChinaRegionController {

    private final ChinaRegionService chinaRegionService;

    /**
     * 获取所有省份
     */
    @Operation(summary = "获取省份")
    @GetMapping("/findAllProvince")
    public Result<List<RegionResult>> findAllProvince() {
        return Res.ok(chinaRegionService.findAllProvince());
    }

    @Operation(summary = "获取省市联动列表")
    @GetMapping("/findAllProvinceAndCity")
    public Result<List<RegionResult>> findAllProvinceAndCity() {
        return Res.ok(chinaRegionService.findAllProvinceAndCity());
    }

    @Operation(summary = "获取省市区县联动列表")
    @GetMapping("/findAllProvinceAndCityAndArea")
    public Result<List<RegionResult>> findAllProvinceAndCityAndArea() {
        return Res.ok(chinaRegionService.findAllProvinceAndCityAndArea());
    }

    @Operation(summary = "根据区划代码获取下级行政区划的列表")
    @GetMapping("/findAllRegionByParentCode")
    public Result<List<RegionResult>> findAllRegionByParentCode(@NotBlank(message = "区划代码不可为空") String code) {
        return Res.ok(chinaRegionService.findAllRegionByParentCode(code));
    }
}
