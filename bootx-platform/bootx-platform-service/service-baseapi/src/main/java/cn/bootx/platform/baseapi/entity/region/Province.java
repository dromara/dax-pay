package cn.bootx.platform.baseapi.entity.region;

import cn.bootx.platform.baseapi.result.region.RegionResult;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 省份表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Data
@TableName("base_province")
public class Province implements ToResult<RegionResult> {

    /** 省份编码 */
    @TableId
    private String code;

    /** 省份名称 */
    private String name;


    @Override
    public RegionResult toResult() {
        return new RegionResult(code, name, 1);
    }

}
