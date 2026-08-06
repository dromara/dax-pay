package cn.daxpay.open.platform.system.result.region;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 地理围栏策略预览结果
///
/// 供演示页面查看某个地级市的交界邻市, 并模拟风控三级地理围栏策略(strict/balanced/loose)的放行城市范围。
/// 三级策略的放行集合由前端按语义派生:
/// - strict(严格): 仅允许门店所在市本身
/// - balanced(平衡): 门店所在市 + 交界邻市
/// - loose(宽松): 门店所在省的全部地级市
@Data
@Accessors(chain = true)
@Schema(title = "地理围栏策略预览结果")
public class GeoFencePreviewResult {

    @Schema(description = "选中的城市")
    private CityInfo city;

    @Schema(description = "交界城市列表(balanced 策略邻市, 可跨省)")
    private List<CityInfo> adjacentCities;

    @Schema(description = "同省全部城市(loose 策略用, 含选中市本身)")
    private List<CityInfo> provinceCities;

    /// # 城市信息
    @Data
    @Accessors(chain = true)
    @Schema(title = "城市信息")
    public static class CityInfo {

        @Schema(description = "城市编码(base_city.code, 4位)")
        private String code;

        @Schema(description = "城市名称")
        private String name;

        @Schema(description = "所属省份编码(2位)")
        private String provinceCode;

        @Schema(description = "所属省份名称")
        private String provinceName;
    }
}
