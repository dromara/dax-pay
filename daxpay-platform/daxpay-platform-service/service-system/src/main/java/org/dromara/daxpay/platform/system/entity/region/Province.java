package org.dromara.daxpay.platform.system.entity.region;

import org.dromara.daxpay.platform.system.result.region.RegionResult;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/// # 省份表
///
@Data
@TableName("base_province")
public class Province implements ToResult<RegionResult> {

    /// 省份编码
    @TableId
    private String code;

    /// 省份名称
    private String name;

    @Override
    public RegionResult toResult() {
        return new RegionResult(code, name, 1);
    }

}
