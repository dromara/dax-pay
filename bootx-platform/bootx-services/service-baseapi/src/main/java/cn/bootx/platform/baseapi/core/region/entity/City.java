package cn.bootx.platform.baseapi.core.region.entity;

import cn.bootx.platform.baseapi.dto.region.RegionDto;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 城市表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Data
@TableName("base_city")
public class City {

    /** 市/地区编码 */
    @TableId
    private String code;

    /** 城市名称 */
    private String name;

    /** 省份code */
    private String provinceCode;

    /** 转换成dto */
    public RegionDto toDto() {
        return new RegionDto(code, name, 2).setParentCode(provinceCode);
    }

}
