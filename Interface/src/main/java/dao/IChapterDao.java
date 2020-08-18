package dao;

import domain.Chapter;
import domain.Problem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IChapterDao {
    @Results(id = "examinationMap",value = {
            @Result(column = "tm_bh",property = "pid",id = true),
            @Result(column = "zj_bh",property = "chapterid"),
            @Result(column = "tmlx_bh",property = "ptype"),
            @Result(column = "tm_tg",property = "question"),
            @Result(column = "tm_da",property = "panswer")
    })
    @Select("select * from tm")
    List<Problem> findAll();

    //添加章节
    @Insert("insert into zj(kc_bh,zj_mc) values(#{cid},#{chaptername})")
    void addChapter(Chapter chapter);

    //删除章节
    @Delete("delete from zj where zj_bh=#{chapterid}")
    int delChapter(@Param("chapterid") String chapterid);




    //查询指定章节中的所有题目
    @Select("select * from tm\n" +
            "where zj_bh=#{chapterid};")
    @ResultMap(value = {"examinationMap"})
    List<Problem> findProblemByChapterId(@Param("chapterid") String chapterid);
}