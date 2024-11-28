package com.wywin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDTO {

    @JsonProperty("id")
    public Long id;

    //서비스에 연결 완료된 시각. UTC
    @JsonProperty("connected_at")
    public Date connectedAt;

    //사용자 프로퍼티
    @JsonProperty("properties")
    public HashMap<String, String> properties;

    //카카오 계정 정보
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    //uuid 등 추가 정보
    @JsonProperty("for_partner")
    public Partner partner;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {

        //프로필 정보 제공 동의 여부
        @JsonProperty("profile_needs_agreement")
        public Boolean isProfileAgree;

        //닉네임 제공 동의 여부
        @JsonProperty("profile_nickname_needs_agreement")
        public Boolean isNickNameAgree;

        //프로필 사진 제공 동의 여부
        @JsonProperty("profile_image_needs_agreement")
        public Boolean isProfileImageAgree;

        //이메일 제공 동의 여부
        @JsonProperty("email_needs_agreement")
        public Boolean isEmailAgree;

        //이메일이 유효 여부
        // true : 유효한 이메일, false : 이메일이 다른 카카오 계정에 사용돼 만료
        @JsonProperty("is_email_valid")
        public Boolean isEmailValid;

        //이메일이 인증 여부
        //true : 인증된 이메일, false : 인증되지 않은 이메일
        @JsonProperty("is_email_verified")
        public Boolean isEmailVerified;

        //카카오계정 대표 이메일
        @JsonProperty("email")
        public String email;

    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Profile {

        //닉네임
        @JsonProperty("nickname")
        public String nickName;

        //프로필 미리보기 이미지 URL
        @JsonProperty("thumbnail_image_url")
        public String thumbnailImageUrl;

        //프로필 사진 URL
        @JsonProperty("profile_image_url")
        public String profileImageUrl;

        //프로필 사진 URL 기본 프로필인지 여부
        //true : 기본 프로필, false : 사용자 등록
        @JsonProperty("is_default_image")
        public String isDefaultImage;

        //닉네임이 기본 닉네임인지 여부
        //true : 기본 닉네임, false : 사용자 등록
        @JsonProperty("is_default_nickname")
        public Boolean isDefaultNickName;

    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Partner {
        //고유 ID
        @JsonProperty("uuid")
        public String uuid;
    }
}



