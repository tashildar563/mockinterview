package com.mockinterview.mockinterview.SprintAI.services;

import com.mockinterview.mockinterview.SprintAI.entities.Sprint;
import com.mockinterview.mockinterview.SprintAI.entities.Story;
import com.mockinterview.mockinterview.SprintAI.enums.SprintStatus;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import com.mockinterview.mockinterview.SprintAI.repositories.SprintRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SprintService {

  @Autowired
  SprintRepository sprintRepository;

  /**
   * Get all sprints
   */
  public List<Sprint> getAllUsers() {
    return sprintRepository.findAll();
  }
  /**
   * Create a new story
   */
  public Sprint createSprint(Sprint sprint) {
    sprint.setCreatedAt(LocalDateTime.now());
    sprint.setStatus(SprintStatus.PLANNED);
    return sprintRepository.save(sprint);
  }
}
