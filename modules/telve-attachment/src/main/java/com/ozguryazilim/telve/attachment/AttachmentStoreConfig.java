package com.ozguryazilim.telve.attachment;

/**
 *
 * @author oyas
 */
public interface AttachmentStoreConfig {

    boolean canSupportFeature( String featureName, String storeName );
    
}
