package com.mockinterview.mockinterview.SprintAI.response;

import com.mockinterview.mockinterview.SprintAI.entities.Sprint;
import com.mockinterview.mockinterview.SprintAI.entities.Story;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIGenerateSprintResponse {
    private Sprint sprint;
    private List<Story> stories;
}
