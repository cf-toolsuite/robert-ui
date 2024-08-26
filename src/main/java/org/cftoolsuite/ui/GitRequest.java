package org.cftoolsuite.ui;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public record GitRequest(
        String uri,
        String base,
        String username,
        String password,
        String commit,
        Set<String> filePaths,
        boolean pushToRemoteEnabled,
        boolean pullRequestEnabled) {

    public GitRequest(
            String uri,
            String base,
            String username,
            String password,
            String commit,
            Set<String> filePaths,
            boolean pushToRemoteEnabled,
            boolean pullRequestEnabled) {
        this.uri = (uri != null) ? uri : "";
        this.base = StringUtils.isNotBlank(base) ? base : "main";
        this.username = username;
        this.password = (password != null) ? password : "";
        this.commit = commit;
        this.filePaths = (filePaths != null) ? filePaths : new HashSet<>();
        this.pushToRemoteEnabled = pushToRemoteEnabled;
        this.pullRequestEnabled = pullRequestEnabled;
    }

    public boolean isAuthenticated() {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
    }

    public static class GitRequestBuilder {
        private String uri = "";
        private String base = "main";
        private String username;
        private String password = "";
        private String commit;
        private Set<String> filePaths = new HashSet<>();
        private boolean pushToRemoteEnabled;
        private boolean pullRequestEnabled;

        public GitRequestBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public GitRequestBuilder base(String base) {
            this.base = base;
            return this;
        }

        public GitRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public GitRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public GitRequestBuilder commit(String commit) {
            this.commit = commit;
            return this;
        }

        public GitRequestBuilder filePaths(Set<String> filePaths) {
            this.filePaths = filePaths;
            return this;
        }

        public GitRequestBuilder pushToRemoteEnabled(boolean pushToRemoteEnabled) {
            this.pushToRemoteEnabled = pushToRemoteEnabled;
            return this;
        }

        public GitRequestBuilder pullRequestEnabled(boolean pullRequestEnabled) {
            this.pullRequestEnabled = pullRequestEnabled;
            return this;
        }

        public GitRequest build() {
            return new GitRequest(uri, base, username, password, commit, filePaths, pushToRemoteEnabled, pullRequestEnabled);
        }
    }

    public static GitRequestBuilder builder() {
        return new GitRequestBuilder();
    }
}
