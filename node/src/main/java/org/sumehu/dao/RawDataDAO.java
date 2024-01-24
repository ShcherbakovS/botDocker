package org.sumehu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sumehu.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
