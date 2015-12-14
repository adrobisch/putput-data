package org.putput;

import org.putput.common.UuidService;
import org.putput.profile.ProfileService;
import org.putput.stream.StreamItemService;
import org.putput.users.UserEntity;
import org.putput.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TestDataService implements SmartLifecycle {

  @Autowired
  UserRepository userRepository;

  @Autowired
  StreamItemService streamItemService;

  @Autowired
  ProfileService profileService;

  @Autowired
  UuidService uuidService;

  private AtomicBoolean running = new AtomicBoolean(false);

  @Override
  public boolean isAutoStartup() {
    return true;
  }

  @Override
  public void stop(Runnable runnable) {
    running.set(false);
    runnable.run();
  }

  @Override
  public void start() {
    running.set(true);

    UserEntity testUser = addTestUser();
    streamItemService.newItemEntity(testUser.getUsername(), "Test Put.", Optional.empty(), Optional.empty());
    streamItemService.newItemEntity("user", "User Put.", Optional.empty(), Optional.empty());

    profileService.addFollower("user", "johndoe");
  }

  private UserEntity addTestUser() {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(uuidService.uuid());
    userEntity.setUsername("johndoe");
    userEntity.setEmail("john@doe.com");
    userEntity.setPasswordHash("$2a$10$NV4rlug0FGyW0UuivufxOetXnm7FeVgDGXbBmtajo6htdDl6g9bs.");
    userEntity.setAbout("John.");

    return userRepository.save(userEntity);
  }

  @Override
  public void stop() {
    throw new IllegalStateException("should never be called for Smart Lifecycle");
  }

  @Override
  public boolean isRunning() {
    return running.get();
  }

  @Override
  public int getPhase() {
    return 0;
  }
}