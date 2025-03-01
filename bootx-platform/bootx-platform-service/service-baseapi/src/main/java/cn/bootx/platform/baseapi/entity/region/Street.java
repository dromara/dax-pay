package cn.bootx.platform.baseapi.entity.region;

import cn.bootx.platform.baseapi.result.region.RegionResult;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 街道/乡镇表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Data
@TableName("base_street")
public class Street implements ToResult<RegionResult> {

    /** 街道code */
    @TableId
    private String code;

    /** 街道名称 */
    private String name;

    /** 区县code */
    private String areaCode;

    @Override
    public RegionResult toResult() {
        return new RegionResult(code, name, 4).setParentCode(areaCode);
    }

}
