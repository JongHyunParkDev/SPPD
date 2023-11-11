package com.dev.was.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name="tm_survey")
public class SurveyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본키
    @Column(nullable = false)
    private Long userId; // userEntity id

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isActive;

    @BatchSize(size = 1000)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SurveyUserEntity> surveyUserEntityList;

    @BatchSize(size = 100)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SurveyDetailEntity> surveyDetailEntityList;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public SurveyEntity(Long id, Long userId, String title, LocalDate startDate, LocalDate endDate, boolean isActive, List<SurveyUserEntity> surveyUserEntityList, List<SurveyDetailEntity> surveyDetailEntityList, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.surveyUserEntityList = surveyUserEntityList;
        this.surveyDetailEntityList = surveyDetailEntityList;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}