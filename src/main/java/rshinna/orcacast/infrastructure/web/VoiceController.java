package rshinna.orcacast.infrastructure.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rshinna.orcacast.infrastructure.ai.VoiceCommandService;

@RestController
public class VoiceController {

    private final VoiceCommandService voiceCommandService;

    public VoiceController(VoiceCommandService voiceCommandService) {
        this.voiceCommandService = voiceCommandService;
    }

    @PostMapping("/voice-commands")
    public String handle(@RequestParam("file") MultipartFile audio) {
        return voiceCommandService.process(audio);
    }
}
