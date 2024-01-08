package com.souvik.twitter.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tweets")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private String image;
    private String video;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();
    @OneToMany
    private List<Tweet> tweetReplies = new ArrayList<>();
    @ManyToMany
    private List<User> retweets = new ArrayList<>();
    @ManyToOne
    private Tweet repliedFor;

    private boolean isReply;
    private boolean isTweet;
}
