package com.example.Dietagram.service;


import com.example.Dietagram.config.OAuthAttributes;
import com.example.Dietagram.config.UserProfile;
import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.LoginDTO;
import com.example.Dietagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth 서비스의 유저 정보들

        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);
        userProfile.setProvider(registrationId);
        User user = saveOrUpdate(userProfile);

        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, userProfile, registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                customAttribute,
                userNameAttributeName);

    }

    private Map customAttribute(Map attributes, String userNameAttributeName, UserProfile memberProfile, String registrationId) {
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", memberProfile.getName());
        customAttribute.put("email", memberProfile.getEmail());
        return customAttribute;

    }

    private User saveOrUpdate(UserProfile userProfile) {

        User user = userRepository.findByEmailAndProvider(userProfile.getEmail(), userProfile.getProvider())
                .map(m -> m.update(userProfile.getName(), userProfile.getEmail())) // OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 DB에도 update
                .orElse(userProfile.toUser());

        return userRepository.save(user);
    }

    public User setTokenInUser(User user) throws UnsupportedEncodingException {
        user.setToken(tokenProvider.create(user));
        return userRepository.save(user);
    }

    public User getUserByToken(String token){
        String id = tokenProvider.validateAndGetUserId(token);
        return userRepository.findById(Long.parseLong(id)).orElse(null);
    }

    public User getUserByAttributeId(String attributeId){
        return userRepository.findByAttributeId(attributeId);
    }


    public User createUserFromLoginDTO(LoginDTO loginDTO){
        User newUser = User.builder().attributeId(loginDTO.getId())
                .nickname(loginDTO.getNickname()+loginDTO.getId())
                .build();
        userRepository.save(newUser);

        return userRepository.findByAttributeId(loginDTO.getId());
    }



}

