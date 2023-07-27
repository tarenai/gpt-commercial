package com.warape.aimechanician.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

/**
 * @author apeto
 * @create 2020/7/3 3:11 下午
 */
public class StringRedisUtils {

  private static final StringRedisTemplate STRING_REDIS_TEMPLATE = SpringUtil.getBean(StringRedisTemplate.class);


  /**
   * 设置 String 类型 key-value
   *
   * @param key
   * @param value
   */
  public static void set (String key, String value) {

    STRING_REDIS_TEMPLATE.opsForValue().set(key, value);
  }

  public static void convertAndSend (String channel, String message) {

    STRING_REDIS_TEMPLATE.convertAndSend(channel, message);
  }
//
//  public static <T> List<T> zRevRangeByScoreWithScores (String key, Integer limitCount, Class<T> tClass) {
//    byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
//
//    RScoredSortedSet<SomeObject> set = redisson.getScoredSortedSet("simple");
//    return null;
//  }


  /**
   * 判断key是否存在
   *
   * @param key
   * @return
   */
  public static Boolean exists (String key) {
    return STRING_REDIS_TEMPLATE.hasKey(key);
  }

  /**
   * 获取 String 类型 key-value
   *
   * @param key
   * @return
   */
  public static String get (String key) {
    return STRING_REDIS_TEMPLATE.opsForValue().get(key);
  }

