package com.ptitB22CN539.QuizRemake.DTO.Response.Chart;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NumberOfPlayerParticipatingForTime {
    private Date date;
    private Long numberOfPlayers;
}
