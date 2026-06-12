package org.dromara.daxpay.platform.system.entity.region;

import org.dromara.daxpay.platform.system.result.region.RegionResult;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/// # 街道/乡镇表
///
@Data
@TableName("base_street")
public class Street implements ToResult<RegionResult> {

    /// 街道code
    @TableId
    private String code;

    /// 街道名称
    private String name;

    /// 区县code
    private String areaCode;

    @Override
    public RegionResult toResult() {
        return new RegionResult(code, name, 4).setParentCode(areaCode);
    }

}
