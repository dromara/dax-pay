package cn.bootx.platform.notice.core.sms.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.notice.core.sms.entity.SmsChannelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsChannelConfigManager extends BaseManager<SmsChannelConfigMapper, SmsChannelConfig> {


    public boolean existsByCode(String code){
        return existedByField(SmsChannelConfig::getCode,code);
    }
    public boolean existsByCode(String code,Long id){
        return existedByField(SmsChannelConfig::getCode,code,id);
    }

    public Optional<SmsChannelConfig> findByCode(String code){
        return findByField(SmsChannelConfig::getCode,code);
    }
}
