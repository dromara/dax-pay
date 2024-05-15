package cn.bootx.platform.baseapi.core.region.entity;

import cn.bootx.platform.baseapi.dto.region.RegionDto;
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
public class Province {

    /** 省份编码 */
    @TableId
    private String code;

    /** 省份名称 */
    private String name;

    /** 转换成dto */
    public RegionDto toDto() {
        return new RegionDto(code, name, 1);
    }

}
