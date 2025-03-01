package cn.bootx.platform.baseapi.enums;

import cn.bootx.platform.core.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 中国行政区划
 *
 * @author xxm
 * @since 2023/2/7
 */
@Getter
@AllArgsConstructor
public enum ChinaRegionEnum {

    /** 省份 */
    IMPORT_TYPE_PROVINCE("province", 1, 2),
    /** 城市 */
    IMPORT_TYPE_CITY("city", 2, 4),
    /** 县区 */
    IMPORT_TYPE_AREA("area", 3, 6),
    /** 街道乡镇 */
    IMPORT_TYPE_STREET("street", 4, 9);

    /** 类型 */
    private final String type;

    /** 级别 */
    private final int level;

    /** 长度 */
    private final int length;

    /**
     * 根据编码判断级别
     */
    public static ChinaRegionEnum findByCode(String code) {
        return Arrays.stream(values())
            .filter(e -> e.length == code.length())
            .findFirst()
            .orElseThrow(() -> new BizException("不支持的类型"));
    }

}
