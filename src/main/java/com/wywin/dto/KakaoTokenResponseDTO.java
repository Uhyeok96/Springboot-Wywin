package com.wywin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor  // Lombok 어노테이션으로, 기본 생성자(매개변수 없는 생성자)를 자동으로 생성.
@JsonIgnoreProperties(ignoreUnknown = true)  // Jackson 어노테이션으로, 응답에서 알 수 없는 속성은 무시하도록 설정.
public class KakaoTokenResponseDTO {

    // 카카오 API에서 반환된 토큰의 타입 (예: "bearer").
    @JsonProperty("token_type")
    public String tokenType;

    // API 요청을 인증할 때 사용할 수 있는 액세스 토큰.
    @JsonProperty("access_token")
    public String accessToken;

    // 사용자 인증에 사용되는 ID 토큰, 일반적으로 OpenID Connect에서 사용.
    @JsonProperty("id_token")
    public String idToken;

    // 액세스 토큰이 만료될 때까지의 시간(초 단위).
    @JsonProperty("expires_in")
    public Integer expiresIn;

    // 액세스 토큰이 만료된 후, 새로운 토큰을 발급받을 때 사용하는 리프레시 토큰.
    @JsonProperty("refresh_token")
    public String refreshToken;

    // 리프레시 토큰이 만료될 때까지의 시간(초 단위).
    @JsonProperty("refresh_token_expires_in")
    public Integer refreshTokenExpiresIn;

    // 액세스 토큰에 의해 부여된 권한 범위를 나타내는 스코프.
    @JsonProperty("scope")
    public String scope;

}