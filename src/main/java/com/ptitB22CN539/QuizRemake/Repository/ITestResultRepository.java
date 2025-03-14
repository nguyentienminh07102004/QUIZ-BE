package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Domains.TestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ITestResultRepository extends JpaRepository<TestResultEntity, String> {
    @Query(value = "select tr.test.id as test, count(tr.test.id) as numberOfPlayers from TestResultEntity tr group by tr.test.id order by count(tr.test.id) desc")
    List<List<String>> findTop10By();

    @Query(value = "select function('date', tr.startedDate), count(tr.id) from TestResultEntity tr where tr.startedDate >= :startDate and tr.startedDate <= :endDate group by function('date', tr.startedDate)")
    List<List<String>> findNumberOfPlayerParticipatingTest(Date startDate, Date endDate);
}
