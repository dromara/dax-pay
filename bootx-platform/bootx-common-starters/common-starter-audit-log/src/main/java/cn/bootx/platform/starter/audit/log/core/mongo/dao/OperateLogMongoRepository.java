package cn.bootx.platform.starter.audit.log.core.mongo.dao;

import cn.bootx.platform.starter.audit.log.core.mongo.entity.OperateLogMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

/**
 * mongo持久化方式
 *
 * @author xxm
 * @since 2021/12/2
 */
public interface OperateLogMongoRepository extends MongoRepository<OperateLogMongo, Long> {
    int deleteByOperateTimeBefore(LocalDateTime offset);
}
