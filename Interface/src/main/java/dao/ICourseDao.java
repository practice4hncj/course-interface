package dao;

import domain.Chapter;
import domain.Course;
import domain.Semester;
import domain.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ICourseDao {
    @Results(id = "courseMap",value = {
            @Result(column = "kc_bh",property = "cid",id = true),
            @Result(column = "xq_bh",property = "semester"),
            @Result(column = "js_gh",property = "tno"),
            @Result(column = "js_xm",property = "tname"),
            @Result(column = "kc_mc",property = "cname"),
            @Result(column = "kc_fm",property = "coverimg"),
            @Result(column = "kc_zt",property = "status"),
            @Result(column = "count(xskc.xs_xh)",property = "snum")
    })
    //教师教授的所有课程
    //或按课程名称模糊查找
    @Select("<script>" +
            "select kc.kc_bh,kc.xq_bh,kc.js_gh,js.js_xm,kc.kc_mc,kc.kc_fm,kc.kc_zt,count(xskc.xs_xh) " +
            "from js,kc,xskc " +
            "where js.js_gh=kc.js_gh and xskc.kc_bh=kc.kc_bh " +
            "and ${condition} " +
            "group by kc_bh " +
            "<if test='start!=null'> limit #{start},#{end} </if>" +
            "</script>")
    List<Course> findTeachCourse(@Param("condition") String condition,@Param("start") Integer start, @Param("end") Integer end);




    //添加课程
    @Insert("insert into kc(xq_bh,js_gh,kc_mc,kc_zt) values(#{semester},#{tno},#{cname},#{status})")
    @SelectKey(statement="select last_insert_id();",before=false,keyColumn="kc_bh",resultType=int.class,keyProperty="cid")
    void addCourse(Course course);


    //修改课程(课程名称、课程封面、课程状态)
    @Update("<script>" +
            "update kc" +
            "<set>" +
            "<if test='name!=null'> kc_mc=#{name},</if>" +
            "<if test='avatar!=null'> kc_fm=#{avatar},</if>" +
            "<if test='status!=null'> kc_zt=#{status},</if>" +
            "</set>" +
            "where kc_bh=#{courseid}" +
            "</script>")
    void UpdateCourse(@Param("courseid") Integer courseid,@Param("name") String name,@Param("avatar") String avatar,@Param("status") Integer status);

    //查询所有课程的信息
    @Select("<script>" +
            "select kc.kc_bh,kc.xq_bh,kc.js_gh,js.js_xm,kc.kc_mc,kc.kc_fm,kc.kc_zt,count(xskc.xs_xh)\n" +
            "from js,kc,xskc\n" +
            "where js.js_gh=kc.js_gh and kc.kc_bh=xskc.kc_bh\n" +
            "group by kc_bh " +
            "${limit}"+
            "</script>")
    @ResultMap(value = {"courseMap"})
    List<Course> findAll(@Param("limit") String limit);

    //统计课程的总数
    @Select("select count(*) from kc")
    int Total();

    //删除课程
    @Delete("delete from kc where kc_bh=#{courseid}")
    int delCourse(@Param("courseid") String courseid);

    //按课程号查找课程
    @Select("select kc.kc_bh,kc.xq_bh,kc.js_gh,js.js_xm,kc.kc_mc,kc.kc_fm,kc.kc_zt,count(xskc.xs_xh)\n" +
            "from js,kc,xskc\n" +
            "where js.js_gh=kc.js_gh and kc.kc_bh=xskc.kc_bh and kc.kc_bh=#{courseid}\n")
    @ResultMap(value = {"courseMap"})
    Course getCourseByCid(@Param("courseid") Integer courseid);

    //按学号和学期查找课程
    @Select("select kc.kc_bh,kc.xq_bh,kc.js_gh,js.js_xm,kc.kc_mc,kc.kc_fm,kc.kc_zt,count(xskc.xs_xh)\n" +
            "from js,kc,xskc\n" +
            "where xq_bh=#{semesterid} and js.js_gh=kc.js_gh and kc.kc_bh=xskc.kc_bh and kc.kc_bh in\n" +
            "(select kc_bh from xskc where xs_xh=#{studentid})\n" +
            "group by kc.kc_bh;")
    @ResultMap(value = {"courseMap"})
    List<Course> getCourseBySno(@Param("studentid") Integer studentid,@Param("semesterid") Integer semesterid);

}
