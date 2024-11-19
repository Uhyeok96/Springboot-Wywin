package com.wywin.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService { // 이메일 전송하고 인증번호 생성하는 서비스

    private final JavaMailSender mailSender; // 이메일 전송을 위한 객체

    public static final String ePw = createCode();
    private String authNum; // 인증번호 저장할 변수

    // 인증번호 8자리 무작위 생성
    public static String createCode() {
        Random random = new Random(); // 랜덤 객체 생성
        StringBuffer key = new StringBuffer(); // 인증번호를 담을 객체

        for(int i=0; i<8; i++) {
            int idx = random.nextInt(3);

            switch (idx) {
                case 0:
                    // a(97) ~ z(122)
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    // A(65) ~ Z(90)
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    // 0 ~ 9
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString(); // 생성된 인증번호 저장
    }

    /**
     * 이메일의 내용과 양식을 설정하는 메소드
     * @param email 인증번호를 보낼 대상 이메일 주소
     * @return MimeMessage 이메일 양식이 담긴 MimeMessage 객체
     * @throws MessagingException 이메일 전송 중 발생할 수 있는 예외
     * @throws UnsupportedEncodingException 인코딩 지원 안됨 예외
     */
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        createCode(); // 인증번호 생성

        String setFrom = "audals0712@gmail.com"; // 보내는 이메일 (sender)
        String toEmail = email;                  // 받는 이메일 (receiver)
        String title = "WYWIN 인증번호 테스트";    // 이메일 제목

        // MimeMessage 객체 생성
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); // 받는 사람 설정
        message.setSubject(title); // 이메일 제목 설정

        // 메일 내용 (HTML)
        String msgOfEmail = "";
        msgOfEmail += "<div style='margin:20px;'>"; // 본문 외부 div
        msgOfEmail += "<h1> 안녕하세요 WYWIN test 입니다.";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요</p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.</p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana;'>"; // 코드 박스 스타일
        msgOfEmail += "<h3 style='color=blue;'>회원가입 인증 코드 입니다.</h3>"; // 코드 제목
        msgOfEmail += "<div style='font-size:130%;'>"; // 코드 부분
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authNum + "</strong></div><br/>"; // 인증번호 삽입
        msgOfEmail += "</div>";

        // 발신자, 본문내용, 문자셋, MIME 타입(HTML) 설정
        message.setFrom(setFrom);
        message.setText(msgOfEmail, "utf-8", "html");

        return message; // MimeMessage 객체 반환
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {

        MimeMessage message = createEmailForm(to);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }


}
