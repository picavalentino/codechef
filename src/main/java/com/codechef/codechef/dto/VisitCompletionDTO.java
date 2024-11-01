package com.codechef.codechef.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitCompletionDTO {
    private Long memNo;
    private Date reservationDate;
    private Boolean reviewOx;
    private Boolean visitOx;
    private Long chefNo;
    private String resName;
    private String mainImage;
    private String reviewRating;
}

