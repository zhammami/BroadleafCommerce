/*
 * #%L
 * BroadleafCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
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
/**
 *
 */

package org.broadleafcommerce.common.config;

import org.broadleafcommerce.common.extensibility.jpa.convert.BroadleafClassTransformer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.lang.instrument.ClassFileTransformer;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Main configuration class for the broadleaf-common module
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Configuration
public class BroadleafCommonConfig implements LoadTimeWeaverAware {

    LoadTimeWeaver loadTimeWeaver;

    @Resource(name = "blMergedClassTransformers")
    protected Set<BroadleafClassTransformer> mergedClassTransformers;

    /**
     * Other enterprise/mulititenant modules override this adapter to provide one that supports dynamic filtration
     */
    @Bean
    @ConditionalOnMissingBean(name = "blJpaVendorAdapter")
    public JpaVendorAdapter blJpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return vendorAdapter;
    }

    @Override
    public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
        this.loadTimeWeaver = loadTimeWeaver;
    }

    @PostConstruct
    public void postConstruct() {
        for (BroadleafClassTransformer transformer : mergedClassTransformers) {
            loadTimeWeaver.addTransformer((ClassFileTransformer) transformer);
        }
    }
}
