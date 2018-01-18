package org.devgateway.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author idobre
 * @since 18/01/2018
 *
 * Entity used to store the metadata of uploaded files.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "name")})
public class FileMetadata extends GenericPersistable {
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private FileContent content;

    @Size(max = 255)
    private String name;

    @JsonIgnore
    private String contentType;

    @JsonIgnore
    private long size;

    public FileContent getContent() {
        return content;
    }

    public void setContent(final FileContent content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(final long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return name;
    }
}
