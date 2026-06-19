package cn.daxpay.open.platform.system.entity.region;

import cn.daxpay.open.platform.system.result.region.RegionResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/// # 区域表(县区)
///
@Data
@TableName("base_area")
public class Area implements ToResult<RegionResult> {

    /// 县区编码
    @TableId
    private String code;

    /// 名称
    private String name;

    /// 城市code
    private String cityCode;

    @Override
    public RegionResult toResult() {
        return new RegionResult(code, name, 3).setParentCode(cityCode);
    }

}
