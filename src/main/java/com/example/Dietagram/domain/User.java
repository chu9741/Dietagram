package com.example.Dietagram.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
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
    @Column(name = "following_username")
    private List<Long> followingList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follower_username")
    private List<Long> followerList = new ArrayList<>();

    public User update(String name, String email) {
        this.name = name;
        this.email = email;
        this.nickname= name+email;
        return this;
    }
}
