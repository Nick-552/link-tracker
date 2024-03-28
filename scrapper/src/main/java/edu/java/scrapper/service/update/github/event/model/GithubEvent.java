package edu.java.scrapper.service.update.github.event.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GithubEvent {

    @JsonProperty("type")
    private GithubEventType type;
    @JsonProperty("payload")
    private Payload payload;
    @JsonProperty("actor")
    private GithubUser actor;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("repo")
    private GithubRepo repo;

    public record Payload(
        @JsonProperty("action")
        String action,
        @JsonProperty("comment")
        GithubComment comment,
        @JsonProperty("ref")
        String ref,
        @JsonProperty("ref_type")
        String refType,
        @JsonProperty("master_branch")
        String masterBranch,
        @JsonProperty("forkee")
        GithubRepo forkee,
        @JsonProperty("pages")
        GithubWikiPage[] pages,
        @JsonProperty("issue")
        GithubIssue issue,
        @JsonProperty("asignee")
        GithubUser assignee,
        @JsonProperty("label")
        GithubLabel label,
        @JsonProperty("member")
        GithubUser member,
        @JsonProperty("pull_request")
        GithubPullRequest pullRequest,
        @JsonProperty("review")
        GithubPullRequestReview review,
        @JsonProperty("commits")
        GithubCommit[] commits,
        @JsonProperty("release")
        GithubRelease release,
        @JsonProperty("thread")
        GithubThread thread
    ) {
    }

    public record GithubUser(
        @JsonProperty("login")
        String login
    ) {
    }

    public record GithubRepo(
        @JsonProperty("name")
        String fullName,
        @JsonProperty("html_url")
        String htmlUrl
    ) {
    }

    public record GithubComment(
        @JsonProperty("body")
        String body,
        @JsonProperty("user")
        GithubUser user,
        @JsonProperty("html_url")
        String htmlUrl
    ) {
    }

    public record GithubWikiPage(
        @JsonProperty("html_url")
        String htmlUrl,
        @JsonProperty("page_name")
        String pageName
    ) {
    }

    public record GithubIssue(
        @JsonProperty("html_url")
        String htmlUrl,
        @JsonProperty("body")
        String body,
        @JsonProperty("user")
        GithubUser user
    ) {
    }

    public record GithubLabel(
        @JsonProperty("fullName")
        String name,
        @JsonProperty("description")
        String description
    ) {
    }

    public record GithubPullRequest(
        @JsonProperty("html_url")
        String htmlUrl,
        @JsonProperty("title")
        String title,
        @JsonProperty("user")
        GithubUser user
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GithubPullRequestReview(
        @JsonProperty("user")
        GithubUser author,
        @JsonProperty("body")
        String body,
        @JsonProperty("html_url")
        String htmlUrl,
        @JsonProperty("state")
        String state
    ) {
    }

    public record GithubCommit(
        @JsonProperty("message")
        String message,
        @JsonProperty("author")
        GithubUser author
    ) {
    }

    public record GithubRelease(
        @JsonProperty("html_url")
        String htmlUrl,
        @JsonProperty("name")
        String name,
        @JsonProperty("body")
        String body
    ) {
    }

    public record GithubThread(
        @JsonProperty("html_url")
        String htmlUrl
    ) {
    }
}
