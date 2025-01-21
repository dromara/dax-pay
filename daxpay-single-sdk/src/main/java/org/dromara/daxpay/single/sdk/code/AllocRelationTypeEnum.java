package org.dromara.daxpay.single.sdk.code;

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
    SERVICE_PROVIDER("service_provider","服务商"),
    STORE("store","门店"),
    STAFF("staff","员工"),
    STORE_OWNER("store_owner","店主"),
    PARTNER("partner","合作伙伴"),
    HEADQUARTER("headquarter","总部"),
    BRAND("brand","品牌方"),
    DISTRIBUTOR("distributor","分销商"),
    USER("user","用户"),
    SUPPLIER("supplier","供应商"),
    CUSTOM("custom","自定义");

    private final String code;
    private final String name;
}
