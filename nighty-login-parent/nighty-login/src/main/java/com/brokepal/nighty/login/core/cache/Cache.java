package com.brokepal.nighty.login.core.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.Serializable;

/**
 * Created by chenchao on 17/3/28.
 */
public class Cache {
    private static CacheManager manager = CacheManager.create();
    private static net.sf.ehcache.Cache cache = manager.getCache("commonCache");
    private static net.sf.ehcache.Cache longCache = manager.getCache("longTimeCache");

    private Cache() {}

    public static <K extends Serializable,V extends Serializable> void put(K k, V v){
        Element element = new Element(k,v);
        cache.put(element);
        longCache.remove(k);
    }

    /** 长时间缓存 */
    public static <K extends Serializable,V extends Serializable> void longPut(K k, V v){
        Element element = new Element(k,v);
        longCache.put(element);
        cache.remove(k);
    }

    public static <K extends Serializable, V> V get(K k){
        Element element;
        V result = null;
        do {
            element = cache.get(k);
            if (element != null){
                result = (V) element.getValue();
                break;
            }
            element = longCache.get(k);
            if (element != null){
                result = (V) element.getValue();
                break;
            }
        } while (false);
        return result;
    }

    public static <K extends Serializable> boolean has(K k){
        Element element = cache.get(k);
        boolean result = false;
        do {
            element = cache.get(k);
            if (element != null){
                result = true;
                break;
            }
            element = longCache.get(k);
            if (element != null){
                result = true;
                break;
            }
        } while (false);
        return result;
    }

    public static <K extends Serializable> void remove(K k){
        cache.remove(k);
        longCache.remove(k);
    }



    public static <K extends Serializable,V extends Serializable> void put(String pre, String key, V v){
        key = pre + "-" + key;
        Element element = new Element(key,v);
        cache.put(element);
        longCache.remove(key);
    }

    /** 长时间缓存 */
    public static <K extends Serializable,V extends Serializable> void longPut(String pre, String key, V v){
        key = pre + "-" + key;
        Element element = new Element(key,v);
        longCache.put(element);
        cache.remove(key);
    }

    public static <K extends Serializable, V> V get(String pre, String key){
        key = pre + "-" + key;
        Element element;
        V result = null;
        do {
            element = cache.get(key);
            if (element != null){
                result = (V) element.getValue();
                break;
            }
            element = longCache.get(key);
            if (element != null){
                result = (V) element.getValue();
                break;
            }
        } while (false);
        return result;
    }

    public static <K extends Serializable> boolean has(String pre, String key){
        key = pre + "-" + key;
        Element element = cache.get(key);
        boolean result = false;
        do {
            element = cache.get(key);
            if (element != null){
                result = true;
                break;
            }
            element = longCache.get(key);
            if (element != null){
                result = true;
                break;
            }
        } while (false);
        return result;
    }

    public static <K extends Serializable> void remove(String pre, String key){
        key = pre + "-" + key;
        cache.remove(key);
        longCache.remove(key);
    }
}
