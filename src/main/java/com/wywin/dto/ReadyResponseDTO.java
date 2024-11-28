package com.wywin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ReadyResponseDTO {

    private String tid;
    private String next_redirect_pc_url; //카카오톡으로 결제 요청 메시지를 보내기 위한 사용자 정보 입력화면 Redirect URL
}
