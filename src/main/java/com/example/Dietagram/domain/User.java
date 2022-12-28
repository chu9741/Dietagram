package com.example.Dietagram.domain;

import com.example.Dietagram.dto.UserPrimeDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String attributeId;
    private String email;
    private String provider;
    private String nickname;
    private Gender gender;
    private long calorie_goal;
    private long weight;
    private long height;
    private String token;


    @OneToMany(mappedBy = "feedUser",cascade = CascadeType.ALL)
    private List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "feedImageUser",cascade = CascadeType.ALL)
    private List<FeedImage> feedImageList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "following_userid")
    private List<Long> followingList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follower_userid")
    private List<Long> followerList = new ArrayList<>();

    public User update(String name, String email) {
        this.name = name;
        this.email = email;
        this.nickname= name+email;
        return this;
    }

    public void followUser(User targetUser){
        this.getFollowingList().add(targetUser.id);
        targetUser.getFollowerList().add(this.id);
    }

    public void unfollowUser(User targetUser){
        this.getFollowingList().remove(targetUser.id);
        targetUser.getFollowerList().remove(this.id);
    }

    //=========================================//

    public UserPrimeDTO toPrimeDTO(){
        return UserPrimeDTO.builder().id(id).email(this.email)
                .gender(this.gender).name(this.name)
                .nickname(this.nickname).build();
    }

}
