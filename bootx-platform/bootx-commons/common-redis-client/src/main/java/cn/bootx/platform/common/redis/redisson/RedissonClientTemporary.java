package cn.bootx.platform.common.redis.redisson;

import org.redisson.api.*;
import org.redisson.api.redisnode.BaseRedisNodes;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonCodec;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * 只是在初始化时候使用一下，之后就会被替换
 *
 * @author xxm
 * @since 2022/11/30
 */
public class RedissonClientTemporary implements RedissonClient {

    /**
     * @param s
     * @param <V>
     * @param <L>
     * @return
     */
    @Override
    public <V, L> RTimeSeries<V, L> getTimeSeries(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @param <L>
     * @return
     */
    @Override
    public <V, L> RTimeSeries<V, L> getTimeSeries(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RStream<K, V> getStream(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RStream<K, V> getStream(String s, Codec codec) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RSearch getSearch() {
        return null;
    }

    /**
     * @param codec
     * @return
     */
    @Override
    public RSearch getSearch(Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RRateLimiter getRateLimiter(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RBinaryStream getBinaryStream(String s) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RGeo<V> getGeo(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RGeo<V> getGeo(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RSetCache<V> getSetCache(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RSetCache<V> getSetCache(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMapCache<K, V> getMapCache(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param mapCacheOptions
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMapCache<K, V> getMapCache(String s, Codec codec, MapCacheOptions<K, V> mapCacheOptions) {
        return null;
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMapCache<K, V> getMapCache(String s) {
        return null;
    }

    /**
     * @param s
     * @param mapCacheOptions
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMapCache<K, V> getMapCache(String s, MapCacheOptions<K, V> mapCacheOptions) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RBucket<V> getBucket(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RBucket<V> getBucket(String s, Codec codec) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RBuckets getBuckets() {
        return null;
    }

    /**
     * @param codec
     * @return
     */
    @Override
    public RBuckets getBuckets(Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param jsonCodec
     * @param <V>
     * @return
     */
    @Override
    public <V> RJsonBucket<V> getJsonBucket(String s, JsonCodec<V> jsonCodec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RList<V> getList(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RList<V> getList(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param localCachedMapOptions
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String s, LocalCachedMapOptions<K, V> localCachedMapOptions) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param localCachedMapOptions
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String s, Codec codec,
            LocalCachedMapOptions<K, V> localCachedMapOptions) {
        return null;
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMap<K, V> getMap(String s) {
        return null;
    }

    /**
     * @param s
     * @param mapOptions
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMap<K, V> getMap(String s, MapOptions<K, V> mapOptions) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMap<K, V> getMap(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param mapOptions
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RMap<K, V> getMap(String s, Codec codec, MapOptions<K, V> mapOptions) {
        return null;
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RSemaphore getSemaphore(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RLock getLock(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RLock getSpinLock(String s) {
        return null;
    }

    /**
     * @param s
     * @param backOff
     * @return
     */
    @Override
    public RLock getSpinLock(String s, LockOptions.BackOff backOff) {
        return null;
    }

    @Override
    public RFencedLock getFencedLock(String s) {
        return null;
    }

    /**
     * @param rLocks
     * @return
     */
    @Override
    public RLock getMultiLock(RLock... rLocks) {
        return null;
    }

    /**
     * @param rLocks
     * @deprecated
     */
    @Override
    public RLock getRedLock(RLock... rLocks) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RLock getFairLock(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RReadWriteLock getReadWriteLock(String s) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RSet<V> getSet(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RSet<V> getSet(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RSortedSet<V> getSortedSet(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RSortedSet<V> getSortedSet(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RLexSortedSet getLexSortedSet(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RShardedTopic getShardedTopic(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @return
     */
    @Override
    public RShardedTopic getShardedTopic(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RTopic getTopic(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @return
     */
    @Override
    public RTopic getTopic(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RReliableTopic getReliableTopic(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @return
     */
    @Override
    public RReliableTopic getReliableTopic(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RPatternTopic getPatternTopic(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @return
     */
    @Override
    public RPatternTopic getPatternTopic(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RQueue<V> getQueue(String s) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RTransferQueue<V> getTransferQueue(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RTransferQueue<V> getTransferQueue(String s, Codec codec) {
        return null;
    }

    /**
     * @param rQueue
     * @param <V>
     * @return
     */
    @Override
    public <V> RDelayedQueue<V> getDelayedQueue(RQueue<V> rQueue) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RQueue<V> getQueue(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RRingBuffer<V> getRingBuffer(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RRingBuffer<V> getRingBuffer(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RDeque<V> getDeque(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RDeque<V> getDeque(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RAtomicLong getAtomicLong(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RAtomicDouble getAtomicDouble(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RLongAdder getLongAdder(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RDoubleAdder getDoubleAdder(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RCountDownLatch getCountDownLatch(String s) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RBitSet getBitSet(String s) {
        return null;
    }

    /**
     * @param s
     * @param <V>
     * @return
     */
    @Override
    public <V> RBloomFilter<V> getBloomFilter(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param <V>
     * @return
     */
    @Override
    public <V> RBloomFilter<V> getBloomFilter(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RIdGenerator getIdGenerator(String s) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RFunction getFunction() {
        return null;
    }

    /**
     * @param codec
     * @return
     */
    @Override
    public RFunction getFunction(Codec codec) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RScript getScript() {
        return null;
    }

    /**
     * @param codec
     * @return
     */
    @Override
    public RScript getScript(Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RScheduledExecutorService getExecutorService(String s) {
        return null;
    }

    /**
     * @param s
     * @param executorOptions
     * @return
     */
    @Override
    public RScheduledExecutorService getExecutorService(String s, ExecutorOptions executorOptions) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @return
     */
    @Override
    public RScheduledExecutorService getExecutorService(String s, Codec codec) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @param executorOptions
     * @return
     */
    @Override
    public RScheduledExecutorService getExecutorService(String s, Codec codec, ExecutorOptions executorOptions) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RRemoteService getRemoteService() {
        return null;
    }

    /**
     * @param codec
     * @return
     */
    @Override
    public RRemoteService getRemoteService(Codec codec) {
        return null;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public RRemoteService getRemoteService(String s) {
        return null;
    }

    /**
     * @param s
     * @param codec
     * @return
     */
    @Override
    public RRemoteService getRemoteService(String s, Codec codec) {
        return null;
    }

    /**
     * @param transactionOptions
     * @return
     */
    @Override
    public RTransaction createTransaction(TransactionOptions transactionOptions) {
        return null;
    }

    /**
     * @param batchOptions
     * @return
     */
    @Override
    public RBatch createBatch(BatchOptions batchOptions) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RBatch createBatch() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RKeys getKeys() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RLiveObjectService getLiveObjectService() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RedissonRxClient rxJava() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RedissonReactiveClient reactive() {
        return null;
    }

    /**
     *
     */
    @Override
    public void shutdown() {

    }

    /**
     * @param l
     * @param l1
     * @param timeUnit
     */
    @Override
    public void shutdown(long l, long l1, TimeUnit timeUnit) {

    }

    /**
     * @return
     */
    @Override
    public Config getConfig() {
        return null;
    }

    /**
     * @param redisNodes
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseRedisNodes> T getRedisNodes(RedisNodes<T> redisNodes) {
        return null;
    }

    /**
     * @deprecated
     */
    @Override
    public NodesGroup<Node> getNodesGroup() {
        return null;
    }

    /**
     * @deprecated
     */
    @Override
    public ClusterNodesGroup getClusterNodesGroup() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public boolean isShutdown() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean isShuttingDown() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public String getId() {
        return null;
    }

}
