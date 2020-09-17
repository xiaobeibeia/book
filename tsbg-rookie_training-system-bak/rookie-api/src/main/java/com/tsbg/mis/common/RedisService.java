package com.tsbg.mis.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by sewen on 2017/8/25.
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        if(null != operation){
            operation.set(key, value);
        }
        return operation;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key    缓存的键值
     * @param value  缓存的值
     * @param expire 缓存时间 (秒)
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value, long expire) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        if(null != operation){
            operation.set(key, value, expire, TimeUnit.SECONDS);
        }
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(String key/*,ValueOperations<String,T> operation*/) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        if(null != operation){
            return operation.get(key);
        }
        return null;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList && null != listOperation) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {

                listOperation.rightPush(key, dataList.get(i));
            }
        }

        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        if(null != listOperation){
            Long size = listOperation.size(key);
            if(size != null){
                for (int i = 0; i < size; i++) {
                    dataList.add((T) listOperation.leftPop(key));
                }
            }
        }
        return dataList;
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);

        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            if(null != setOperation){
                setOperation.add(it.next());
            }
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public Set getCacheSet(String key/*,BoundSetOperations<String,T> operation*/) {
        Set dataSet = new HashSet();
        BoundSetOperations operation = redisTemplate.boundSetOps(key);
        if(null != operation){
            Long size = operation.size();
            if(null != size){
                for (int i = 0; i < size; i++) {
                    dataSet.add(operation.pop());
                }
            }
        }
        return dataSet;
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap) {

        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap && null != hashOperations) {
            for (Map.Entry<String, T> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }

        return hashOperations;
    }

    /**
     * 缓存Map
     *
     * @param key
     * @return
     */
    public <T> HashOperations<String, String, T> deleteCacheMapValue(String key, String... keys) {

        HashOperations hashOperations = redisTemplate.opsForHash();
        if(null != hashOperations){
            hashOperations.delete(key, keys);
        }

        return hashOperations;
    }


    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(String key) {
        if(null != redisTemplate.opsForHash()){
            Map<String, T> map = redisTemplate.opsForHash().entries(key);
            return map;
        }
       return null;
    }


    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap && null != hashOperations) {
            for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }

        return hashOperations;
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<Integer, T> getCacheIntegerMap(String key) {
        if(null != redisTemplate.opsForHash()){
            Map<Integer, T> map = redisTemplate.opsForHash().entries(key);
            return map;
        }
        return null;
    }

    /**
     * 通过通配符获取key
     *
     * @param pattern
     * @return
     */
    public Set<String> getKeys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /**
     * 删除
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 模糊删除
     *
     * @param key
     */
    public void vagueDelete(String key){
        Set<String> keys = redisTemplate.keys(key + "*");
        redisTemplate.delete(keys);
    }

    /**
     * 更新有效时间
     *
     * @param sessionId
     * @param expire
     */
    public void updateExpire(String sessionId, long expire) {
        redisTemplate.expire(sessionId, expire, TimeUnit.SECONDS);
    }
}
