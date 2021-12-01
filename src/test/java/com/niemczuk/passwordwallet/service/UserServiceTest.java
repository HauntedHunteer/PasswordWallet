package com.niemczuk.passwordwallet.service;

import com.niemczuk.passwordwallet.dto.RegistrationDto;
import com.niemczuk.passwordwallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findUserByLogin_returnUserData_whenLoginExist() {
        // given
        String login = "login";
        when(userRepository.existsByLogin(any())).thenReturn(true);
        // when
        userService.findUserByLogin(login);
        // then
        verify(userRepository, times(1)).existsByLogin(any());
        verify(userRepository, times(1)).findByLogin(any());
    }

    @Test
    void saveUser_shouldSaveNewUser_whenCorrectDataGivenAndPasswordKeptAs(){
        // given
        RegistrationDto newUserData = new RegistrationDto("login", "password", true);

        // when
        userService.saveUser(newUserData);

        // then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void saveUser_shouldSaveNewUser_whenCorrectDataGivenAndPasswordKeptAsSha256(){
        // given
        RegistrationDto newUserData = new RegistrationDto("login", "password", false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // when
        userService.saveUser(newUserData);

        // then
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void findUserByLogin_shouldThrowException_whenLoginNotExist() {
        // given

        String login = "sogin";
        when(userRepository.existsByLogin(any())).thenReturn(false);

        // when

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.findUserByLogin(login));

        //then

        String expectedMessage = "User does not exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).existsByLogin(any());
        verify(userRepository, times(0)).findByLogin(any());
    }
}
