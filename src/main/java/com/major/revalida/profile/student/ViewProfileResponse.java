package com.major.revalida.profile.student;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ViewProfileResponse {
    private String studentNo;
    private Integer programId;
    private Integer subjectId;
    private String firstname;
    private String middlename;
    private String lastname;
    private LocalDate birthdate;
    private Integer sem;
    private Integer yearlevel;
    private String academicYear;
    private Integer professorId;
    private String email;
    private String Photo;
}