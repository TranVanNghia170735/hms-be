package com.hms.media.repository;

import com.hms.media.entity.MediaFile;
import org.springframework.data.repository.CrudRepository;

public interface MediaFileRepository extends CrudRepository<MediaFile, Long> {
}
