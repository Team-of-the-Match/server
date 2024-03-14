package com.totm.totm.component;

import com.totm.totm.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.totm.totm.dto.ManagerDto.AddManagerRequestDto;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ManagerCreaterBean implements InitializingBean {

    private final ManagerService managerService;

    @Value("${manager.username}")
    private String username;

    @Value("${manager.password}")
    private String password;

    @Value("${manager.name}")
    private String name;

    @Value("${manager.phone-number}")
    private String phoneNumber;


    @Override
    public void afterPropertiesSet() throws Exception {
        AddManagerRequestDto request = new AddManagerRequestDto(username, password, name, phoneNumber);

        try {
            managerService.addManager(request);
        } catch(Exception e) {
            log.info("매니저 계정이 이미 존재합니다.");
        }
    }
}
