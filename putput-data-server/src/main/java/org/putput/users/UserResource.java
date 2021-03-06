package org.putput.users;

import org.putput.api.model.*;
import org.putput.api.resource.User;
import org.putput.common.UuidService;
import org.putput.common.web.BaseResource;
import org.putput.password.PasswordService;
import org.putput.rss.RssFeedInfoEntity;
import org.putput.rss.RssFeedInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserResource extends BaseResource implements User {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordService passwordService;

  @Autowired
  RssFeedInfoRepository rssFeedInfoRepository;

  @Autowired
  UuidService uuidService;

  @Override
  public PostUserResponse postUser(UserRegistration userRegistration) throws Exception {
    UserEntity newUser = new UserEntity()
        .setUsername(userRegistration.getUserName())
        .setId(uuidService.uuid())
        .setEmail(userRegistration.getEmail())
        .setPasswordHash(passwordService.hash(userRegistration.getPassword()));
    
    userRepository.save(newUser);
    
    return PostUserResponse.withOK();
  }

  @Secured("USER")
  @Override
  public GetUserInfoResponse getUserInfo() throws Exception {
    return GetUserInfoResponse.withJsonOK(new UserInfo().withDisplayName(user()));
  }

  @Override
  public GetUserSettingsResponse getUserSettings() throws Exception {
    UserEntity currentUser = userRepository.findByUsername(user());
    return GetUserSettingsResponse.withJsonOK(new UserSettings().withEmail(currentUser.getEmail()));
  }

  @Override
  public PutUserSettingsResponse putUserSettings(UserSettingsUpdate settingsUpdate) throws Exception {
    UserEntity userToUpdate = userRepository.findByUsername(user());

    updateEmail(settingsUpdate, userToUpdate);
    updatePassword(settingsUpdate, userToUpdate);

    return PutUserSettingsResponse.withNoContent();
  }

  @Override
  public GetUserRssFeedsResponse getUserRssFeeds() throws Exception {
    List<RssFeed> rssFeeds = rssFeedInfoRepository.findByUsername(user())
        .stream()
        .map(rssFeedInfoEntity -> new RssFeed()
            .withId(rssFeedInfoEntity.getId())
            .withUrl(rssFeedInfoEntity.getUrl()))
        .collect(Collectors.toList());

    return GetUserRssFeedsResponse.withJsonOK(new RssFeedList().withRssFeeds(rssFeeds));
  }

  @Override
  public PostUserRssFeedsResponse postUserRssFeeds(RssFeed entity) throws Exception {
    RssFeedInfoEntity rssFeedInfoEntity = new RssFeedInfoEntity()
        .setOwner(userRepository.findByUsername(user()))
        .setUrl(entity.getUrl())
        .setId(uuidService.uuid());

    rssFeedInfoRepository.save(rssFeedInfoEntity);

    return PostUserRssFeedsResponse.withOK();
  }

  @Override
  public PutUserRssFeedResponse putUserRssFeed() throws Exception {
    throw new UnsupportedOperationException("not supported yet");
  }

  @Override
  public DeleteUserRssFeedByIdResponse deleteUserRssFeedById(String id) throws Exception {
    rssFeedInfoRepository.delete(id);
    return DeleteUserRssFeedByIdResponse.withOK();
  }

  private void updateEmail(UserSettingsUpdate settingsUpdate, UserEntity userToUpdate) {
    if (userToUpdate.getEmail() != null && !userToUpdate.getEmail().isEmpty()) {
      userToUpdate.setEmail(settingsUpdate.getEmail());
      userRepository.save(userToUpdate);
    }
  }

  private Optional<PasswordService.PasswordResetSuccess> updatePassword(UserSettingsUpdate settingsUpdate, UserEntity userToUpdate) {
    return passwordService.changePassword(userToUpdate.getUsername(), settingsUpdate.getPassword(), settingsUpdate.getPasswordRepeat());
  }

}
