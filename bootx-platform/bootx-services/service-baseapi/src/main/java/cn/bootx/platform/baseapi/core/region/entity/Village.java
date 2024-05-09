package cn.bootx.platform.baseapi.core.region.entity;

import cn.bootx.platform.baseapi.dto.region.RegionDto;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 村庄/社区
 *
 * @author xxm
 * @since 2023/2/3
 */
@Data
@TableName("base_village")
public class Village {

    /** 街道code */
    @TableId
    private String code;

    /** 街道名称 */
    private String name;

    /** 社区/乡镇 */
    private String streetCode;

    /** 转换成dto */
    public RegionDto toDto() {
        return new RegionDto(code, name, 4).setParentCode(streetCode);
    }

}
