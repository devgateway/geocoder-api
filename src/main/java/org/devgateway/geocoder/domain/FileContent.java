package org.devgateway.geocoder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author idobre
 * @since 18/01/2018
 *
 * Entity used to store the content of uploaded files.
 */

@Entity
public class FileContent extends GenericPersistable {
    private static final int LOB_LENGTH = 10000000;

    @Lob
    @Column(length = LOB_LENGTH)
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }
}
