package com.task.flow.repository;

import com.task.flow.domain.CustomExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CustomExtensionRepository extends JpaRepository<CustomExtension, Long> {
    boolean existsByName(String extension);

    @Modifying
    @Query("DELETE FROM CustomExtension c WHERE c.name IN :names")
    void deleteByNameIn(Set<String> names);
}
