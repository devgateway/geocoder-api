package org.devgateway.geocoder.repositories;

import org.devgateway.geocoder.domain.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 18/01/2018
 */
@Repository
@Transactional
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

}
