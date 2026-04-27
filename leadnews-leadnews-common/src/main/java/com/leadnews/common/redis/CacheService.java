package com.leadnews.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CacheService extends CachingConfigurerSupport {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public StringRedisTemplate getstringRedisTemplate() {
        return this.stringRedisTemplate;
    }

    /** -------------------key鐩稿叧鎿嶄綔--------------------- */

    /**
     * 鍒犻櫎key
     *
     * @param key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 鎵归噺鍒犻櫎key
     *
     * @param keys
     */
    public void delete(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /**
     * 搴忓垪鍖杒ey
     *
     * @param key
     * @return
     */
    public byte[] dump(String key) {
        return stringRedisTemplate.dump(key);
    }

    /**
     * 鏄惁瀛樺湪key
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 璁剧疆杩囨湡鏃堕棿
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.expire(key, timeout, unit);
    }

    /**
     * 璁剧疆杩囨湡鏃堕棿
     *
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(String key, Date date) {
        return stringRedisTemplate.expireAt(key, date);
    }

    /**
     * 鏌ユ壘鍖归厤鐨刱ey
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /**
     * 灏嗗綋鍓嶆暟鎹簱鐨?key 绉诲姩鍒扮粰瀹氱殑鏁版嵁搴?db 褰撲腑
     *
     * @param key
     * @param dbIndex
     * @return
     */
    public Boolean move(String key, int dbIndex) {
        return stringRedisTemplate.move(key, dbIndex);
    }

    /**
     * 绉婚櫎 key 鐨勮繃鏈熸椂闂达紝key 灏嗘寔涔呬繚鎸?
     *
     * @param key
     * @return
     */
    public Boolean persist(String key) {
        return stringRedisTemplate.persist(key);
    }

    /**
     * 杩斿洖 key 鐨勫墿浣欑殑杩囨湡鏃堕棿
     *
     * @param key
     * @param unit
     * @return
     */
    public Long getExpire(String key, TimeUnit unit) {
        return stringRedisTemplate.getExpire(key, unit);
    }

    /**
     * 杩斿洖 key 鐨勫墿浣欑殑杩囨湡鏃堕棿
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    /**
     * 浠庡綋鍓嶆暟鎹簱涓殢鏈鸿繑鍥炰竴涓?key
     *
     * @return
     */
    public String randomKey() {
        return stringRedisTemplate.randomKey();
    }

    /**
     * 淇敼 key 鐨勫悕绉?
     *
     * @param oldKey
     * @param newKey
     */
    public void rename(String oldKey, String newKey) {
        stringRedisTemplate.rename(oldKey, newKey);
    }

    /**
     * 浠呭綋 newkey 涓嶅瓨鍦ㄦ椂锛屽皢 oldKey 鏀瑰悕涓?newkey
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return stringRedisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 杩斿洖 key 鎵€鍌ㄥ瓨鐨勫€肩殑绫诲瀷
     *
     * @param key
     * @return
     */
    public DataType type(String key) {
        return stringRedisTemplate.type(key);
    }

    /** -------------------string鐩稿叧鎿嶄綔--------------------- */

    /**
     * 璁剧疆鎸囧畾 key 鐨勫€?
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 鑾峰彇鎸囧畾 key 鐨勫€?
     * @param key
     * @return
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 杩斿洖 key 涓瓧绗︿覆鍊肩殑瀛愬瓧绗?
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String getRange(String key, long start, long end) {
        return stringRedisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 灏嗙粰瀹?key 鐨勫€艰涓?value 锛屽苟杩斿洖 key 鐨勬棫鍊?old value)
     *
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key, String value) {
        return stringRedisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 瀵?key 鎵€鍌ㄥ瓨鐨勫瓧绗︿覆鍊硷紝鑾峰彇鎸囧畾鍋忕Щ閲忎笂鐨勪綅(bit)
     *
     * @param key
     * @param offset
     * @return
     */
    public Boolean getBit(String key, long offset) {
        return stringRedisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 鎵归噺鑾峰彇
     *
     * @param keys
     * @return
     */
    public List<String> multiGet(Collection<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 璁剧疆ASCII鐮? 瀛楃涓?a'鐨凙SCII鐮佹槸97, 杞负浜岃繘鍒舵槸'01100001', 姝ゆ柟娉曟槸灏嗕簩杩涘埗绗琽ffset浣嶅€煎彉涓簐alue
     *
     * @param key
     * @param
     * @param value
     *            鍊?true涓?, false涓?
     * @return
     */
    public boolean setBit(String key, long offset, boolean value) {
        return stringRedisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 灏嗗€?value 鍏宠仈鍒?key 锛屽苟灏?key 鐨勮繃鏈熸椂闂磋涓?timeout
     *
     * @param key
     * @param value
     * @param timeout
     *            杩囨湡鏃堕棿
     * @param unit
     *            鏃堕棿鍗曚綅, 澶?TimeUnit.DAYS 灏忔椂:TimeUnit.HOURS 鍒嗛挓:TimeUnit.MINUTES
     *            绉?TimeUnit.SECONDS 姣:TimeUnit.MILLISECONDS
     */
    public void setEx(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 鍙湁鍦?key 涓嶅瓨鍦ㄦ椂璁剧疆 key 鐨勫€?
     *
     * @param key
     * @param value
     * @return 涔嬪墠宸茬粡瀛樺湪杩斿洖false,涓嶅瓨鍦ㄨ繑鍥瀟rue
     */
    public boolean setIfAbsent(String key, String value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 鐢?value 鍙傛暟瑕嗗啓缁欏畾 key 鎵€鍌ㄥ瓨鐨勫瓧绗︿覆鍊硷紝浠庡亸绉婚噺 offset 寮€濮?
     *
     * @param key
     * @param value
     * @param offset
     *            浠庢寚瀹氫綅缃紑濮嬭鍐?
     */
    public void setRange(String key, String value, long offset) {
        stringRedisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * 鑾峰彇瀛楃涓茬殑闀垮害
     *
     * @param key
     * @return
     */
    public Long size(String key) {
        return stringRedisTemplate.opsForValue().size(key);
    }

    /**
     * 鎵归噺娣诲姞
     *
     * @param maps
     */
    public void multiSet(Map<String, String> maps) {
        stringRedisTemplate.opsForValue().multiSet(maps);
    }

    /**
     * 鍚屾椂璁剧疆涓€涓垨澶氫釜 key-value 瀵癸紝褰撲笖浠呭綋鎵€鏈夌粰瀹?key 閮戒笉瀛樺湪
     *
     * @param maps
     * @return 涔嬪墠宸茬粡瀛樺湪杩斿洖false,涓嶅瓨鍦ㄨ繑鍥瀟rue
     */
    public boolean multiSetIfAbsent(Map<String, String> maps) {
        return stringRedisTemplate.opsForValue().multiSetIfAbsent(maps);
    }

    /**
     * 澧炲姞(鑷闀?, 璐熸暟鍒欎负鑷噺
     *
     * @param key
     * @param
     * @return
     */
    public Long incrBy(String key, long increment) {
        return stringRedisTemplate.opsForValue().increment(key, increment);
    }

    /**
     *
     * @param key
     * @param
     * @return
     */
    public Double incrByFloat(String key, double increment) {
        return stringRedisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 杩藉姞鍒版湯灏?
     *
     * @param key
     * @param value
     * @return
     */
    public Integer append(String key, String value) {
        return stringRedisTemplate.opsForValue().append(key, value);
    }

    /** -------------------hash鐩稿叧鎿嶄綔------------------------- */

    /**
     * 鑾峰彇瀛樺偍鍦ㄥ搱甯岃〃涓寚瀹氬瓧娈电殑鍊?
     *
     * @param key
     * @param field
     * @return
     */
    public Object hGet(String key, String field) {
        return stringRedisTemplate.opsForHash().get(key, field);
    }

    /**
     * 鑾峰彇鎵€鏈夌粰瀹氬瓧娈电殑鍊?
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hGetAll(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 鑾峰彇鎵€鏈夌粰瀹氬瓧娈电殑鍊?
     *
     * @param key
     * @param fields
     * @return
     */
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return stringRedisTemplate.opsForHash().multiGet(key, fields);
    }

    public void hPut(String key, String hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void hPutAll(String key, Map<String, String> maps) {
        stringRedisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 浠呭綋hashKey涓嶅瓨鍦ㄦ椂鎵嶈缃?
     *
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    public Boolean hPutIfAbsent(String key, String hashKey, String value) {
        return stringRedisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 鍒犻櫎涓€涓垨澶氫釜鍝堝笇琛ㄥ瓧娈?
     *
     * @param key
     * @param fields
     * @return
     */
    public Long hDelete(String key, Object... fields) {
        return stringRedisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 鏌ョ湅鍝堝笇琛?key 涓紝鎸囧畾鐨勫瓧娈垫槸鍚﹀瓨鍦?
     *
     * @param key
     * @param field
     * @return
     */
    public boolean hExists(String key, String field) {
        return stringRedisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 涓哄搱甯岃〃 key 涓殑鎸囧畾瀛楁鐨勬暣鏁板€煎姞涓婂閲?increment
     *
     * @param key
     * @param field
     * @param increment
     * @return
     */
    public Long hIncrBy(String key, Object field, long increment) {
        return stringRedisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 涓哄搱甯岃〃 key 涓殑鎸囧畾瀛楁鐨勬暣鏁板€煎姞涓婂閲?increment
     *
     * @param key
     * @param field
     * @param delta
     * @return
     */
    public Double hIncrByFloat(String key, Object field, double delta) {
        return stringRedisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * 鑾峰彇鎵€鏈夊搱甯岃〃涓殑瀛楁
     *
     * @param key
     * @return
     */
    public Set<Object> hKeys(String key) {
        return stringRedisTemplate.opsForHash().keys(key);
    }

    /**
     * 鑾峰彇鍝堝笇琛ㄤ腑瀛楁鐨勬暟閲?
     *
     * @param key
     * @return
     */
    public Long hSize(String key) {
        return stringRedisTemplate.opsForHash().size(key);
    }

    /**
     * 鑾峰彇鍝堝笇琛ㄤ腑鎵€鏈夊€?
     *
     * @param key
     * @return
     */
    public List<Object> hValues(String key) {
        return stringRedisTemplate.opsForHash().values(key);
    }

    /**
     * 杩唬鍝堝笇琛ㄤ腑鐨勯敭鍊煎
     *
     * @param key
     * @param options
     * @return
     */
    public Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options) {
        return stringRedisTemplate.opsForHash().scan(key, options);
    }

    /** ------------------------list鐩稿叧鎿嶄綔---------------------------- */

    /**
     * 閫氳繃绱㈠紩鑾峰彇鍒楄〃涓殑鍏冪礌
     *
     * @param key
     * @param index
     * @return
     */
    public String lIndex(String key, long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

    /**
     * 鑾峰彇鍒楄〃鎸囧畾鑼冨洿鍐呯殑鍏冪礌
     *
     * @param key
     * @param start
     *            寮€濮嬩綅缃? 0鏄紑濮嬩綅缃?
     * @param end
     *            缁撴潫浣嶇疆, -1杩斿洖鎵€鏈?
     * @return
     */
    public List<String> lRange(String key, long start, long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 瀛樺偍鍦╨ist澶撮儴
     *
     * @param key
     * @param value
     * @return
     */
    public Long lLeftPush(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Long lLeftPushAll(String key, String... value) {
        return stringRedisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Long lLeftPushAll(String key, Collection<String> value) {
        return stringRedisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 褰搇ist瀛樺湪鐨勬椂鍊欐墠鍔犲叆
     *
     * @param key
     * @param value
     * @return
     */
    public Long lLeftPushIfPresent(String key, String value) {
        return stringRedisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 濡傛灉pivot瀛樺湪,鍐峱ivot鍓嶉潰娣诲姞
     *
     * @param key
     * @param pivot
     * @param value
     * @return
     */
    public Long lLeftPush(String key, String pivot, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Long lRightPush(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Long lRightPushAll(String key, String... value) {
        return stringRedisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Long lRightPushAll(String key, Collection<String> value) {
        return stringRedisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 涓哄凡瀛樺湪鐨勫垪琛ㄦ坊鍔犲€?
     *
     * @param key
     * @param value
     * @return
     */
    public Long lRightPushIfPresent(String key, String value) {
        return stringRedisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 鍦╬ivot鍏冪礌鐨勫彸杈规坊鍔犲€?
     *
     * @param key
     * @param pivot
     * @param value
     * @return
     */
    public Long lRightPush(String key, String pivot, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 閫氳繃绱㈠紩璁剧疆鍒楄〃鍏冪礌鐨勫€?
     *
     * @param key
     * @param index
     *            浣嶇疆
     * @param value
     */
    public void lSet(String key, long index, String value) {
        stringRedisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 绉诲嚭骞惰幏鍙栧垪琛ㄧ殑绗竴涓厓绱?
     *
     * @param key
     * @return 鍒犻櫎鐨勫厓绱?
     */
    public String lLeftPop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 绉诲嚭骞惰幏鍙栧垪琛ㄧ殑绗竴涓厓绱狅紝 濡傛灉鍒楄〃娌℃湁鍏冪礌浼氶樆濉炲垪琛ㄧ洿鍒扮瓑寰呰秴鏃舵垨鍙戠幇鍙脊鍑哄厓绱犱负姝?
     *
     * @param key
     * @param timeout
     *            绛夊緟鏃堕棿
     * @param unit
     *            鏃堕棿鍗曚綅
     * @return
     */
    public String lBLeftPop(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 绉婚櫎骞惰幏鍙栧垪琛ㄦ渶鍚庝竴涓厓绱?
     *
     * @param key
     * @return 鍒犻櫎鐨勫厓绱?
     */
    public String lRightPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 绉诲嚭骞惰幏鍙栧垪琛ㄧ殑鏈€鍚庝竴涓厓绱狅紝 濡傛灉鍒楄〃娌℃湁鍏冪礌浼氶樆濉炲垪琛ㄧ洿鍒扮瓑寰呰秴鏃舵垨鍙戠幇鍙脊鍑哄厓绱犱负姝?
     *
     * @param key
     * @param timeout
     *            绛夊緟鏃堕棿
     * @param unit
     *            鏃堕棿鍗曚綅
     * @return
     */
    public String lBRightPop(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 绉婚櫎鍒楄〃鐨勬渶鍚庝竴涓厓绱狅紝骞跺皢璇ュ厓绱犳坊鍔犲埌鍙︿竴涓垪琛ㄥ苟杩斿洖
     *
     * @param sourceKey
     * @param destinationKey
     * @return
     */
    public String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                destinationKey);
    }

    /**
     * 浠庡垪琛ㄤ腑寮瑰嚭涓€涓€硷紝灏嗗脊鍑虹殑鍏冪礌鎻掑叆鍒板彟澶栦竴涓垪琛ㄤ腑骞惰繑鍥炲畠锛?濡傛灉鍒楄〃娌℃湁鍏冪礌浼氶樆濉炲垪琛ㄧ洿鍒扮瓑寰呰秴鏃舵垨鍙戠幇鍙脊鍑哄厓绱犱负姝?
     *
     * @param sourceKey
     * @param destinationKey
     * @param timeout
     * @param unit
     * @return
     */
    public String lBRightPopAndLeftPush(String sourceKey, String destinationKey,
                                        long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                destinationKey, timeout, unit);
    }
    
    /**
     * 鍒犻櫎闆嗗悎涓€肩瓑浜巚alue寰楀厓绱?
     *
     * @param key
     * @param index
     *            index=0, 鍒犻櫎鎵€鏈夊€肩瓑浜巚alue鐨勫厓绱? index>0, 浠庡ご閮ㄥ紑濮嬪垹闄ょ涓€涓€肩瓑浜巚alue鐨勫厓绱?
     *            index<0, 浠庡熬閮ㄥ紑濮嬪垹闄ょ涓€涓€肩瓑浜巚alue鐨勫厓绱?
     * @param value
     * @return
     */
    public Long lRemove(String key, long index, String value) {
        return stringRedisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * 瑁佸壀list
     *
     * @param key
     * @param start
     * @param end
     */
    public void lTrim(String key, long start, long end) {
        stringRedisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 鑾峰彇鍒楄〃闀垮害
     *
     * @param key
     * @return
     */
    public Long lLen(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }


    /** --------------------set鐩稿叧鎿嶄綔-------------------------- */

    /**
     * set娣诲姞鍏冪礌
     *
     * @param key
     * @param values
     * @return
     */
    public Long sAdd(String key, String... values) {
        return stringRedisTemplate.opsForSet().add(key, values);
    }

    /**
     * set绉婚櫎鍏冪礌
     *
     * @param key
     * @param values
     * @return
     */
    public Long sRemove(String key, Object... values) {
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 绉婚櫎骞惰繑鍥為泦鍚堢殑涓€涓殢鏈哄厓绱?
     *
     * @param key
     * @return
     */
    public String sPop(String key) {
        return stringRedisTemplate.opsForSet().pop(key);
    }

    /**
     * 灏嗗厓绱爒alue浠庝竴涓泦鍚堢Щ鍒板彟涓€涓泦鍚?
     *
     * @param key
     * @param value
     * @param destKey
     * @return
     */
    public Boolean sMove(String key, String value, String destKey) {
        return stringRedisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 鑾峰彇闆嗗悎鐨勫ぇ灏?
     *
     * @param key
     * @return
     */
    public Long sSize(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 鍒ゆ柇闆嗗悎鏄惁鍖呭惈value
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean sIsMember(String key, Object value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 鑾峰彇涓や釜闆嗗悎鐨勪氦闆?
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<String> sIntersect(String key, String otherKey) {
        return stringRedisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 鑾峰彇key闆嗗悎涓庡涓泦鍚堢殑浜ら泦
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<String> sIntersect(String key, Collection<String> otherKeys) {
        return stringRedisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * key闆嗗悎涓巓therKey闆嗗悎鐨勪氦闆嗗瓨鍌ㄥ埌destKey闆嗗悎涓?
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * key闆嗗悎涓庡涓泦鍚堢殑浜ら泦瀛樺偍鍒癲estKey闆嗗悎涓?
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys,
                                   String destKey) {
        return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 鑾峰彇涓や釜闆嗗悎鐨勫苟闆?
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<String> sUnion(String key, String otherKeys) {
        return stringRedisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 鑾峰彇key闆嗗悎涓庡涓泦鍚堢殑骞堕泦
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<String> sUnion(String key, Collection<String> otherKeys) {
        return stringRedisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * key闆嗗悎涓巓therKey闆嗗悎鐨勫苟闆嗗瓨鍌ㄥ埌destKey涓?
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key闆嗗悎涓庡涓泦鍚堢殑骞堕泦瀛樺偍鍒癲estKey涓?
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys,
                               String destKey) {
        return stringRedisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 鑾峰彇涓や釜闆嗗悎鐨勫樊闆?
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<String> sDifference(String key, String otherKey) {
        return stringRedisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 鑾峰彇key闆嗗悎涓庡涓泦鍚堢殑宸泦
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<String> sDifference(String key, Collection<String> otherKeys) {
        return stringRedisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * key闆嗗悎涓巓therKey闆嗗悎鐨勫樊闆嗗瓨鍌ㄥ埌destKey涓?
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long sDifference(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKey,
                destKey);
    }

    /**
     * key闆嗗悎涓庡涓泦鍚堢殑宸泦瀛樺偍鍒癲estKey涓?
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long sDifference(String key, Collection<String> otherKeys,
                            String destKey) {
        return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 鑾峰彇闆嗗悎鎵€鏈夊厓绱?
     *
     * @param key
     * @param
     * @param
     * @return
     */
    public Set<String> setMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 闅忔満鑾峰彇闆嗗悎涓殑涓€涓厓绱?
     *
     * @param key
     * @return
     */
    public String sRandomMember(String key) {
        return stringRedisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 闅忔満鑾峰彇闆嗗悎涓璫ount涓厓绱?
     *
     * @param key
     * @param count
     * @return
     */
    public List<String> sRandomMembers(String key, long count) {
        return stringRedisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 闅忔満鑾峰彇闆嗗悎涓璫ount涓厓绱犲苟涓斿幓闄ら噸澶嶇殑
     *
     * @param key
     * @param count
     * @return
     */
    public Set<String> sDistinctRandomMembers(String key, long count) {
        return stringRedisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     *
     * @param key
     * @param options
     * @return
     */
    public Cursor<String> sScan(String key, ScanOptions options) {
        return stringRedisTemplate.opsForSet().scan(key, options);
    }

    /**------------------zSet鐩稿叧鎿嶄綔--------------------------------*/

    /**
     * 娣诲姞鍏冪礌,鏈夊簭闆嗗悎鏄寜鐓у厓绱犵殑score鍊肩敱灏忓埌澶ф帓鍒?
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     *
     * @param key
     * @param values
     * @return
     */
    public Long zAdd(String key, Set<TypedTuple<String>> values) {
        return stringRedisTemplate.opsForZSet().add(key, values);
    }

    /**
     *
     * @param key
     * @param values
     * @return
     */
    public Long zRemove(String key, Object... values) {
        return stringRedisTemplate.opsForZSet().remove(key, values);
    }

    public Long zRemove(String key, Collection<String> values) {
        if(values!=null&&!values.isEmpty()){
            Object[] objs = values.toArray(new Object[values.size()]);
            return stringRedisTemplate.opsForZSet().remove(key, objs);
        }
       return 0L;
    }

    /**
     * 澧炲姞鍏冪礌鐨剆core鍊硷紝骞惰繑鍥炲鍔犲悗鐨勫€?
     *
     * @param key
     * @param value
     * @param delta
     * @return
     */
    public Double zIncrementScore(String key, String value, double delta) {
        return stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 杩斿洖鍏冪礌鍦ㄩ泦鍚堢殑鎺掑悕,鏈夊簭闆嗗悎鏄寜鐓у厓绱犵殑score鍊肩敱灏忓埌澶ф帓鍒?
     *
     * @param key
     * @param value
     * @return 0琛ㄧず绗竴浣?
     */
    public Long zRank(String key, Object value) {
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 杩斿洖鍏冪礌鍦ㄩ泦鍚堢殑鎺掑悕,鎸夊厓绱犵殑score鍊肩敱澶у埌灏忔帓鍒?
     *
     * @param key
     * @param value
     * @return
     */
    public Long zReverseRank(String key, Object value) {
        return stringRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 鑾峰彇闆嗗悎鐨勫厓绱? 浠庡皬鍒板ぇ鎺掑簭
     *
     * @param key
     * @param start
     *            寮€濮嬩綅缃?
     * @param end
     *            缁撴潫浣嶇疆, -1鏌ヨ鎵€鏈?
     * @return
     */
    public Set<String> zRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }
    
    /**
     * 鑾峰彇zset闆嗗悎鐨勬墍鏈夊厓绱? 浠庡皬鍒板ぇ鎺掑簭
     *
     */
    public Set<String> zRangeAll(String key) {
        return zRange(key,0,-1);
    }

    /**
     * 鑾峰彇闆嗗悎鍏冪礌, 骞朵笖鎶妔core鍊间篃鑾峰彇
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<TypedTuple<String>> zRangeWithScores(String key, long start,
                                                    long end) {
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 鏍规嵁Score鍊兼煡璇㈤泦鍚堝厓绱?
     *
     * @param key
     * @param min
     *            鏈€灏忓€?
     * @param max
     *            鏈€澶у€?
     * @return
     */
    public Set<String> zRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }


    /**
     * 鏍规嵁Score鍊兼煡璇㈤泦鍚堝厓绱? 浠庡皬鍒板ぇ鎺掑簭
     *
     * @param key
     * @param min
     *            鏈€灏忓€?
     * @param max
     *            鏈€澶у€?
     * @return
     */
    public Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                           double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @param start
     * @param end
     * @return
     */
    public Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                           double min, double max, long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,
                start, end);
    }

    /**
     * 鑾峰彇闆嗗悎鐨勫厓绱? 浠庡ぇ鍒板皬鎺掑簭
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zReverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);

    }

    public Set<String> zReverseRangeByScore(String key, long min, long max) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);

    }

    /**
     * 鑾峰彇闆嗗悎鐨勫厓绱? 浠庡ぇ鍒板皬鎺掑簭, 骞惰繑鍥瀞core鍊?
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<TypedTuple<String>> zReverseRangeWithScores(String key,
                                                           long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start,
                end);
    }

    /**
     * 鏍规嵁Score鍊兼煡璇㈤泦鍚堝厓绱? 浠庡ぇ鍒板皬鎺掑簭
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zReverseRangeByScore(String key, double min,
                                            double max) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 鏍规嵁Score鍊兼煡璇㈤泦鍚堝厓绱? 浠庡ぇ鍒板皬鎺掑簭
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<TypedTuple<String>> zReverseRangeByScoreWithScores(
            String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
                min, max);
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @param start
     * @param end
     * @return
     */
    public Set<String> zReverseRangeByScore(String key, double min,
                                            double max, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max,
                start, end);
    }

    /**
     * 鏍规嵁score鍊艰幏鍙栭泦鍚堝厓绱犳暟閲?
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zCount(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 鑾峰彇闆嗗悎澶у皬
     *
     * @param key
     * @return
     */
    public Long zSize(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    /**
     * 鑾峰彇闆嗗悎澶у皬
     *
     * @param key
     * @return
     */
    public Long zZCard(String key) {
        return stringRedisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 鑾峰彇闆嗗悎涓璿alue鍏冪礌鐨剆core鍊?
     *
     * @param key
     * @param value
     * @return
     */
    public Double zScore(String key, Object value) {
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 绉婚櫎鎸囧畾绱㈠紩浣嶇疆鐨勬垚鍛?
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zRemoveRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 鏍规嵁鎸囧畾鐨剆core鍊肩殑鑼冨洿鏉ョЩ闄ゆ垚鍛?
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 鑾峰彇key鍜宱therKey鐨勫苟闆嗗苟瀛樺偍鍦╠estKey涓?
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long zUnionAndStore(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long zUnionAndStore(String key, Collection<String> otherKeys,
                               String destKey) {
        return stringRedisTemplate.opsForZSet()
                .unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 浜ら泦
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long zIntersectAndStore(String key, String otherKey,
                                   String destKey) {
        return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * 浜ら泦
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long zIntersectAndStore(String key, Collection<String> otherKeys,
                                   String destKey) {
        return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     *
     * @param key
     * @param options
     * @return
     */
    public Cursor<TypedTuple<String>> zScan(String key, ScanOptions options) {
        return stringRedisTemplate.opsForZSet().scan(key, options);
    }

    /**
     * 鎵弿涓婚敭锛屽缓璁娇鐢?
     * @param patten
     * @return
     */
    public Set<String> scan(String patten){
        Set<String> keys = stringRedisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> result = new HashSet<>();
            try (Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                    .match(patten).count(10000).build())) {
                while (cursor.hasNext()) {
                    result.add(new String(cursor.next()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        });
        return  keys;
    }
    
    /**
     * 绠￠亾鎶€鏈紝鎻愰珮鎬ц兘
     * @param type
     * @param values
     * @return
     */
    public List<Object> lRightPushPipeline(String type,Collection<String> values){
        List<Object> results = stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
                        //闆嗗悎杞崲鏁扮粍
                        String[] strings = values.toArray(new String[values.size()]);
                        //鐩存帴鎵归噺鍙戦€?
                        stringRedisConn.rPush(type, strings);
                        return null;
                    }
                });
        return results;
    }

    public List<Object> refreshWithPipeline(String future_key,String topic_key,Collection<String> values){

        List<Object> objects = stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Nullable
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection)redisConnection;
                String[] strings = values.toArray(new String[values.size()]);
                stringRedisConnection.lPush(topic_key,strings);
                stringRedisConnection.zRem(future_key,strings);
                return null;
            }
        });
        return objects;
    }

    /**
     * 鍔犻攣
     *
     * @param name
     * @param expire
     * @return
     */
    public String tryLock(String name, long expire) {
        name = name + "_lock";
        String token = UUID.randomUUID().toString();
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        RedisConnection conn = factory.getConnection();
        try {

            //鍙傝€價edis鍛戒护锛?
            //set key value [EX seconds] [PX milliseconds] [NX|XX]
            Boolean result = conn.set(
                    name.getBytes(),
                    token.getBytes(),
                    Expiration.from(expire, TimeUnit.MILLISECONDS),
                    RedisStringCommands.SetOption.SET_IF_ABSENT //NX
            );
            if (result != null && result)
                return token;
        } finally {
            RedisConnectionUtils.releaseConnection(conn, factory,false);
        }
        return null;
    }
}
