package rshinna.orcacast.infrastructure.ai;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VoiceCommandService {
    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;

    public VoiceCommandService(TranscriptionModel transcriptionModel, ChatClient chatClient, TextToSpeechModel textToSpeechModel) {
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClient;
        this.textToSpeechModel = textToSpeechModel;
    }

    public byte[] process(MultipartFile audio) {
        var transcribed = transcriptionModel.transcribe(audio.getResource());
        var reply = chatClient.prompt(transcribed).call().content();
        return textToSpeechModel.call(reply);

    }
}
