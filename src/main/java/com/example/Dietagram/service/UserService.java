package com.example.Dietagram.service;


import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.UserEditDTO;
import com.example.Dietagram.dto.UserPrimeDTO;
import com.example.Dietagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getUserFromRepo(String nickname){
        return userRepository.findByNickname(nickname);
    }

    public User getUserFromAttributeId(Map<String, Object> attributes){
        String attributeId = null;
        if(attributes.get("provider").equals("naver")){
            //noinspection unchecked
            Map<String, Object> response =(Map<String, Object>) attributes.get("response");
            attributeId = (String)response.get("id");
        }
        else if(attributes.get("provider").equals("kakao")){
            attributeId =attributes.get("id").toString();
        }
        return userRepository.findByAttributeId(attributeId);
    }


    public User getUserById(String id) {
        return userRepository.findById(Long.parseLong(id)).orElse(null);
    }

    public void editMyPage(UserEditDTO dto, User user) {
        if (dto.getWeight() != 0)
            user.setWeight(dto.getWeight());
        if (dto.getCalorie_goal() != 0)
            user.setCalorie_goal(dto.getCalorie_goal());
        if (dto.getGender() != null)
            user.setGender(dto.getGender());
        if (dto.getHeight() != 0)
            user.setHeight(dto.getHeight());
        if (dto.getNewName() != null)
            user.setNickname(dto.getNewName());
        userRepository.save(user);
    }

    public String followUser(User user, User targetUser){
        if(!user.getFollowingList().contains(targetUser.getId()) && !user.equals(targetUser)) {
            user.followUser(targetUser);
            userRepository.save(user);
            userRepository.save(targetUser);
            return "success";
        }
        else return "failed";
    }

    public String unfollowUser(User user, User targetUser){
        if(user.getFollowingList().contains(targetUser.getId())){
            user.unfollowUser(targetUser);
            userRepository.save(user);
            userRepository.save(targetUser);
            return "success";
        }
        return "failed";
    }

    public List<UserPrimeDTO> getFollowingList(User user){
        List<UserPrimeDTO> userPrimeDTOList = new ArrayList<>();
        for(long followingId : user.getFollowingList()){
            User temp = getUserById(Long.toString(followingId));
            UserPrimeDTO dto = temp.toPrimeDTO();
            userPrimeDTOList.add(dto);
        }
        return userPrimeDTOList;
    }

    public List<UserPrimeDTO> getFollowerList(User user){
        List<UserPrimeDTO> userPrimeDTOList = new ArrayList<>();
        for(long followerId : user.getFollowerList()){
            User temp = getUserById(Long.toString(followerId));
            UserPrimeDTO dto = temp.toPrimeDTO();
            userPrimeDTOList.add(dto);
        }
        return userPrimeDTOList;
    }



    public List<UserPrimeDTO> searchUser(String keyword){
        List<User> userListByKeyword = userRepository.findByNicknameContainingIgnoreCase(keyword);
        List<UserPrimeDTO> userPrimeDTOList = new ArrayList<>();
        if(userListByKeyword.isEmpty())
            return new ArrayList<UserPrimeDTO>();
        else
            for (User user :userListByKeyword) {
                userPrimeDTOList.add(user.toPrimeDTO());
            }
        return userPrimeDTOList;
    }

}
