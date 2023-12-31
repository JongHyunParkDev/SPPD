package com.dev.was.repository;

import com.dev.was.entity.SurveyUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyUserRepository extends JpaRepository<SurveyUserEntity, Long> {
    List<SurveyUserEntity> findBySurveyEntityId(Long surveyId);
}
