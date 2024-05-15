package cn.bootx.platform.baseapi.core.region.entity;

import cn.bootx.platform.baseapi.dto.region.RegionDto;
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
public class Street {

    /** 街道code */
    @TableId
    private String code;

    /** 街道名称 */
    private String name;

    /** 区县code */
    private String areaCode;

    /** 转换成dto */
    public RegionDto toDto() {
        return new RegionDto(code, name, 4).setParentCode(areaCode);
    }

}
