function UsersApi(http, api) {

  var userSettingsRelation = "user-settings";

  this.userInfo = function (userInfoCallback) {
    return api.withLink("user", function (userLink) {
      return http.get(userLink).success(userInfoCallback);
    });
  };

  this.requestNewPassword = function(email) {
    return api.withLink("password-reset", function (resetLink) {
      return http.post(resetLink, {
        "email": email
      });
    })
  };

  this.getSettings = function() {
    return api.withLink(userSettingsRelation, function (settingsLink) {
      return http.get(settingsLink);
    })
  };

  this.updateSettings = function(settings) {
    return api.withLink(userSettingsRelation, function (settingsLink) {
      return http.put(settingsLink, settings);
    })
  };
}

UsersApi.$inject = ["$http", "api"];

module.exports = UsersApi;