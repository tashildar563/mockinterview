package com.mockinterview.mockinterview.SprintAI.response;

import com.mockinterview.mockinterview.SprintAI.entities.Story;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIGenerateStoriesResponse {
    private List<Story> stories;
}
