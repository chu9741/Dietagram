package com.example.Dietagram.config;

import com.example.Dietagram.domain.User;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    NAVER("naver", (attributes) -> {
        //noinspection unchecked
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        System.out.println("response = " + response);
        UserProfile userProfile = new UserProfile();
        userProfile.setAttributeId((String) response.get("id"));
        userProfile.setName((String) response.get("name"));
        userProfile.setEmail(((String) response.get("email")));
        userProfile.setNickname((String) response.get("name")+(String) response.get("email"));
        return userProfile;
    }),

    KAKAO("kakao", (attributes) -> {
        //noinspection unchecked
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        //noinspection unchecked
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
        //noinspection unchecked
        Long kakaoId =  (Long)attributes.get("id");
        System.out.println("response = " + kakaoProfile);

        UserProfile userProfile = new UserProfile();
        userProfile.setName((String) kakaoProfile.get("nickname"));
        userProfile.setAttributeId(kakaoId.toString());
        userProfile.setEmail((String) kakaoAccount.get("email"));
        userProfile.setNickname((String) kakaoProfile.get("nickname")+(String) kakaoAccount.get("email"));
        return userProfile;
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}

