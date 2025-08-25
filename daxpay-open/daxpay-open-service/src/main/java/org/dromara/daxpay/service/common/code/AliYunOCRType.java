package org.dromara.daxpay.service.common.code;

public enum AliYunOCRType {
/**
 * @Description:
 * @Author: zhuyuanbo
 * @Date: 2022/1/18 16:05
 **/
IdCard("身份证"),BusinessLicense("营业执照"),BankCard("银行卡");

    private String name;

    AliYunOCRType(String name) {
        this.name = name;
    }
}
