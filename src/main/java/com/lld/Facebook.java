package com.lld;


import java.util.LinkedList;

public class Facebook {

    public class User {

        private Long id;

        private String name;

        private String email;

        private String password;

        // Profile details -> Address, Work, Education, Location, etc

    }
    public class Member extends User {

    }

    public class Post {

        private Long id;

        private String text;

        private String uploadMediaUrl;

        private Long createdTimestamp;

        private Long editedTimestamp;

        private Long memberId;

        private Integer likes = 0;

        private Integer shares = 0;

        private LinkedList<Comment> comments = new LinkedList<>();

        public void addLike() {
            this.likes++;
        }

        public void addShares() {
            this.shares++;
        }

        public void addComment(String text, String memberId) {

        }
    }

    public class Comment {
        private Integer likes = 0;

        private Long id;

        private String message;

        private Long memberId;

        private Long createdTimestamp;

        private Long editedTimestamp;

        public void addLike() {
            this.likes++;
        }
    }

    public class Group {

    }

    public class Page {

    }

    public class Recommendation {

    }

}

