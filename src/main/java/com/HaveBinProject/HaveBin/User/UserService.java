package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.DTO.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    //일반회원가입
    public ResponseEntity<?> join(RegisterDto registerDto){
        User user = new User();

        user.setEmail(registerDto.getEmail());
        user.setNickname(registerDto.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));

        try {
            userRepository.save(user);
        } catch(Exception e){
            logger.error("Join Error");
            return ResponseEntity.badRequest().body("Join Error");
        }
        logger.info("Join Success");
        return ResponseEntity.ok("Welcome HaveBin!!");
    }

    @Transactional
    public ResponseEntity<?> validateDuplicateUser(String email) {
        System.out.println("email = " + email);
        email = email.substring(1);
        email = email.substring(0, email.length() - 1);

        //중복 검출 시 예외 발생
        try {
            List<User> findEmails = userRepository.findByEmail(email);
            if (!findEmails.isEmpty()) {
                throw new IllegalStateException("이미 존재하는 이메일입니다.");
            }
        } catch (IllegalStateException e){
            logger.error("Email Duplicate");
            return ResponseEntity.badRequest().body("Email Duplicate"); // 상태코드 == 400
        }
        logger.info("Email Not Duplicate");
        return ResponseEntity.ok("Email Not Duplicate"); // 상태코드 == 200

    }

    @Transactional
    public ResponseEntity<?> validateDuplicateNickname(String nickname){
        System.out.println("nickname = " + nickname);
        nickname = nickname.substring(1);
        nickname = nickname.substring(0, nickname.length() - 1);

        //이메일 중복 검출 시 예외 발생
        try{
            List<User> findNicknames = userRepository.findByNickname(nickname);
            if(!findNicknames.isEmpty()){
                throw new IllegalStateException("이미 존재하는 닉네임입니다.");
            }
        } catch (IllegalStateException e){
            logger.error("Nickname Duplicate");
            return ResponseEntity.badRequest().body("Nickname Duplicate");
        }
        logger.info("Nickname Not Duplicate");
        return ResponseEntity.ok("Nickname Not Duplicate");
    }

    //회원 전체 조회, 읽기 전용표시로 성능향상
    public List<User> findMembers() {
        return userRepository.findAll();
    }

    //단건 조회
    public User findOne(Long id){
        return userRepository.find(id);
    }

    //유저 삭제
    @Transactional
    public void deleteUser(Long id){
        userRepository.delete(id);
    }
}
