package com.task.flow.repository;

import com.task.flow.domain.FixedExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FixedExtensionRepository extends JpaRepository<FixedExtension, Long> {
    @Query("SELECT COUNT(f) FROM FixedExtension f WHERE f.isChecked = true AND f.name = :extension")
    int existsByNameAndIsChecked(String extension);
}
