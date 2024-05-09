package cn.bootx.platform.baseapi.core.region.entity;

import cn.bootx.platform.baseapi.dto.region.RegionDto;
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
public class Area {

    /** d */
    @TableId
    private String code;

    /** 名称 */
    private String name;

    /** 城市code */
    private String cityCode;

    /** 转换成dto */
    public RegionDto toDto() {
        return new RegionDto(code, name, 3).setParentCode(cityCode);
    }

}
