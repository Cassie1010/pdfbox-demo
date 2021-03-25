package org.apache;

/**
 * @author: zmm
 * @time: 2021/3/23 14:00
 */
public class Extension {

    private String oid;

    private boolean critical;

    private byte[] value;

    public String getOid() {
        return oid;
    }

    public byte[] getValue() {
        return value;
    }
    public boolean isCritical() {
        return critical;
    }
}
