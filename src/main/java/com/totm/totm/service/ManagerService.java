package com.totm.totm.service;

import com.totm.totm.component.JwtTokenProvider;
import com.totm.totm.dto.TokenResponseDto;
import com.totm.totm.entity.Manager;
import com.totm.totm.exception.DuplicatedUsernameException;
import com.totm.totm.exception.ManagerNotFoundException;
import com.totm.totm.exception.PasswordNotEqualException;
import com.totm.totm.exception.TokenNotFoundException;
import com.totm.totm.repository.ManagerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Duration;
import java.util.Optional;

import static com.totm.totm.dto.ManagerDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = { MethodArgumentNotValidException.class })
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public TokenResponseDto login(LoginRequestDto request) {
        Optional<Manager> findManager = managerRepository.findByUsername(request.getUsername());
        if(findManager.isPresent()) {
            if(!passwordEncoder.matches(request.getPassword(), findManager.get().getPassword()))
                throw new PasswordNotEqualException("아이디 또는 비밀번호가 틀렸습니다.");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            String accessToken = jwtTokenProvider.createAccessToken(authentication, findManager.get().getId(), null, "manager");
            String refreshToken = jwtTokenProvider.createRefreshToken();

            redisTemplate.opsForValue().set(findManager.get().getUsername(), refreshToken, Duration.ofDays(7L));

            return new TokenResponseDto(accessToken, refreshToken);
        } else throw new ManagerNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
    }

    @Transactional
    public TokenResponseDto refresh(String username, HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        if(StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        String savedToken = (String) redisTemplate.opsForValue().get(username);
        if(refreshToken.equals(savedToken)) {
            Manager manager = managerRepository.findByUsername(username)
                    .orElseThrow(() -> new ManagerNotFoundException("해당 매니저를 찾을 수 없음"));

            String accessToken = jwtTokenProvider.createNewAccessToken(manager.getUsername(), manager.getId(), null, "manager");
            String newRefreshToken = jwtTokenProvider.createRefreshToken();

            redisTemplate.opsForValue().set(manager.getUsername(), newRefreshToken, Duration.ofDays(7L));

            return new TokenResponseDto(accessToken, newRefreshToken);
        } else
            throw new TokenNotFoundException("다시 로그인 후 시도해주세요.");
    }

    public Page<FindManagersResponseDto> findManagers(String name, Pageable pageable) {
        return managerRepository.findManagersByNameContainsOrderByCreatedDateDesc(name, pageable)
                .map(FindManagersResponseDto::new);
    }

    @Transactional
    public void addManager(AddManagerRequestDto request) {
        if(duplicatedUsername(request.getUsername())) throw new DuplicatedUsernameException("중복된 아이디입니다.");
        Manager manager = new Manager(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getName(), request.getPhoneNumber());
        managerRepository.save(manager);
    }

    public boolean duplicatedUsername(String username) {
        return managerRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public void delete(Long id) {
        managerRepository.deleteById(id);
    }

    @Transactional
    public void resetPassword(Long id) {
        Optional<Manager> findManager = managerRepository.findById(id);
        if(findManager.isPresent()) {
            findManager.get().changePassword(passwordEncoder.encode("123456789a"));
        } else throw new ManagerNotFoundException("해당 매니저가 존재하지 않습니다.");
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDto request) {
        Optional<Manager> findManager = managerRepository.findById(id);
        if(findManager.isPresent()) {
                if(passwordEncoder.matches(request.getOldPassword(), findManager.get().getPassword())) {
                    findManager.get().changePassword(passwordEncoder.encode(request.getNewPassword()));
                } else throw new PasswordNotEqualException("기존 패스워드가 일치하지 않습니다.");
        } else throw new ManagerNotFoundException("해당 매니저가 존재하지 않습니다.");
    }
}
