package com.major.revalida.profile.student;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String userNo;
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
    private String photo;
}


//{
//    "userNo": "234",
//    "programId": 1,
//    "subjectId": 2,
//    "firstname": "John",
//    "middlename": "Doe",
//    "lastname": "Smith",
//    "birthdate": "1998-06-01",
//    "sem": 1,
//    "yearlevel": 2,
//    "academicYear": "2022-2023",
//    "professorId": 2001,
//    "email": "john.smith@example.com",
//    "photos": "http://example.com/john_smith.jpg"
//}
