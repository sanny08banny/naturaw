package com.example.jabaJuiceServer.analyticsControllers;

import com.example.jabaJuiceServer.analyticsDAOs.Interest;
import com.example.jabaJuiceServer.analyticsServices.InterestService;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sanny/api/interests")
public class InterestController {

    private UserService userService;
    private InterestService interestService;
    @Autowired
    public InterestController(UserService userService, InterestService interestService) {
        this.userService = userService;
        this.interestService = interestService;
    }

    @GetMapping("/all")
    public List<Interest> getAllInterestsFromJson() throws IOException {
        return interestService.loadInterestsFromJsonFile();
    }
    @PostMapping("/up-date-interests")
    public ResponseEntity<String> updateUserInterests(
            @RequestParam("email") String email,
            @RequestBody List<Interest> interests) {

        User user = userService.getUserByEmail(email);
        interestService.updateUserInterests(user,interests);

        return ResponseEntity.ok("Interests updated successfully");
    }

}

