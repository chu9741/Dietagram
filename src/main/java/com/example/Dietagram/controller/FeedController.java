package com.example.Dietagram.controller;


import com.example.Dietagram.domain.Feed;
import com.example.Dietagram.domain.FeedComment;
import com.example.Dietagram.domain.FeedImage;
import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.NutritionDTO;
import com.example.Dietagram.dto.ResponseFeedCommentDTO;
import com.example.Dietagram.dto.ResponseFeedDTO;
import com.example.Dietagram.dto.ResponseFeedImageDTO;
import com.example.Dietagram.service.FeedService;
import com.example.Dietagram.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    OAuthService oAuthService;

    @Autowired
    FeedService feedService;

    @GetMapping
    public ResponseEntity<?> getAllFeeds(@RequestHeader String token){

        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }

        List<Long> followingList = userFromRepo.getFollowingList();
        List<Feed> feedList = feedService.getAllFeeds(userFromRepo);

        return ResponseEntity.ok().body(feedList);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postFeed(@RequestHeader String token,
                                      @RequestPart NutritionDTO nutritionDTO,
                                      @RequestPart String content,
                                      @RequestPart MultipartFile image) throws IOException {
        User userFromRepo = oAuthService.getUserByToken(token);
        // user -> feed , feedimg ,저장

        Feed newFeed =feedService.createFeed(image,nutritionDTO,userFromRepo,content,"Server");
        return ResponseEntity.ok().body("feed created successfully.");
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<?> deleteFeed(@RequestHeader String token,
                                        @PathVariable String feedId){

        User user = oAuthService.getUserByToken(token);
        Feed targetFeed = feedService.getFeed(feedId);

        feedService.deleteFeed(targetFeed,user,"Server");

        return ResponseEntity.ok().body("feed deleted.");
    }

    @PostMapping("/{feedId}/comment")
    public ResponseEntity<?> createComment(@RequestHeader String token,
                                           @PathVariable String feedId,
                                           @RequestParam String comment){
        User user = oAuthService.getUserByToken(token);
        Feed targetFeed = feedService.getFeed(feedId);

        FeedComment feedComment = feedService.createFeedComment(targetFeed,user,comment);
        ResponseFeedCommentDTO feedCommentDTO = feedComment.toResponseDTO();
        return ResponseEntity.ok().body(feedCommentDTO);
    }

    @DeleteMapping("/{feedCommentId}/comment")
    public ResponseEntity<?> deleteComment(@RequestHeader String token,
                                           @PathVariable String feedCommentId){
        User user = oAuthService.getUserByToken(token);
        FeedComment feedComment = feedService.getFeedComment(feedCommentId);
        feedService.deleteFeedComment(feedComment);

        return ResponseEntity.ok().body("comment deleted.");
    }

//
    @GetMapping("/mainfeedlist")
    public ResponseEntity<?> mainFeedList(@RequestHeader String token){
        User user = oAuthService.getUserByToken(token);
        List<Long> userFollowingList = user.getFollowingList();
        List<Feed> feedList = feedService.getAllFeeds(user); // user's followinglist user's feeds


        List<ResponseFeedDTO> responseFeedDTOList = new ArrayList<>();
        for(Feed feed  : feedList){
            List<FeedComment> feedCommentList =feed.getFeedCommentList();
            List<ResponseFeedCommentDTO> responseFeedCommentDTOList = new ArrayList<>();
            FeedImage feedImage = feed.getFeedFeedImage();

            for(FeedComment feedComment : feedCommentList){  // response_feed_comment list 생성 , FeedCommentDTO 로 생성
                ResponseFeedCommentDTO dto = ResponseFeedCommentDTO.builder().id(feedComment.getId())
                        .createdDate(feedComment.getCreatedDate()).nickname(feedComment.getNickname())
                        .content(feedComment.getContent()).build();

                responseFeedCommentDTOList.add(dto);
            }


            ResponseFeedImageDTO responseFeedImageDTO = feedImage.toResponseDto(); //image를 dto로 생성

            ResponseFeedDTO responseFeedDTO = ResponseFeedDTO.builder()
                    .responseFeedImageDTO(responseFeedImageDTO)
                    .responseFeedCommentDTOList(responseFeedCommentDTOList).createdDate(feed.getCreatedDate())
                    .id(feed.getId()).content(feed.getContent()).nickname(feed.getFeedUser().getNickname())
                    .build();

            responseFeedDTOList.add(responseFeedDTO);
        }

        return ResponseEntity.ok().body(responseFeedDTOList);
    }



}
