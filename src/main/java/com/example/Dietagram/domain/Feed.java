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
public class Feed extends BaseTimeDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private long id;

    private String title;
    private String content;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "feed_id"))
    @Column(name = "likes")
    private List<Long> likes = new ArrayList<>(); // 리스트에 좋아요를 누른 userid 추가

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feed_image_id")
    private FeedImage feedFeedImage;

    @OneToMany(mappedBy = "feedCommentFeed")
    private List<FeedComment> feedCommentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User feedUser;


}



