package com.example.auctionapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.RedisConstant;
import com.example.auctionapp.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zll
 * 2018/6/25.
 */
@Service
@Slf4j
public class RedisServiceImpl implements IRedisService {

    @Value("${dgt.user.token.time}")
    private String tokenTime;

    @Autowired
    private RedisTemplate redisTemplate;


    private static final String redis_null_str = "nil";

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存 分布式锁
     *
     * @param key
     * @param value
     * @param expireTime (TimeUnit.SECONDS)
     * @return
     */
    @Override
   public boolean setnx(final String key, Object value, Long expireTime){

        boolean result = false;
        if(expireTime==null){
            expireTime=5L;
        }
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            result=operations.setIfAbsent(key, value);
            if(result) {
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
           log.error("# redis setnx is error:{}",e);
        }
         return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    @Override
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    @Override
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    @Override
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean existsRemove(String key) {
        if (exists(key)) {
            remove(key);
            return true;
        }
        return false;
    }

    @Override
    public Long expire(String key) {
        if (key != null) {
            return redisTemplate.getExpire(key);
        }
        return 0L;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    @Override
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        if (value instanceof String) {
            hash.put(key, hashKey, value);
            return;
        }
        hash.put(key, hashKey, JSON.toJSONString(value));
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param objClass
     */
    @Override
    public <T> T hmGet4Obj(String key, Object hashKey, Class<T> objClass) {
        Object obj = hmGet(key, hashKey);
        if (obj == null) {
            return null;
        }
        return JSON.parseObject(JSON.parse((String) obj).toString(), objClass);
    }

    /**
     * 删除指定哈希数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public void hmDel(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.delete(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    @Override
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    @Override
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    @Override
    public Set<Object> members4Set(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    @Override
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    @Override
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param from
     * @param to
     * @return
     */
    @Override
    public Set<Object> rangeByIndex(String key, int from, int to) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.range(key, from, to);
    }

    /**
     * 倒序获得有序集合获取
     *
     * @param key
     * @param from
     * @param to
     * @return
     */
    @Override
    public Set<Object> rangeByIndexReverse(String key, int from, int to) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRange(key, from, to);
    }

    /**
     * 判断用户是否已登录
     *
     * @param request
     * @return
     */
    @Override
    public boolean isLogin(HttpServletRequest request) {
//        String userId = request.getHeader("userId");
//        if(StringUtils.isEmpty(userId)){
//            return false;
//        }else{
//            boolean exists = exists(ProjectConstant.HTLH_LOGIN_USER_ID_KEY + userId);
//            if(exists){
//                return true;
//            }else{
//                return false;
//            }
//        }
        return true;
    }


    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, Object> getCacheIntegerMap(String key) {
        Map<String, Object> map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    /**
     * 发布消息
     */
    @Override
    public void sendMessage(String channel, Object message) {
        String msgStr;
        if (!(message instanceof String)) {
            msgStr = JSON.toJSONString(message);
        } else {
            msgStr = (String) message;
        }
        redisTemplate.convertAndSend(channel, msgStr);
    }

    @Override
    public Set<Object> keys(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        Map<Object, Object> map = hash.entries(key);
        Set<Object> keySet = map.keySet();
        return keySet;
    }

    @Override
    public void hmDelAll(String key, Set<String> hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        Iterator<String> iterable = hashKey.iterator();
        while (iterable.hasNext()) {
            hash.delete(key, iterable.next());
        }
    }

    @Override
    public Long getIncrId() {
        String key = "Lottery:seq:id";
        long id = 0L;
        if (exists(key)) {
            id = redisTemplate.opsForValue().increment(key, 1);
        } else {
            id = 111222;
            redisTemplate.opsForValue().set(key, id);
        }
        return id;
    }

    /**
     * 活动排行榜
     *
     * @param customerId
     * @param customerId
     * @param activityScoure
     */
    @Override
    public void setActivityZadd(String activityCode, Integer customerId, int activityScoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(RedisConstant.RDS_ACTIVITY_CODE + activityCode, customerId, activityScoure);
        redisTemplate.expire(RedisConstant.RDS_ACTIVITY_CODE + activityCode, 7, TimeUnit.DAYS);
    }

    /**
     * 查询用户的活动排名
     *
     * @param activityCode
     * @param customerId
     * @return
     */
    @Override
    public Long getActivityReverseRank(String activityCode, Integer customerId) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        if (!exists(RedisConstant.RDS_ACTIVITY_CODE + activityCode)) {
            return 0L;
        }
        Long rank = zset.reverseRank(RedisConstant.RDS_ACTIVITY_CODE + activityCode, customerId);
        if (rank == null) {
            return 0L;
        }
        return rank + 1;
    }

    /**
     * 获取用户的score
     *
     * @param activityCode
     * @param customerId
     * @return
     */
    @Override
    public Double getActivityReverseScore(String activityCode, Integer customerId) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        if (!exists(RedisConstant.RDS_ACTIVITY_CODE + activityCode)) {
            return 0d;
        }
        Double score = zset.score(RedisConstant.RDS_ACTIVITY_CODE + activityCode, customerId);
        if (score == null) {
            score = 0d;
        }
        return score;
    }


    /**
     * 获取活动 排名
     *
     * @param activityCode
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<ImmutablePair<String, Double>> getActivityRevRangeWithScores(String activityCode, int start, int end) {
        String redisKey = RedisConstant.RDS_ACTIVITY_CODE + activityCode;
        Set<RedisZSetCommands.Tuple> items = (Set<RedisZSetCommands.Tuple>) redisTemplate.execute((RedisCallback<Set<RedisZSetCommands.Tuple>>) connection -> connection
                .zRevRangeWithScores(redisKey.getBytes(), start, end)
        );
        return items.stream()
                .map(record -> ImmutablePair.of(new String(record.getValue()), record.getScore()))
                .collect(Collectors.toList());
    }

    /**
     * 设置用户Token
     *
     * @param userId
     * @return
     */
    @Override
    public String doUserToken(Integer userId) {
        //拼接存储用户token 的 redis key
        String rdsKey = ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + userId;

        //获取用户的Token
        Object userToken = get(rdsKey);
        if (userToken == null) {
            //存入token，使用UUID类的随机码
            userToken = UUID.randomUUID().toString().replace("-", "");
        }

        //重新set 并且设置失效时间
       set(rdsKey, userToken, new Long(tokenTime), TimeUnit.DAYS);
        return String.valueOf(userToken);
    }


    /**
     * 设置用户tonken
     *
     * @param userId
     * @param token
     * @return
     */
    @Override
    public boolean checkUserToken(Integer userId, String token) {
        //拼接存储用户token 的 redis key
        String rdsKey = ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + userId;
        Object userToken = get(rdsKey);

        //用户token 不存在 返回false
        if (userToken == null) {
            return false;
        }
        //两个token 比较不相等 返回 false
        if (!String.valueOf(userToken).equals(token)) {
            return false;
        }
        return true;
    }
}
