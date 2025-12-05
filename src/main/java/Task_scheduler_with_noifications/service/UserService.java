package Task_scheduler_with_noifications.service;

import Task_scheduler_with_noifications.models.User;

public interface UserService {
    void addUser(User user);
    void updateUserProfile(User user);
    User getUserProfile();
}
