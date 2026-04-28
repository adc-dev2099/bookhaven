// Loads preset data such as sample books, users, and genres for testing
package com.example.bookhaven.controller;

import com.example.bookhaven.preset.BookHavenPreset;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
@Profile("dev")
public class DevPresetController {
    private final BookHavenPreset preset;

    public DevPresetController(BookHavenPreset preset){
        this.preset = preset;
    }

    @PostMapping("/reseed")
    public String resetDatabase(){
        preset.reseedDatabase();
        return "Database reset complete!";
    }
}
