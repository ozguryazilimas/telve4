package com.ozguryazilim.mutfak.kahve;

/**
 *
 * @author haky
 */
public enum TestKahveKey implements KahveKey{

    Test1( "TEST-KEY1", "TEST-VALUE1" ),
    Test2( "TEST-KEY2", "TEST-VALUE2" ),
    ;

    private String key;
    private String defaultValue;

    private TestKahveKey() {
    }

    private TestKahveKey(String key) {
        this.key = key;
    }

    private TestKahveKey(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

}
