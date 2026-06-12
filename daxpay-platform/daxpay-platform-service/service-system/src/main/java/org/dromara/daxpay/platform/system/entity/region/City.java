package org.dromara.daxpay.platform.system.entity.region;

import org.dromara.daxpay.platform.system.result.region.RegionResult;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/// # 城市表
///
@Data
@TableName("base_city")
public class City implements ToResult<RegionResult> {

    /// 市/地区编码
    @TableId
    private String code;

    /// 城市名称
    private String name;

    /// 省份code
    private String provinceCode;

    @Override
    public RegionResult toResult() {
        return new RegionResult(code, name, 2).setParentCode(provinceCode);
    }

}
