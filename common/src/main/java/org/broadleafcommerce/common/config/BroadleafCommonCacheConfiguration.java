/*
 * #%L
 * BroadleafCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2019 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.common.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

import javax.cache.Caching;
import javax.cache.configuration.Configuration;

@org.springframework.context.annotation.Configuration
@EnableCaching
public class BroadleafCommonCacheConfiguration {

    // Cache manager configuration

//    @Bean
//    public JCacheCacheManager blSpringCacheManager() {
//        return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager());
//    }

    // Cache configuration

    public static Configuration<Object, Object> buildCacheConfiguration(long heapEntries,
                                                                        Duration timeToLive) {
        CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        Object.class, Object.class,
                        ResourcePoolsBuilder.heap(heapEntries))
                .withExpiry(timeToLive == null ? ExpiryPolicyBuilder.noExpiration() :
                            ExpiryPolicyBuilder.timeToLiveExpiration(timeToLive))
                .build();

        return Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
    }

    // One day hydrated caches

    @Bean
    public Configuration<Object, Object> blDefaultHydratedCacheConfiguration() {
        // TODO 6.1 ehcache 3 should we configure the hydrated event listener here?
        return buildCacheConfiguration(100000, Duration.ofDays(1));
    }

    @Bean
    public JCacheManagerCustomizer blStandardElementsCacheCustomizer(
            final Configuration<?, ?> blDefaultHydratedCacheConfiguration) {
        return cacheManager -> cacheManager.createCache("blStandardElements", 
                blDefaultHydratedCacheConfiguration);
    }

    @Bean
    public JCacheManagerCustomizer blProductsCacheCustomizer(
            final Configuration<?, ?> blDefaultHydratedCacheConfiguration) {
        return cacheManager -> cacheManager.createCache("blProducts", 
                blDefaultHydratedCacheConfiguration);
    }

    @Bean
    public JCacheManagerCustomizer blCategoriesCacheCustomizer(
            final Configuration<?, ?> blDefaultHydratedCacheConfiguration) {
        return cacheManager -> cacheManager.createCache("blCategories", 
                blDefaultHydratedCacheConfiguration);
    }

    @Bean
    public JCacheManagerCustomizer blOffersCacheCustomizer(
            final Configuration<?, ?> blDefaultHydratedCacheConfiguration) {
        return cacheManager -> cacheManager.createCache("blOffers",
                blDefaultHydratedCacheConfiguration);
    }

    // One day caches

    @Bean
    public JCacheManagerCustomizer blSiteElementsQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blSiteElementsQuery",
                buildCacheConfiguration(1000, Duration.ofDays(1)));
    }

    @Bean
    public JCacheManagerCustomizer blBundleElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blBundleElements",
                buildCacheConfiguration(1000, Duration.ofDays(1)));
    }

    @Bean
    public JCacheManagerCustomizer blResourceCacheElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blResourceCacheElements",
                buildCacheConfiguration(1000, Duration.ofDays(1)));
    }

    @Bean
    public JCacheManagerCustomizer blResourceTransformerCacheElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blResourceTransformerCacheElements",
                buildCacheConfiguration(1000, Duration.ofDays(1)));
    }

    @Bean
    public JCacheManagerCustomizer blSecurityElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blSecurityElements",
                buildCacheConfiguration(1000, Duration.ofDays(1)));
    }

    @Bean
    public JCacheManagerCustomizer blSiteElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blSiteElements",
                buildCacheConfiguration(5000, Duration.ofDays(1)));
    }

    // One hour caches

    @Bean
    public JCacheManagerCustomizer blProductUrlCacheCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blProductUrlCache",
                buildCacheConfiguration(1000, Duration.ofHours(1)));
    }

    @Bean
    public JCacheManagerCustomizer blCategoryUrlCacheCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blCategoryUrlCache",
                buildCacheConfiguration(1000, Duration.ofHours(1)));
    }

    @Bean
    public JCacheManagerCustomizer blTemplateElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blTemplateElements",
                buildCacheConfiguration(1000, Duration.ofHours(1)));
    }

    @Bean
    public JCacheManagerCustomizer blTranslationElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blTranslationElements",
                buildCacheConfiguration(10000000, Duration.ofHours(1)));
    }

    // Ten minute caches

    @Bean
    public JCacheManagerCustomizer blStandardQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("org.hibernate.cache.internal.StandardQueryCache",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blCatalogQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.Catalog",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blPriceListQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.PriceList",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blCmsQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.Cms",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blOfferQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.Offer",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blOrderQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.Order",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blSearchQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.Search",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blConfigurationModuleElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blConfigurationModuleElements",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blConfigurationModuleElementsQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.ConfigurationModuleElements",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blSystemPropertyElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blSystemPropertyElements",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blSystemPropertyNullCheckCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blSystemPropertyNullCheckCache",
                buildCacheConfiguration(1000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blGeneratedResourceCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("generatedResourceCache",
                buildCacheConfiguration(100, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blOrderElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blOrderElements",
                buildCacheConfiguration(100000, Duration.ofMinutes(10)));
    }

    @Bean
    public JCacheManagerCustomizer blCustomerElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blCustomerElements",
                buildCacheConfiguration(100000, Duration.ofMinutes(10)));
    }

    // No expiry caches

    @Bean
    public JCacheManagerCustomizer blUpdateTimestampsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("org.hibernate.cache.spi.UpdateTimestampsCache",
                buildCacheConfiguration(5000, null));
    }

    @Bean
    public JCacheManagerCustomizer blBatchTranslationCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blBatchTranslationCache",
                buildCacheConfiguration(10000, null));
    }

    // Other expiry

    @Bean
    public JCacheManagerCustomizer blInventoryElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blInventoryElements",
                buildCacheConfiguration(100000, Duration.ofMinutes(1)));
    }

    @Bean
    public JCacheManagerCustomizer blSandBoxElementsCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("blSandBoxElements",
                buildCacheConfiguration(2000, Duration.ofSeconds(3)));
    }

    @Bean
    public JCacheManagerCustomizer blSandBoxElementsQueryCacheCustomizer() {
        return cacheManager -> cacheManager.createCache("query.blSandBoxElements",
                buildCacheConfiguration(500, Duration.ofSeconds(3)));
    }

}
