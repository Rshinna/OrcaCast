package rshinna.orcacast.infrastructure.ai;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VoiceCommandService {
    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;

    public VoiceCommandService(TranscriptionModel transcriptionModel, ChatClient chatClient) {
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClient;
    }

    public String process(MultipartFile audio) {
        var transcribed = transcriptionModel.transcribe(audio.getResource());
        return chatClient.prompt(transcribed).call().content();

    }
}
