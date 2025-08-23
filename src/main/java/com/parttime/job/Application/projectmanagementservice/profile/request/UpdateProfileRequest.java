package com.parttime.job.Application.projectmanagementservice.profile.request;

import com.parttime.job.Application.projectmanagementservice.profile.enumration.Gender;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdateProfileRequest {
    // Update profile
    @Size(max = 20, message = "Nick name must have less than 20 char")
    private String nickName;
    @Size(max = 20, message = "Nick name must have less than 20 char")
    private String fullName;
    @Pattern(regexp = "\\d{10}", message = "Number phone must have ten number")
    private String phoneNumber;
    private Date dateOfBirth;
    private String avatar;
    private Gender gender;

    // Update Address
    private List<UpdateAddressRequest> addresses;
    // Update Information

    @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?facebook\\.com\\/.*$",
            message = "Facebook link must have URL valid and in facebook.com")
    private String facebook;

    @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?instagram\\.com\\/.*$",
            message = "Instagram link must have URL valid and in instagram.com")
    private String instagram;

    @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?tiktok\\.com\\/.*$",
            message = "TikTok link must have URL valid and in tiktok.com")
    private String tiktok;

    @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?zalo\\.me\\/.*$",
            message = "Zalo link must have URL valid and in zalo.me")
    private String zalo;

    @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?twitter\\.com\\/.*$",
            message = "Twitter link must have URL valid and in twitter.com")
    private String twitter;


}
