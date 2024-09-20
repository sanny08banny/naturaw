package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.entities.*;
import com.example.jabaJuiceServer.repositories.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {
    private FollowRepository followRepository;
    private NotificationService notificationService;

    @Autowired
    public FollowService(FollowRepository followRepository, NotificationService notificationService) {
        this.followRepository = followRepository;
        this.notificationService = notificationService;
    }

    public boolean isFollowing(User user, User followedUser) {
        return followRepository.existsByUserAndFollowedUser(user, followedUser);
    }

    public void followUser(User user, User followedUser) {
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollowedUser(followedUser);
        followRepository.save(follow);
        user.getFollows().add(follow);
        followedUser.getFollowers().add(follow);
        // Send a new follower notification to the followed user
        notificationService.sendFollowNotification(followedUser.getId(), user);
    }

    public void unfollowUser(Follow follow) {
        User user = follow.getUser();
        User folloewUser = follow.getFollowedUser();
        user.getFollows().remove(follow);
        folloewUser.getFollowers().remove(follow);
        followRepository.delete(follow);
    }

    public List<User> getFollowersByUser(User user) {
        List<Follow> follows = followRepository.findFollowersByFollowedUser(user);

        List<User> result = new ArrayList<>();

        for (Follow follow : follows){
            User user1 = follow.getUser();
            result.add(user1);
        }
        return result;
    }

    public List<User> getFollowsByUser(User user) {
        List<Follow> follows = followRepository.findFollowsByUser(user);

        List<User> result = new ArrayList<>();

        for (Follow follow : follows){
            User user1 = follow.getFollowedUser();
            result.add(user1);
        }
        return result;
    }
    public Follow getFollowByUserAndFollowedUser(User user, User followedUser) {
        return followRepository.findByUserAndFollowedUser(user, followedUser).get();
    }
}