  /**
   * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
   *
   * @param key
   * @param value
   * @param time  过期时间,分钟单位
   */
  public static void setForTimeSE (String key, String value, long time) {
    STRING_REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.SECONDS);
  }

  /**
   * 设置 String 类型 key-value 并添加过期时间 (毫秒单位)
   *
   * @param key
   * @param value
   * @param time  过期时间,毫秒单位
   */
  public static void setForTimeMS (String key, String value, long time) {
    STRING_REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
  }

  /**
   * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
   *
   * @param key
   * @param value
   * @param time  过期时间,分钟单位
   */
  public static void setForTimeMIN (String key, String value, long time) {
    STRING_REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.MINUTES);
  }


  /**
   * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
   *
   * @param key
   * @param value
   * @param time  过期时间,分钟单位
   */
  public static void setForTimeCustom (String key, String value, long time, TimeUnit type) {
    STRING_REDIS_TEMPLATE.opsForValue().set(key, value, time, type);
  }

  /**
   * 如果 key 存在则覆盖,并返回旧值. 如果不存在,返回null 并添加
   *
   * @param key
   * @param value
   * @return
   */
  public static String getAndSet (String key, String value) {
    return STRING_REDIS_TEMPLATE.opsForValue().getAndSet(key, value);
  }


  /**
   * 批量添加 key-value (重复的键会覆盖)
   *
   * @param keyAndValue
   */
  public static void batchSet (Map<String, String> keyAndValue) {
    STRING_REDIS_TEMPLATE.opsForValue().multiSet(keyAndValue);
  }

  /**
   * 批量添加 key-value 只有在键不存在时,才添加 map 中只要有一个key存在,则全部不添加
   *
   * @param keyAndValue
   */
  public static void batchSetIfAbsent (Map<String, String> keyAndValue) {
    STRING_REDIS_TEMPLATE.opsForValue().multiSetIfAbsent(keyAndValue);
  }


  /**
   * 对一个 key-value 的值进行加减操作, 如果该 key 不存在 将创建一个key 并赋值该 number 如果 key 存在,但 value 不是长整型 ,将报错
   *
   * @param key
   * @param number
   */
  public static Long increment (String key, long number) {
    return STRING_REDIS_TEMPLATE.opsForValue().increment(key, number);
  }

  /**
   * 对一个 key-value 的值进行加减操作, 如果 key 存在,但 value 不是长整型 ,将报错
   *
   * @param key
   */
  public static Long increment (String key) {
    return STRING_REDIS_TEMPLATE.opsForValue().increment(key);
  }

  /**
   * 对一个 key-value 的值进行加减操作, 如果该 key 不存在 将创建一个key 并赋值该 number 如果 key 存在,但 value 不是 纯数字 ,将报错
   *
   * @param key
   * @param number
   */
  public static Double increment (String key, double number) {
    return STRING_REDIS_TEMPLATE.opsForValue().increment(key, number);
  }


  /**
   * 给一个指定的 key 值附加过期时间
   *
   * @param key
   * @param time
   * @param type
   * @return
   */
  public static boolean expire (String key, long time, TimeUnit type) {
    return STRING_REDIS_TEMPLATE.boundValueOps(key).expire(time, type);
  }

  /**
   * 给一个指定的 key 值附加过期时间 秒
   *
   * @param key
   * @param time
   * @return
   */
  public static boolean expireSeconds (String key, long time) {
    return STRING_REDIS_TEMPLATE.boundValueOps(key).expire(time, TimeUnit.SECONDS);
  }

  /**
   * 移除指定key 的过期时间
   *
   * @param key
   * @return
   */
  public static boolean persist (String key) {
    return STRING_REDIS_TEMPLATE.boundValueOps(key).persist();
  }


  /**
   * 获取指定key 的过期时间
   *
   * @param key
   * @return
   */
  public static Long getExpire (String key) {
    return STRING_REDIS_TEMPLATE.boundValueOps(key).getExpire();
  }

  /**
   * 修改 key
   *
   * @param key
   * @return
   */
  public static void rename (String key, String newKey) {
    STRING_REDIS_TEMPLATE.boundValueOps(key).rename(newKey);
  }

  /**
   * 删除 key-value
   *
   * @param key
   * @return
   */
  public static void delete (String key) {
    STRING_REDIS_TEMPLATE.delete(key);
  }

  //hash操作

  /**
   * 添加 Hash 键值对
   *
   * @param key
   * @param hashKey
   * @param value
   */
  public static void put (String key, String hashKey, String value) {
    STRING_REDIS_TEMPLATE.opsForHash().put(key, hashKey, value);
  }

  /**
   * 批量添加 hash 的 键值对 有则覆盖,没有则添加
   *
   * @param key
   * @param map
   */
  public static void putAll (String key, Map<String, String> map) {
    STRING_REDIS_TEMPLATE.opsForHash().putAll(key, map);
  }

  /**
   * 添加 hash 键值对. 不存在的时候才添加
   *
   * @param key
   * @param hashKey
   * @param value
   * @return
   */
  public static boolean putIfAbsent (String key, String hashKey, String value) {
    return STRING_REDIS_TEMPLATE.opsForHash().putIfAbsent(key, hashKey, value);
  }


  /**
   * 删除指定 hash 的 HashKey
   *
   * @param key
   * @param hashKeys
   * @return 删除成功的 数量
   */
  public static Long delete (String key, String... hashKeys) {
    return STRING_REDIS_TEMPLATE.opsForHash().delete(key, hashKeys);
  }


  /**
   * 给指定 hash 的 hashkey 做增减操作
   *
   * @param key
   * @param hashKey
   * @param number
   * @return
   */
  public static Long increment (String key, String hashKey, long number) {
    return STRING_REDIS_TEMPLATE.opsForHash().increment(key, hashKey, number);
  }

  /**
   * 给指定 hash 的 hashkey 做增减操作
   *
   * @param key
   * @param hashKey
   * @param number
   * @return
   */
  public static Double increment (String key, String hashKey, Double number) {
    return STRING_REDIS_TEMPLATE.opsForHash().increment(key, hashKey, number);
  }

  /**
   * 获取指定 key 下的 hashkey
   *
   * @param key
   * @param hashKey
   * @return
   */
  public static String getHashKey (String key, String hashKey) {
    return (String) STRING_REDIS_TEMPLATE.opsForHash().get(key, hashKey);
  }


  /**
   * 获取 key 下的 所有  hashkey 和 value
   *
   * @param key
   * @return
   */
  public static Map<Object, Object> getHashEntries (String key) {
    return STRING_REDIS_TEMPLATE.opsForHash().entries(key);
  }

  /**
   * 验证指定 key 下 有没有指定的 hashkey
   *
   * @param key
   * @param hashKey
   * @return
   */
  public static boolean hashKey (String key, String hashKey) {
    return STRING_REDIS_TEMPLATE.opsForHash().hasKey(key, hashKey);
  }

  /**
   * 获取 key 下的 所有 hashkey 字段名
   *
   * @param key
   * @return
   */
  public static Set<Object> hashKeys (String key) {
    return STRING_REDIS_TEMPLATE.opsForHash().keys(key);
  }


  /**
   * 获取指定 hash 下面的 键值对 数量
   *
   * @param key
   * @return
   */
  public static Long hashSize (String key) {
    return STRING_REDIS_TEMPLATE.opsForHash().size(key);
  }


  /**
   * 获取指定 hash 下面的值
   *
   * @param key
   * @return
   */
  public static List<Object> hashValus (String key) {
    return STRING_REDIS_TEMPLATE.opsForHash().values(key);
  }

  //List 操作

  /**
   * 指定 list 从左入栈
   *
   * @param key
   * @return 当前队列的长度
   */
  public static Long leftPush (String key, String value) {
    return STRING_REDIS_TEMPLATE.opsForList().leftPush(key, value);
  }

  /**
   * 指定 list 从左出栈 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
   *
   * @param key
   * @return 出栈的值
   */
  public static String leftPop (String key) {
    return STRING_REDIS_TEMPLATE.opsForList().leftPop(key);
  }

  /**
   * 从左边依次入栈 导入顺序按照 Collection 顺序 如: a b c => c b a
   *
   * @param key
   * @param values
   * @return
   */
  public static Long leftPushAll (String key, Collection<String> values) {
    return STRING_REDIS_TEMPLATE.opsForList().leftPushAll(key, values);
  }

  /**
   * 指定 list 从右入栈
   *
   * @param key
   * @return 当前队列的长度
   */
  public static Long rightPush (String key, String value) {
    return STRING_REDIS_TEMPLATE.opsForList().rightPush(key, value);
  }

  /**
   * 指定 list 从右出栈 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
   *
   * @param key
   * @return 出栈的值
   */
  public static String rightPop (String key) {
    return STRING_REDIS_TEMPLATE.opsForList().rightPop(key);
  }

  /**
   * 从右边依次入栈 导入顺序按照 Collection 顺序 如: a b c => a b c
   *
   * @param key
   * @param values
   * @return
   */
  public static Long rightPushAll (String key, Collection<String> values) {
    return STRING_REDIS_TEMPLATE.opsForList().rightPushAll(key, values);
  }


  /**
   * 根据下标获取值
   *
   * @param key
   * @param index
   * @return
   */
  public static String popIndex (String key, long index) {
    return STRING_REDIS_TEMPLATE.opsForList().index(key, index);
  }


  /**
   * 获取列表指定长度
   *
   * @param key
   * @param index
   * @return
   */
  public static Long listSize (String key, long index) {
    return STRING_REDIS_TEMPLATE.opsForList().size(key);
  }


  /**
   * 获取列表 指定范围内的所有值
   *
   * @param key
   * @param start
   * @param end
   * @return
   */
  public static List<String> listRange (String key, long start, long end) {
    return STRING_REDIS_TEMPLATE.opsForList().range(key, start, end);
  }


  /**
   * 删除 key 中 值为 value 的 count 个数.
   *
   * @param key
   * @param count
   * @param value
   * @return 成功删除的个数
   */
  public static Long listRemove (String key, long count, String value) {
    return STRING_REDIS_TEMPLATE.opsForList().remove(key, count, value);
  }

  public static Boolean move (String key, int dbIndex) {
    return STRING_REDIS_TEMPLATE.move(key, dbIndex);
  }

  /**
   * 删除 列表 [start,end] 以外的所有元素
   *
   * @param key
   * @param start
   * @param end
   */
  public static void listTrim (String key, long start, long end) {
    STRING_REDIS_TEMPLATE.opsForList().trim(key, start, end);

  }

  /**
   * 将 key 右出栈,并左入栈到 key2
   *
   * @param key  右出栈的列表
   * @param key2 左入栈的列表
   * @return 操作的值
   */
  public static String rightPopAndLeftPush (String key, String key2) {
    return STRING_REDIS_TEMPLATE.opsForList().rightPopAndLeftPush(key, key2);

  }

  //set 操作  无序不重复集合

  /**
   * 添加 set 元素
   *
   * @param key
   * @param values
   * @return
   */
  public static Long add (String key, String... values) {
    return STRING_REDIS_TEMPLATE.opsForSet().add(key, values);
  }

  /**
   * 获取两个集合的差集
   *
   * @param key
   * @return
   */
  public static Set<String> difference (String key, String otherkey) {
    return STRING_REDIS_TEMPLATE.opsForSet().difference(key, otherkey);
  }


  /**
   * 获取 key 和 集合  collections 中的 key 集合的差集
   *
   * @param key
   * @return
   */
  public static Set<String> difference (String key, Collection<String> otherKeys) {
    return STRING_REDIS_TEMPLATE.opsForSet().difference(key, otherKeys);
  }

  /**
   * 将  key 与 otherkey 的差集 ,添加到新的 newKey 集合中
   *
   * @param key
   * @param otherkey
   * @param newKey
   * @return 返回差集的数量
   */
  public static Long differenceAndStore (String key, String otherkey, String newKey) {
    return STRING_REDIS_TEMPLATE.opsForSet().differenceAndStore(key, otherkey, newKey);
  }

  /**
   * 将 key 和 集合  collections 中的 key 集合的差集 添加到  newkey 集合中
   *
   * @param key
   * @param otherKeys
   * @param newKey
   * @return 返回差集的数量
   */
  public static Long differenceAndStore (String key, Collection<String> otherKeys, String newKey) {
    return STRING_REDIS_TEMPLATE.opsForSet().differenceAndStore(newKey, otherKeys, newKey);
  }

  /**
   * 删除一个或多个集合中的指定值
   *
   * @param key
   * @param values
   * @return 成功删除数量
   */
  public static Long remove (String key, Object... values) {
    return STRING_REDIS_TEMPLATE.opsForSet().remove(key, values);
  }

  /**
   * 随机移除一个元素,并返回出来
   *
   * @param key
   * @return
   */
  public static String randomSetPop (String key) {
    return STRING_REDIS_TEMPLATE.opsForSet().pop(key);
  }

  /**
   * 随机获取一个元素
   *
   * @param key
   * @return
   */
  public static String randomSet (String key) {
    return STRING_REDIS_TEMPLATE.opsForSet().randomMember(key);
  }

  /**
   * 随机获取指定数量的元素,同一个元素可能会选中两次
   *
   * @param key
   * @param count
   * @return
   */
  public static List<String> randomSet (String key, long count) {
    return STRING_REDIS_TEMPLATE.opsForSet().randomMembers(key, count);
  }

  /**
   * 随机获取指定数量的元素,去重(同一个元素只能选择两一次)
   *
   * @param key
   * @param count
   * @return
   */
  public static Set<String> randomSetDistinct (String key, long count) {
    return STRING_REDIS_TEMPLATE.opsForSet().distinctRandomMembers(key, count);
  }

  /**
   * 将 key 中的 value 转入到 destKey 中
   *
   * @param key
   * @param value
   * @param destKey
   * @return 返回成功与否
   */
  public static boolean moveSet (String key, String value, String destKey) {
    return STRING_REDIS_TEMPLATE.opsForSet().move(key, value, destKey);
  }

  /**
   * 无序集合的大小
   *
   * @param key
   * @return
   */
  public static Long setSize (String key) {
    return STRING_REDIS_TEMPLATE.opsForSet().size(key);
  }

  /**
   * 判断 set 集合中 是否有 value
   *
   * @param key
   * @param value
   * @return
   */
  public static boolean isMember (String key, Object value) {
    return STRING_REDIS_TEMPLATE.opsForSet().isMember(key, value);
  }

  /**
   * 返回 key 和 othere 的并集
   *
   * @param key
   * @param otherKey
   * @return
   */
  public static Set<String> unionSet (String key, String otherKey) {
    return STRING_REDIS_TEMPLATE.opsForSet().union(key, otherKey);
  }

  /**
   * 返回 key 和 otherKeys 的并集
   *
   * @param key
   * @param otherKeys key 的集合
   * @return
   */
  public static Set<String> unionSet (String key, Collection<String> otherKeys) {
    return STRING_REDIS_TEMPLATE.opsForSet().union(key, otherKeys);
  }

  /**
   * 将 key 与 otherKey 的并集,保存到 destKey 中
   *
   * @param key
   * @param otherKey
   * @param destKey
   * @return destKey 数量
   */
  public static Long unionAndStoreSet (String key, String otherKey, String destKey) {
    return STRING_REDIS_TEMPLATE.opsForSet().unionAndStore(key, otherKey, destKey);
  }

  /**
   * 将 key 与 otherKey 的并集,保存到 destKey 中
   *
   * @param key
   * @param otherKeys
   * @param destKey
   * @return destKey 数量
   */
  public static Long unionAndStoreSet (String key, Collection<String> otherKeys, String destKey) {
    return STRING_REDIS_TEMPLATE.opsForSet().unionAndStore(key, otherKeys, destKey);
  }

  /**
   * 返回集合中所有元素
   *
   * @param key
   * @return
   */
  public static Set<String> members (String key) {
    return STRING_REDIS_TEMPLATE.opsForSet().members(key);
  }

  //Zset 根据 socre 排序   不重复 每个元素附加一个 socre  double类型的属性(double 可以重复)

  /**
   * 添加 ZSet 元素
   *
   * @param key
   * @param value
   * @param score
   */
  public static boolean add (String key, String value, double score) {
    return STRING_REDIS_TEMPLATE.opsForZSet().add(key, value, score);
  }

  /**
   * 批量添加 Zset <br> Set<TypedTuple<Object>> tuples = new HashSet<>();<br> TypedTuple<Object> objectTypedTuple1 = new
   * DefaultTypedTuple<Object>("zset-5",9.6);<br> tuples.add(objectTypedTuple1);
   *
   * @param key
   * @param tuples
   * @return
   */
  public static Long batchAddZset (String key, Set<TypedTuple<String>> tuples) {
    return STRING_REDIS_TEMPLATE.opsForZSet().add(key, tuples);
  }

  /**
   * Zset 删除一个或多个元素
   *
   * @param key
   * @param values
   * @return
   */
  public static Long removeZset (String key, String... values) {
    return STRING_REDIS_TEMPLATE.opsForZSet().remove(key, values);
  }

  /**
   * 对指定的 zset 的 value 值 , socre 属性做增减操作
   *
   * @param key
   * @param value
   * @param score
   * @return
   */
  public static Double incrementScore (String key, String value, double score) {
    return STRING_REDIS_TEMPLATE.opsForZSet().incrementScore(key, value, score);
  }

  /**
   * 获取 key 中指定 value 的排名(从0开始,从小到大排序)
   *
   * @param key
   * @param value
   * @return
   */
  public static Long rank (String key, String value) {
    return STRING_REDIS_TEMPLATE.opsForZSet().rank(key, value);
  }

  /**
   * 获取 key 中指定 value 的排名(从0开始,从大到小排序)
   *
   * @param key
   * @param value
   * @return
   */
  public static Long reverseRank (String key, String value) {
    return STRING_REDIS_TEMPLATE.opsForZSet().reverseRank(key, value);
  }

  /**
   * 获取索引区间内的排序结果集合(从0开始,从小到大,带上分数)
   *
   * @param key
   * @param start
   * @param end
   * @return
   */
  public static Set<TypedTuple<String>> rangeWithScores (String key, long start, long end) {
    return STRING_REDIS_TEMPLATE.opsForZSet().rangeWithScores(key, start, end);
  }


  public static Set<TypedTuple<String>> popMax (String key, long count) {
    return STRING_REDIS_TEMPLATE.opsForZSet().popMax(key, count);
  }

  /**
   * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)
   *
   * @param key
   * @param start
   * @param end
   * @return
   */
  public static Set<String> range (String key, long start, long end) {
    return STRING_REDIS_TEMPLATE.opsForZSet().range(key, start, end);
  }

  /**
   * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
   *
   * @param key
   * @param min
   * @param max
   * @return
   */
  public static Set<String> rangeByScore (String key, double min, double max) {
    return STRING_REDIS_TEMPLATE.opsForZSet().rangeByScore(key, min, max);
  }

  /**
   * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,集合带分数)
   *
   * @param key
   * @param min
   * @param max
   * @return
   */
  public static Set<TypedTuple<String>> rangeByScoreWithScores (String key, double min, double max) {
    return STRING_REDIS_TEMPLATE.opsForZSet().rangeByScoreWithScores(key, min, max);
  }

  /**
   * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,不带分数的集合)
   *
   * @param key
   * @param min
   * @param max
   * @param offset 从指定下标开始
   * @param count  输出指定元素数量
   * @return
   */
  public static Set<String> rangeByScore (String key, double min, double max, long offset, long count) {
    return STRING_REDIS_TEMPLATE.opsForZSet().rangeByScore(key, min, max, offset, count);
  }

  /**
   * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,带分数的集合)
   *
   * @param key
   * @param min
   * @param max
   * @param offset 从指定下标开始
   * @param count  输出指定元素数量
   * @return
   */
  public static Set<TypedTuple<String>> rangeByScoreWithScores (String key, double min, double max, long offset, long count) {
    return STRING_REDIS_TEMPLATE.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
  }

  /**
   * 获取索引区间内的排序结果集合(从0开始,从大到小,只有列名)
   *
   * @param key
   * @param start
   * @param end
   * @return
   */
  public static Set<String> reverseRange (String key, long start, long end) {
    return STRING_REDIS_TEMPLATE.opsForZSet().reverseRange(key, start, end);
  }

  /**
   * 获取索引区间内的排序结果集合(从0开始,从大到小,带上分数)
   *
   * @param key
   * @param start
   * @param end
   * @return
   */
  public static Set<TypedTuple<String>> reverseRangeWithScores (String key, long start, long end) {
    return STRING_REDIS_TEMPLATE.opsForZSet().reverseRangeWithScores(key, start, end);
  }

  /**
   * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合不带分数)
   *
   * @param key
   * @param min
   * @param max
   * @return
   */
  public static Set<String> reverseRangeByScore (String key, double min, double max) {
    return STRING_REDIS_TEMPLATE.opsForZSet().reverseRangeByScore(key, min, max);
  }

  /**
   * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数)
   *
   * @param key
   * @param min
   * @param max
   * @return
   */
  public static Set<TypedTuple<String>> reverseRangeByScoreWithScores (String key, double min, double max) {
    return STRING_REDIS_TEMPLATE.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
  }

  /**
   * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,不带分数的集合)
   *
   * @param key
   * @param min
   * @param max
   * @param offset 从指定下标开始
   * @param count  输出指定元素数量
   * @return
   */
  public static Set<String> reverseRangeByScore (String key, double min, double max, long offset, long count) {
    return STRING_REDIS_TEMPLATE.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
  }

  /**
   * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,带分数的集合)
   *
   * @param key
   * @param min
   * @param max
   * @param offset 从指定下标开始
   * @param count  输出指定元素数量
   * @return
   */
  public static Set<TypedTuple<String>> reverseRangeByScoreWithScores (String key, double min, double max, long offset, long count) {
    return STRING_REDIS_TEMPLATE.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
  }

  /**
   * 返回指定分数区间 [min,max] 的元素个数
   *
   * @param key
   * @param min
   * @param max
   * @return
   */
  public static long countZSet (String key, double min, double max) {
    return STRING_REDIS_TEMPLATE.opsForZSet().count(key, min, max);
  }

  /**
   * 返回 zset 集合数量
   *
   * @param key
   * @return
   */
  public static long sizeZset (String key) {
    return STRING_REDIS_TEMPLATE.opsForZSet().size(key);
  }

  /**
   * 获取指定成员的 score 值
   *
   * @param key
   * @param value
   * @return
   */
  public static Double score (String key, Object value) {
    return STRING_REDIS_TEMPLATE.opsForZSet().score(key, value);
  }

  /**
   * 删除指定索引位置的成员,其中成员分数按( 从小到大 )
   *
   * @param key
   * @param start
   * @param end
   * @return
   */
  public static Long removeRange (String key, long start, long end) {
    return STRING_REDIS_TEMPLATE.opsForZSet().removeRange(key, start, end);
  }

  /**
   * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )
   *
   * @param key
   * @param min
   * @param max
   * @return
   */
  public static Long removeRangeByScore (String key, double min, double max) {
    return STRING_REDIS_TEMPLATE.opsForZSet().removeRangeByScore(key, min, max);
  }

  /**
   * key 和 other 两个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
   *
   * @param key
   * @param otherKey
   * @param destKey
   * @return
   */
  public static Long unionAndStoreZset (String key, String otherKey, String destKey) {
    return STRING_REDIS_TEMPLATE.opsForZSet().unionAndStore(key, otherKey, destKey);
  }

  /**
   * key 和 otherKeys 多个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
   *
   * @param key
   * @param otherKeys
   * @param destKey
   * @return
   */
  public static Long unionAndStoreZset (String key, Collection<String> otherKeys, String destKey) {
    return STRING_REDIS_TEMPLATE.opsForZSet().unionAndStore(key, otherKeys, destKey);
  }

  /**
   * key 和 otherKey 两个集合的交集,保存在 destKey 集合中
   *
   * @param key
   * @param otherKey
   * @param destKey
   * @return
   */
  public static Long intersectAndStore (String key, String otherKey, String destKey) {
    return STRING_REDIS_TEMPLATE.opsForZSet().intersectAndStore(key, otherKey, destKey);
  }

  /**
   * key 和 otherKeys 多个集合的交集,保存在 destKey 集合中
   *
   * @param key
   * @param otherKeys
   * @param destKey
   * @return
   */
  public static Long intersectAndStore (String key, Collection<String> otherKeys, String destKey) {
    return STRING_REDIS_TEMPLATE.opsForZSet().intersectAndStore(key, otherKeys, destKey);
  }
}
