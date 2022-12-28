package com.example.Dietagram.service;

import com.example.Dietagram.domain.Feed;
import com.example.Dietagram.domain.FeedComment;
import com.example.Dietagram.domain.FeedImage;
import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.*;
import com.example.Dietagram.repository.FeedCommentRepository;
import com.example.Dietagram.repository.FeedImageRepository;
import com.example.Dietagram.repository.FeedRepository;
import com.example.Dietagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class FeedService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    FeedImageRepository feedImageRepository;

    @Autowired
    FeedCommentRepository feedCommentRepository;

    @Autowired
    OAuthService oAuthService;


    @Autowired
    ImageService imageService;

    public Feed getFeed(String id){
        return feedRepository.findById(Long.parseLong(id)).orElseThrow();
    }

    public List<Feed> getAllFeeds(User user){
        List<Long> followingList = user.getFollowingList();
        if(followingList == null){
            return null;
        }
        List<Feed> allFeedList= new ArrayList<>();

        for(long i : followingList){
            User tempUser = userRepository.findById(i).orElseThrow();
            allFeedList.addAll(tempUser.getFeedList());
        }

        return allFeedList;
    }



    public FeedComment getFeedComment(String feedCommentId){
        return feedCommentRepository.findById(Long.parseLong(feedCommentId)).orElseThrow();
    }


    public Feed createFeed(MultipartFile foodImage, NutritionDTO nutritionDTO, User user, String content,
                           String localOrServer) throws IOException {

        Feed feed = new Feed();
        FoodImageDTO foodImageDTO = imageService.upload(foodImage,user.getId().toString(),localOrServer);
        // feed post, save,
        FeedImage feedImage = FeedImage.FromNutritionDTO(nutritionDTO);
        feedImage.setImageUrl(foodImageDTO.getUrl());
        feedImage.setImageName(foodImageDTO.getFilename());
        feedImageRepository.save(feedImage);

        feed.addFeedImage(feedImage);
        feed.addUser(user);
        feed.setContent(content);

        feedRepository.save(feed);
        return feed;
    }

    public void deleteFeed(Feed targetFeed, User user, String localOrServer){

        FeedImage feedImage = targetFeed.getFeedFeedImage();
        feedCommentRepository.deleteAll(targetFeed.getFeedCommentList());
        imageService.delete(feedImage.getImageName(), user.getId().toString(), localOrServer);
        feedRepository.delete(targetFeed);
        feedImageRepository.delete(feedImage);

    }

    public FeedComment createFeedComment(Feed targetFeed, User user, String content){
        FeedComment feedComment = new FeedComment();
        feedComment.setContent(content);
        feedComment.setNickname(user.getNickname());

        targetFeed.addFeedComment(feedComment);
        feedCommentRepository.save(feedComment);
        feedRepository.save(targetFeed);

        return feedComment;
    }

    public void deleteFeedComment(FeedComment feedComment){
        feedCommentRepository.delete(feedComment);
    }

    public List<CalorieByDateDTO> getAllCalorieByDate(User user){
        List<Feed> userFeedList =  user.getFeedList();
        List<CalorieByDateDTO> calorieByDateDTOList = new ArrayList<>();
        for(int i=0; i<userFeedList.size(); i++){
            Feed temp = userFeedList.get(i);
            if(i!=0){
                CalorieByDateDTO formerTemp = calorieByDateDTOList.get(calorieByDateDTOList.size()-1); // 리스트의 마지막 값

                CalorieByDateDTO dto = CalorieByDateDTO.createDTO(temp); // 현재값
                int result = compareDay(formerTemp.date,dto.date); // 날짜 비교
                if(result==0){formerTemp.calorieSum += dto.calorieSum;}
                else {calorieByDateDTOList.add(dto);}
            }
            else {
                CalorieByDateDTO dto = CalorieByDateDTO.createDTO(temp);
                calorieByDateDTOList.add(dto);
            }
        }
        return calorieByDateDTOList;
    }

    public List<CalorieByDateDTO> getAllCalorieByMinute(User user){
        List<Feed> userFeedList =  user.getFeedList();
        List<CalorieByDateDTO> calorieByDateDTOList = new ArrayList<>();
        for(int i=0; i<userFeedList.size(); i++){
            Feed temp = userFeedList.get(i);
            if(i!=0){
                CalorieByDateDTO formerTemp = calorieByDateDTOList.get(calorieByDateDTOList.size()-1); // 리스트의 마지막 값

                CalorieByDateDTO dto = CalorieByDateDTO.createDTO(temp); // 현재값
                int result = compareMinute(formerTemp.date,dto.date); // 날짜 비교
                if(result==0){formerTemp.calorieSum += dto.calorieSum;}
                else {calorieByDateDTOList.add(dto);}
            }
            else {
                CalorieByDateDTO index0Dto = CalorieByDateDTO.createDTO(temp);
                calorieByDateDTOList.add(index0Dto);
            }
        }
        return calorieByDateDTOList;
    }

    public List<ResponseFeedDTO> getMyResponseFeedDTOList(User user){
        List<Feed> feedList = user.getFeedList();
        List<ResponseFeedDTO> responseFeedDTOList = new ArrayList<>();
        for(Feed feed  : feedList){
            List<FeedComment> feedCommentList =feed.getFeedCommentList();
            List<ResponseFeedCommentDTO> responseFeedCommentDTOList = new ArrayList<>();
            FeedImage feedImage = feed.getFeedFeedImage();

            for(FeedComment feedComment : feedCommentList){  // response_feed_comment list 생성 , FeedCommentDTO 로 생성
                ResponseFeedCommentDTO dto = ResponseFeedCommentDTO.builder().id(feedComment.getId())
                        .nickname(feedComment.getNickname()).content(feedComment.getContent()).build();

                responseFeedCommentDTOList.add(dto);
            }


            ResponseFeedImageDTO responseFeedImageDTO = feedImage.toResponseDto(); //image를 dto로 생성

            ResponseFeedDTO responseFeedDTO = ResponseFeedDTO.builder()
                    .responseFeedImageDTO(responseFeedImageDTO)
                    .responseFeedCommentDTOList(responseFeedCommentDTOList)
                    .id(feed.getId()).content(feed.getContent())
                    .nickname(feed.getFeedUser().getNickname())
                    .createdDate(feed.getCreatedDate())
                    .build();

            responseFeedDTOList.add(responseFeedDTO);
        }
        return responseFeedDTOList;
    }



    public static int compareDay(LocalDateTime date1, LocalDateTime date2) {
        LocalDateTime dayDate1 = date1.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime dayDate2 = date2.truncatedTo(ChronoUnit.DAYS);

        return dayDate1.compareTo(dayDate2);
    }

    public static int compareMinute(LocalDateTime date1, LocalDateTime date2) {
        LocalDateTime dayDate1 = date1.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime dayDate2 = date2.truncatedTo(ChronoUnit.MINUTES);

        return dayDate1.compareTo(dayDate2);
    }
}
