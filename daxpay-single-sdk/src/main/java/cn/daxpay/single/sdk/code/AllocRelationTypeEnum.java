package cn.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分账关系类型
 * @author xxm
 * @since 2024/3/27
 */
@Getter
@AllArgsConstructor
public enum AllocRelationTypeEnum {
    SERVICE_PROVIDER("SERVICE_PROVIDER","服务商"),
    STORE("STORE","门店"),
    STAFF("STAFF","员工"),
    STORE_OWNER("STORE_OWNER","店主"),
    PARTNER("PARTNER","合作伙伴"),
    HEADQUARTER("HEADQUARTER","总部"),
    BRAND("BRAND","品牌方"),
    DISTRIBUTOR("DISTRIBUTOR","分销商"),
    USER("USER","用户"),
    SUPPLIER("SUPPLIER","供应商"),
    CUSTOM("CUSTOM","自定义");

    private final String code;
    private final String name;
}
