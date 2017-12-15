package org.devgateway.geocoder.service;

import net.sf.ehcache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @author idobre
 * @since 14/12/2017
 */
@Service
public class CacheService {

    /**
     * Clear all ehcache cache.
     */
    public void clearAllCache() {
        CacheManager cm = CacheManager.getInstance();
        cm.clearAll();
    }
}
