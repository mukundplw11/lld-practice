package com.lld.facebook;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * Functionalities Implemented:
 *
 * <li>User can create a post</li>
 * <li>User can add a comment</li>
 * <li>User can follow other users</li>
 *
 * <br>
 * Functionalities that could be added:
 * <li>Provide recommendations to user for group/pages</li>
 * <li>Follow pages/groups</li>
 * <li>Share a post</li>
 * <li>Comment reply</li>
 */
public class Facebook {
    
    public static void main(String[] args) {
        System.out.println("Welcome to Facebook");

        Member member1 = new Member("M1", "m1@gmail.com");
        Member member2 = new Member("M2", "m2@gmail.com");

        member2.follow(member1);
        Post post1 = member1.addPost("My post", null);
        System.out.println("Member 1 posted " + post1.id);
        List<Post> member2feed = member2.getFeed(null, null);
        System.out.println("Feed posts " + member2feed.get(0).id + " " + member2feed.get(0).text);
        post1.addComment("M2 Comment", member2);
        post1.addLike();
        System.out.println("Post likes " + post1.likes + " Post comments " + post1.comments.size() + " by " + post1.comments.get(0).uploadedBy.name);
    }

    static class Utils {
        private static final Random random = new Random();

        public static long getNextId() {
            return random.nextInt(1000);
        }
    }

}


class User {

    Long id;

    String name;

    String email;

    String password;

    // Profile details -> Address, Work, Education, Location, etc

    public User(String name, String email) {
        this.id = Facebook.Utils.getNextId();
        this.name = name;
        this.email = email;
        this.password = "1234"; // Default password
    }

}

class Member extends User {
    LinkedList<Post> userPosts = new LinkedList<>();

    List<Member> followers;

    List<Page> followedPages;
    List<Group> followedGroups;
    List<Member> followees;


    public Member(String name, String email) {
        super(name, email);
        this.followers = new ArrayList<>();
        this.followees = new ArrayList<>();
        this.followedPages = new ArrayList<>();
        this.followedGroups = new ArrayList<>();
    }

    public Post addPost(String text, String attachmentUrl) {
        Post post = new Post(text, attachmentUrl, this);
        userPosts.add(post);
        return post;
    }

    public void follow(Member member) {
        this.followees.add(member);
        member.followers.add(this);
    }

    public List<Post> getFeed(Long offset, Long limit) {
        offset = offset == null ? 0L : offset;
        limit = limit == null ? 10 : limit;
        return this.followees.stream().map(m -> m.userPosts).flatMap(List::stream).skip(offset).limit(limit).collect(Collectors.toList());
    }
}

class Post {

    Long id;

    String text;

    String uploadMediaUrl;

    Long createdTimestamp;

    Long editedTimestamp;

    Member uploadedBy;

    Integer likes = 0;

    LinkedList<Comment> comments = new LinkedList<>();

    public Post(String text, String fileUrl, Member uploadedBy) {
        this.id = Facebook.Utils.getNextId();
        this.text = text;
        this.uploadedBy = uploadedBy;
        this.uploadMediaUrl = fileUrl;
        this.createdTimestamp = new Date().getTime();
        this.editedTimestamp = new Date().getTime();
    }

    public void addLike() {
        this.likes++;
    }

    public void addComment(String text, Member member) {
        Comment comment = new Comment(text, member);
        this.comments.add(comment);
    }
}

class Comment {
    Integer likes = 0;

    Long id;

    String message;

    Member uploadedBy;

    Long createdTimestamp;

    Long editedTimestamp;

    public Comment(String message, Member uploadedBy) {
        this.id = Facebook.Utils.getNextId();
        this.uploadedBy = uploadedBy;
        this.createdTimestamp = new Date().getTime();
        this.editedTimestamp = new Date().getTime();
    }

    public void addLike() {
        this.likes++;
    }
}

class Group {
    Long id;
    String name;
    List<Member> members;
    LinkedList<Post> groupPosts = new LinkedList<>();

    public Group(String name) {
        this.id = Facebook.Utils.getNextId();
        this.members = new ArrayList<>();
        this.name = name;
    }

    public Post addPost(String text, String attachmentUrl, Member member) {
        Post post = new Post(text, attachmentUrl, member);
        member.userPosts.add(post);
        groupPosts.add(post);
        return post;
    }
}

class Page {
    Long id;
    String name;
    List<Member> followers;
    List<Member> moderators;
    LinkedList<Post> pagePosts = new LinkedList<>();

    public Page(String name) {
        this.id = Facebook.Utils.getNextId();
        this.followers = new ArrayList<>();
        this.moderators = new ArrayList<>();
        this.name = name;
    }

    public Post addPost(String text, String attachmentUrl, Member member) {
        Post post = new Post(text, attachmentUrl, member);
        member.userPosts.add(post);
        pagePosts.add(post);
        return post;
    }
}


