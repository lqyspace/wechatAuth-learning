package com.bw.iot.tbc.wechatdemo.provider.utils.ehcache;


import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @ClassName EhcacheUtils
 * @Description ehcache工具类
 * @Author lengqy
 * @Date 2024年12月08日 02:35
 * @Version 1.0
 */
@Slf4j
public class EhcacheUtil {
    private static CacheManager cacheManager;
    private static Cache cache;

    static {
        // 初始化CacheManager 和 Cache
        try {
            // CacheManager.create() 方法重用已有的 CacheManager 实例
            cacheManager = CacheManager.create(EhcacheUtil.class.getClassLoader().getResourceAsStream("ehcache.xml"));
            cache = cacheManager.getCache("WxCpTpConfigCache");
        } catch (Exception e) {
            log.info("EhcacheUtil - 发生异常：e:", e);
        }
    }

    // 添加缓存
    public static void put(String key, Object value) {
        if (cache == null || key == null) {
            return;
        }
        log.info("EhcacheUtil - 添加缓存，key:{},value:{}", key, value);
        Element el = new Element(key, value);
        cache.put(el);
    }

    // 获取缓存
    public static Object get(String key) {
        if (cache == null || key == null)
            return null;
        if (!cache.isKeyInCache(key) || cache.get(key).isExpired() || cache.get(key) == null)
            return null;
        log.info("EhcacheUtil - 获取缓存，key:{},value:{}", key, cache.get(key).getObjectValue());
        return cache.get(key).getObjectValue();
    }

    // 更新缓存
    public static void update(String key, Object value) {
        if (cache == null || key == null)
            return;
        Element el = cache.get(key);
        log.info("EhcacheUtil - 更新缓存，key:{},value:{}", key, value);
        if (el != null) {
            Element newEl = new Element(key, value);
            cache.put(newEl);
        } else {
            put(key, value);
        }
    }

    // 删除并返回元素
    public static Object remove(String key) {
        if (cache == null || key == null)
            return null;
        Object val = get(key);
        log.info("EhcacheUtil - 删除并返回元素，key:{},value:{}", key, val);
        cache.remove(key);
        return val;
    }

    // 清空所有缓存
    public static void clear() {
        if (cache == null)
            return;
        log.info("EhcacheUtil - 清空所有缓存");
        cache.removeAll();
    }

    // 关闭缓存管理器
    public static void shutdown() {
        log.info("EhcacheUtil - 关闭缓存管理器");
        if (cacheManager != null)
            cacheManager.shutdown();
    }

    // 获取缓存大小
    public static int size() {
        log.info("EhcacheUtil - 获取缓存大小");
        if (cache == null)
            return 0;
        return cache.getSize();
    }
}
