package com.example.jabaJuiceServer.analyticsServices;

import com.example.jabaJuiceServer.analyticsDAOs.Interest;
import com.example.jabaJuiceServer.analyticsRepositories.InterestRepository;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.UserRepository;
import com.example.jabaJuiceServer.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class InterestService {

    private UserService userService;
    private UserRepository userRepository;
    private InterestRepository interestRepository;
    @Autowired
    public InterestService(UserService userService, UserRepository userRepository, InterestRepository interestRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
    }

    public Interest saveInterest(Interest interest, String email){
        User user = userService.getUserByEmail(email);
        interest.setUser(user);
        return interestRepository.save(interest);
    }

    public List<Interest> getAllInterests() {
        return interestRepository.findAll();
    }

    public List<Interest> getMainTopics() {
        List<Interest> mainTopics = interestRepository.findAll();
        mainTopics.forEach(interest -> interest.setSubtopics(Collections.emptySet()));
        return mainTopics;
    }

    public List<Interest> loadInterestsFromJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("interests.json");

        TypeReference<List<Interest>> typeReference = new TypeReference<List<Interest>>() {};
        return objectMapper.readValue(resource.getInputStream(), typeReference);
    }
    public List<Interest> getInterests() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("interests.json");
            return mapper.readValue(resource.getInputStream(), new TypeReference<List<Interest>>() {});
        } catch (IOException e) {
            // Handle exception (e.g., log error, throw custom exception)
            return null;
        }
    }

    public void updateUserInterests(User user, List<Interest> interests) {
        for (Interest interest : interests){
            if (!user.getInterests().contains(interest)) {
                interest.setUser(user);
                interestRepository.save(interest);
            }
        }
        user.getInterests().addAll(interests);
        userRepository.save(user);
    }
}
