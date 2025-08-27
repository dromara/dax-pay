package cn.bootx.platform.baseapi.entity.region;

import cn.bootx.platform.baseapi.result.region.RegionResult;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 区域表(县区)
 *
 * @author xxm
 * @since 2022-12-24
 */
@Data
@TableName("base_area")
public class Area implements ToResult<RegionResult> {

    /** 县区编码 */
    @TableId
    private String code;

    /** 名称 */
    private String name;

    /** 城市code */
    private String cityCode;

    @Override
    public RegionResult toResult() {
        return new RegionResult(code, name, 3).setParentCode(cityCode);
    }

}
