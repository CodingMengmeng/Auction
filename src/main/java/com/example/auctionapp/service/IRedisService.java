package com.example.auctionapp.service;

import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zll on 2018/6/25.
 */
public interface IRedisService {

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime(TimeUnit.SECONDS)
     * @return
     */
    boolean setnx(final String key, Object value, Long expireTime);

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit);

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    void remove(final String... keys);

    /**
     * 批量删除key
     *
     * @param pattern
     */
    void removePattern(final String pattern);

    /**
     * 删除对应的value
     *
     * @param key
     */
    void remove(final String key);

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    boolean exists(final String key);

    /**
     * 判断缓存中是否有对应的value,如果存在则会删除并返回true
     *
     * @param key
     * @return
     */
    boolean existsRemove(final String key);


    /**
     * 查看key的过期时间
     */
    Long expire(String key);

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    Object get(final String key);

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    void hmSet(String key, Object hashKey, Object value);

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    Object hmGet(String key, Object hashKey);

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param objClass
     */
    <T> T hmGet4Obj(String key, Object hashKey, Class<T> objClass);

    /**
     * 删除指定哈希数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    void hmDel(String key, Object hashKey);

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    void lPush(String k, Object v);

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    List<Object> lRange(String k, long l, long l1);

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    void add(String key, Object value);

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    Set<Object> members4Set(String key);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    void zAdd(String key, Object value, double scoure);

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    Set<Object> rangeByScore(String key, double scoure, double scoure1);

    /**
     * 有序集合获取
     *
     * @param key
     * @param from
     * @param to
     * @return
     */
    Set<Object> rangeByIndex(String key, int from, int to);

    /**
     * 倒序获得有序集合获取
     *
     * @param key
     * @param from
     * @param to
     * @return
     */
    Set<Object> rangeByIndexReverse(String key, int from, int to);

    /**
     * 判断用户是否已登录
     *
     * @param request
     * @return
     */
    boolean isLogin(HttpServletRequest request);


    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    Map<String, Object> getCacheIntegerMap(String key);

    /**
     * 发布消息
     */
    void sendMessage(String channel, Object message);



    Set<Object> keys(String key);

    void hmDelAll(String key, Set<String> hashKey);

    /**
     * redis incrId
     * @return
     */
    Long getIncrId();

    /**
     *  活动排行榜
     * @param customerId
     * @param customerId
     * @param activityScoure
     */
    void setActivityZadd(String activityCode,Integer customerId,int activityScoure);

    /**
     * 查询用户的活动排名
     * @param activityCode
     * @param customerId
     * @return
     */
    Long getActivityReverseRank(String activityCode,Integer customerId);

    /**
     * 获取用户的score
     * @param activityCode
     * @param customerId
     * @return
     */
    Double getActivityReverseScore(String activityCode,Integer customerId);

    /**
     * 获取活动 排名
     * @param activityCode
     * @param start
     * @param end
     * @return
     */
    public List<ImmutablePair<String, Double>> getActivityRevRangeWithScores(String activityCode, int start, int end);

    /**
     *  设置用户tonken
     * @param userId
     * @return
     */
    String doUserToken(Integer userId);

    /**
     *  设置用户tonken
     * @param userId
     * @param  token
     * @return
     */
    boolean checkUserToken(Integer userId , String token);
}
