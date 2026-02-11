package com.mockinterview.mockinterview.SprintAI.services;

import com.mockinterview.mockinterview.SprintAI.entities.Sprint;
import com.mockinterview.mockinterview.SprintAI.entities.Story;
import com.mockinterview.mockinterview.SprintAI.entities.Task;
import com.mockinterview.mockinterview.SprintAI.entities.User;
import com.mockinterview.mockinterview.SprintAI.enums.SprintStatus;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import com.mockinterview.mockinterview.SprintAI.enums.TaskStatus;
import com.mockinterview.mockinterview.SprintAI.repositories.SprintRepository;
import com.mockinterview.mockinterview.SprintAI.repositories.StoryRepository;
import com.mockinterview.mockinterview.SprintAI.repositories.TeamRepository;
import com.mockinterview.mockinterview.SprintAI.repositories.UserRepository;
import com.mockinterview.mockinterview.SprintAI.response.DashboardStatsResponse;
import com.mockinterview.mockinterview.SprintAI.response.SprintStatsResponse;
import com.mockinterview.mockinterview.SprintAI.response.UserStatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class StatsService {
    
    @Autowired
    private SprintRepository sprintRepository;
    
    @Autowired
    private StoryRepository storyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    /**
     * Get overall dashboard statistics
     */
    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();
        
        // Sprint Stats
        List<Sprint> allSprints = sprintRepository.findAll();
        stats.setTotalSprints((long) allSprints.size());
        stats.setActiveSprints(allSprints.stream().filter(s -> s.getStatus() == SprintStatus.ACTIVE).count());
        stats.setCompletedSprints(allSprints.stream().filter(s -> s.getStatus() == SprintStatus.COMPLETED).count());
        stats.setPlannedSprints(allSprints.stream().filter(s -> s.getStatus() == SprintStatus.PLANNED).count());
        
        // Story Stats
        List<Story> allStories = storyRepository.findAll();
        stats.setTotalStories((long) allStories.size());
        stats.setTodoStories(allStories.stream().filter(s -> s.getStatus() == StoryStatus.TODO).count());
        stats.setInProgressStories(allStories.stream().filter(s -> s.getStatus() == StoryStatus.IN_PROGRESS).count());
        stats.setInReviewStories(allStories.stream().filter(s -> s.getStatus() == StoryStatus.IN_REVIEW).count());
        stats.setDoneStories(allStories.stream().filter(s -> s.getStatus() == StoryStatus.DONE).count());
        stats.setBlockedStories(allStories.stream().filter(s -> s.getStatus() == StoryStatus.BLOCKED).count());
        
        // Task Stats
        long totalTasks = allStories.stream().mapToLong(s -> s.getTasks() != null ? s.getTasks().size() : 0).sum();
        long todoTasks = allStories.stream()
            .flatMap(s -> s.getTasks() != null ? s.getTasks().stream() : java.util.stream.Stream.empty())
            .filter(t -> t.getStatus() == TaskStatus.TODO)
            .count();
        long inProgressTasks = allStories.stream()
            .flatMap(s -> s.getTasks() != null ? s.getTasks().stream() : java.util.stream.Stream.empty())
            .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
            .count();
        long doneTasks = allStories.stream()
            .flatMap(s -> s.getTasks() != null ? s.getTasks().stream() : java.util.stream.Stream.empty())
            .filter(t -> t.getStatus() == TaskStatus.DONE)
            .count();
        
        stats.setTotalTasks(totalTasks);
        stats.setTodoTasks(todoTasks);
        stats.setInProgressTasks(inProgressTasks);
        stats.setDoneTasks(doneTasks);
        
        // Story Points
        int totalPoints = allStories.stream().mapToInt(s -> s.getPoints() != null ? s.getPoints() : 0).sum();
        int completedPoints = allStories.stream()
            .filter(s -> s.getStatus() == StoryStatus.DONE)
            .mapToInt(s -> s.getPoints() != null ? s.getPoints() : 0)
            .sum();
        int inProgressPoints = allStories.stream()
            .filter(s -> s.getStatus() == StoryStatus.IN_PROGRESS)
            .mapToInt(s -> s.getPoints() != null ? s.getPoints() : 0)
            .sum();
        
        stats.setTotalStoryPoints(totalPoints);
        stats.setCompletedStoryPoints(completedPoints);
        stats.setInProgressStoryPoints(inProgressPoints);
        
        // Team Stats
        stats.setTotalTeamMembers(userRepository.count());
        stats.setTotalTeams(teamRepository.count());
        
        // Calculate Average Velocity
        List<Sprint> completedSprints = allSprints.stream()
            .filter(s -> s.getStatus() == SprintStatus.COMPLETED)
            .toList();
        
        if (!completedSprints.isEmpty()) {
            int totalVelocity = completedSprints.stream()
                .mapToInt(Sprint::getVelocity)
                .sum();
            stats.setAverageVelocity(totalVelocity / completedSprints.size());
        } else {
            stats.setAverageVelocity(0);
        }
        
        // Completion Rate
        if (stats.getTotalStories() > 0) {
            stats.setCompletionRate((stats.getDoneStories() * 100.0) / stats.getTotalStories());
        } else {
            stats.setCompletionRate(0.0);
        }
        
        // Current Sprint Info
        Optional<Sprint> activeSprint = allSprints.stream()
            .filter(s -> s.getStatus() == SprintStatus.ACTIVE)
            .findFirst();
        
        if (activeSprint.isPresent()) {
            Sprint sprint = activeSprint.get();
            stats.setCurrentSprintId(sprint.getId());
            stats.setCurrentSprintName(sprint.getName());
            
            if (sprint.getTotalStoryPoints() != null && sprint.getTotalStoryPoints() > 0) {
                int progress = (int) ((sprint.getCompletedStoryPoints() * 100.0) / sprint.getTotalStoryPoints());
                stats.setCurrentSprintProgress(progress);
            } else {
                stats.setCurrentSprintProgress(0);
            }
        }
        
        return stats;
    }
    
    /**
     * Get statistics for a specific sprint
     */
    public SprintStatsResponse getSprintStats(String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
            .orElseThrow(() -> new RuntimeException("Sprint not found"));
        
        SprintStatsResponse stats = new SprintStatsResponse();
        
        // Basic Sprint Info
        stats.setSprintId(sprint.getId());
        stats.setSprintName(sprint.getName());
        stats.setSprintGoal(sprint.getGoal());
        stats.setStatus(sprint.getStatus().toString());
        stats.setStartDate(sprint.getStartDate());
        stats.setEndDate(sprint.getEndDate());
        
        // Get all stories in this sprint
        List<Story> stories = storyRepository.findBySprintId(sprintId);
        
        // Story Counts
        stats.setTotalStories((long) stories.size());
        stats.setTodoStories(stories.stream().filter(s -> s.getStatus() == StoryStatus.TODO).count());
        stats.setInProgressStories(stories.stream().filter(s -> s.getStatus() == StoryStatus.IN_PROGRESS).count());
        stats.setInReviewStories(stories.stream().filter(s -> s.getStatus() == StoryStatus.IN_REVIEW).count());
        stats.setDoneStories(stories.stream().filter(s -> s.getStatus() == StoryStatus.DONE).count());
        stats.setBlockedStories(stories.stream().filter(s -> s.getStatus() == StoryStatus.BLOCKED).count());
        
        // Task Counts
        long totalTasks = stories.stream().mapToLong(s -> s.getTasks() != null ? s.getTasks().size() : 0).sum();
        long todoTasks = stories.stream()
            .flatMap(s -> s.getTasks() != null ? s.getTasks().stream() : java.util.stream.Stream.empty())
            .filter(t -> t.getStatus() == TaskStatus.TODO)
            .count();
        long inProgressTasks = stories.stream()
            .flatMap(s -> s.getTasks() != null ? s.getTasks().stream() : java.util.stream.Stream.empty())
            .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
            .count();
        long doneTasks = stories.stream()
            .flatMap(s -> s.getTasks() != null ? s.getTasks().stream() : java.util.stream.Stream.empty())
            .filter(t -> t.getStatus() == TaskStatus.DONE)
            .count();
        
        stats.setTotalTasks(totalTasks);
        stats.setTodoTasks(todoTasks);
        stats.setInProgressTasks(inProgressTasks);
        stats.setDoneTasks(doneTasks);
        
        // Story Points
        int totalPoints = stories.stream().mapToInt(s -> s.getPoints() != null ? s.getPoints() : 0).sum();
        int completedPoints = stories.stream()
            .filter(s -> s.getStatus() == StoryStatus.DONE)
            .mapToInt(s -> s.getPoints() != null ? s.getPoints() : 0)
            .sum();
        
        stats.setTotalStoryPoints(totalPoints);
        stats.setCompletedStoryPoints(completedPoints);
        stats.setRemainingStoryPoints(totalPoints - completedPoints);
        
        // Completion Percentage
        if (totalPoints > 0) {
            stats.setCompletionPercentage((completedPoints * 100) / totalPoints);
        } else {
            stats.setCompletionPercentage(0);
        }
        
        // Team Members Count (unique assignees)
        long uniqueAssignees = stories.stream()
            .map(Story::getAssignedTo)
            .filter(assignee -> assignee != null && !assignee.isEmpty())
            .distinct()
            .count();
        stats.setTeamMembersCount(uniqueAssignees);
        
        // Days Calculation
        if (sprint.getStartDate() != null && sprint.getEndDate() != null) {
            long totalDays = ChronoUnit.DAYS.between(sprint.getStartDate(), sprint.getEndDate());
            long daysElapsed = ChronoUnit.DAYS.between(sprint.getStartDate(), LocalDate.now());
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), sprint.getEndDate());
            
            stats.setTotalDays((int) totalDays);
            stats.setDaysElapsed((int) Math.max(0, daysElapsed));
            stats.setDaysRemaining((int) Math.max(0, daysRemaining));
        }
        
        return stats;
    }
    
    /**
     * Get statistics for a specific user
     */
    public UserStatsResponse getUserStats(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserStatsResponse stats = new UserStatsResponse();
        
        // User Info
        stats.setUserId(user.getId());
        stats.setUserName(user.getName());
        stats.setUserEmail(user.getEmail());
        stats.setRole(user.getRole().toString());
        
        // Get all stories assigned to user
        List<Story> assignedStories = storyRepository.findByAssignedTo(userId);
        
        // Story Assignment
        stats.setAssignedStories((long) assignedStories.size());
        stats.setCompletedStories(assignedStories.stream().filter(s -> s.getStatus() == StoryStatus.DONE).count());
        stats.setInProgressStories(assignedStories.stream().filter(s -> s.getStatus() == StoryStatus.IN_PROGRESS).count());
        
        // Get all tasks assigned to user (across all stories)
        List<Story> allStories = storyRepository.findAll();
        List<Task> userTasks = allStories.stream()
            .flatMap(story -> story.getTasks() != null ? story.getTasks().stream() : java.util.stream.Stream.empty())
            .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().equals(userId))
            .toList();
        
        // Task Assignment
        stats.setAssignedTasks((long) userTasks.size());
        stats.setCompletedTasks(userTasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count());
        stats.setInProgressTasks(userTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count());
        stats.setTodoTasks(userTasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count());
        
        // Story Points
        int totalPoints = assignedStories.stream().mapToInt(s -> s.getPoints() != null ? s.getPoints() : 0).sum();
        int completedPoints = assignedStories.stream()
            .filter(s -> s.getStatus() == StoryStatus.DONE)
            .mapToInt(s -> s.getPoints() != null ? s.getPoints() : 0)
            .sum();
        
        stats.setTotalStoryPoints(totalPoints);
        stats.setCompletedStoryPoints(completedPoints);
        
        // Average Task Completion Hours
        List<Task> completedTasks = userTasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.DONE && t.getActualHours() != null)
            .toList();
        
        if (!completedTasks.isEmpty()) {
            int totalHours = completedTasks.stream().mapToInt(Task::getActualHours).sum();
            stats.setAverageTaskCompletionHours(totalHours / completedTasks.size());
        } else {
            stats.setAverageTaskCompletionHours(0);
        }
        
        // Task Completion Rate
        if (stats.getAssignedTasks() > 0) {
            stats.setTaskCompletionRate((stats.getCompletedTasks() * 100.0) / stats.getAssignedTasks());
        } else {
            stats.setTaskCompletionRate(0.0);
        }
        
        // Current Workload
        stats.setActiveTasksCount(stats.getInProgressTasks());
        stats.setActiveStoriesCount(stats.getInProgressStories());
        
        return stats;
    }
    
    /**
     * Get simple counts for quick API responses
     */
    public java.util.Map<String, Long> getQuickCounts() {
        java.util.Map<String, Long> counts = new java.util.HashMap<>();
        
        counts.put("totalSprints", sprintRepository.count());
        counts.put("totalStories", storyRepository.count());
        counts.put("totalUsers", userRepository.count());
        counts.put("totalTeams", teamRepository.count());
        
        counts.put("activeSprints", sprintRepository.findAll().stream()
            .filter(s -> s.getStatus() == SprintStatus.ACTIVE).count());
        
        counts.put("doneStories", storyRepository.findAll().stream()
            .filter(s -> s.getStatus() == StoryStatus.DONE).count());
        
        return counts;
    }
}
