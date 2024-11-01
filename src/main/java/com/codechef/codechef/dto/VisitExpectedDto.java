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
public class VisitExpectedDto {
    private Long reservationNo;
    private Long chefNo;
    private Date reservationDate;
    private String bannerImage;
    private String resName;
    private String address;

}
