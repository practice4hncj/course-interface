package dao;

import domain.Comment;
import domain.Course;
import domain.Student;
import domain.Topic;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ICommentDao {
    @Results(id = "commentMap",value = {
            @Result(column = "pl_bh",property = "commentid",id = true),
            @Result(column = "xs_xh",property = "sno"),
            @Result(column = "xs_xm",property = "sname"),
            @Result(column = "xs_tx",property = "savatar"),
            @Result(column = "ht_bh",property = "topicid"),
            @Result(column = "pl_nr",property = "commentcontent"),
            @Result(column = "pl_sj",property = "commenttime")
    })
    //查询所有的评论信息
    @Select("select * " +
            "from pl " +
            "limit #{start},#{end};")
    List<Comment> findAll(@Param("start") Integer start, @Param("Integer") int end);

    //修改评论
    @Update("<script>" +
            "update pl" +
            "<set>" +
            "<if test='content!=null'> pl_nr=#{content},</if>" +
            "</set>" +
            "where pl_bh=#{commentid}" +

            "</script>")
    void UpdateComment(@Param("commentid") Integer commentid,@Param("content") String content);


    //添加评论
    @Insert("insert into pl(xs_xh,ht_bh,pl_nr,pl_sj) values(#{sno},#{topicid},#{commentcontent},#{commenttime})")
    void addComment(Comment comment);

    //删除评论
    @Delete("delete from pl where pl_bh=#{commentid}")
    int delComment(@Param("commentid") String commentid);

    //按话题号查找评论
    @Select("select pl_bh,pl.xs_xh,xs.xs_xm,xs.xs_tx,ht_bh,pl_nr,pl_sj from pl,xs "+
            "where xs.xs_xh=pl.xs_xh and pl.ht_bh=#{topicid} " +
            "order by pl_sj desc " +
            "limit #{start},#{end}")
    @ResultMap(value = {"commentMap"})
    List<Comment> getCommentByTopicid(@Param("topicid") Integer topicid,@Param("start") Integer start, @Param("end") Integer end);

    //统计某话题下的评论总数
    @Select("select count(*) from pl where ht_bh=#{topicid}")
    int Total(@Param("topicid") Integer topicid);
}
