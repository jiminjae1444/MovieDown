package com.example.MovieDown.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] poster;
    private String title;
    private String director;
    private String category;
    private LocalDate date;
    private int time;
    private String age;
    private String cast;
    private String synobsis;
    private int price;
    private String url;

    @OneToMany(mappedBy = "movie")
    private List<Comment> comments = new ArrayList<>();

}
