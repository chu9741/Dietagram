package com.example.Dietagram.domain;


import com.example.Dietagram.dto.CalorieByDateDTO;
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
public class Feed extends BaseTimeDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private long id;

    private String content;

//    @ElementCollection
//    @CollectionTable(joinColumns = @JoinColumn(name = "feed_id"))
//    @Column(name = "likes")
//    private List<Long> likes = new ArrayList<>(); // 리스트에 좋아요를 누른 userid 추가

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feed_image_id")
    private FeedImage feedFeedImage;

    @OneToMany(mappedBy = "feedCommentFeed")
    private List<FeedComment> feedCommentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User feedUser;


    // =============================================== //

    public void addFeedImage(FeedImage feedImage){
        feedImage.setFeedImageFeed(this);
        this.feedFeedImage = feedImage;
    }

    public void addUser(User user){
        if(!user.getFeedList().contains(this)){
            user.getFeedList().add(this);
            this.setFeedUser(user);
        }
    }

    public void addFeedComment(FeedComment feedComment){
        if(!this.getFeedCommentList().contains(feedComment)){
            feedComment.setFeedCommentFeed(this);
            this.getFeedCommentList().add(feedComment);
        }
    }


}



