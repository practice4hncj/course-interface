package dao;

import domain.Clase;
import domain.Course;
import domain.Student;
import domain.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ITeacherDao {
    @Results(id = "teacherMap",value = {
            @Result(column = "js_gh",property = "tno",id = true),
            @Result(column = "js_mm",property = "pwd"),
            @Result(column = "js_xm",property = "name"),
            @Result(column = "js_xb",property = "sex"),
            @Result(column = "js_yx",property = "email"),
            @Result(column = "js_tx",property = "avatar"),
            @Result(column = "js_zt",property = "status")
    })
    @Select("select * from js")
    List<Teacher> findAll();


    //教师登录
    @Select("select * from js where js_gh=#{username} and js_mm=#{password}")
    @ResultMap(value = {"teacherMap"})
    Teacher findByTnoAndPwd(@Param("username") String username, @Param("password") String password);

    //添加教师
    @Insert("insert into js values(#{tno},#{pwd},#{name},#{sex},#{email},#{avatar},#{status})")
    void addTeacher(Teacher teacher);

    //删除教师
    @Delete("delete from js where js_gh=#{tno} and #{admin_pwd}=(select gly_mm from gly where gly_zh=#{admin_user} )")
    int deleteTeacher(@Param("tno") String tno,@Param("admin_user") String admin_user,@Param("admin_pwd") String admin_pwd);



    //重置教师密码
    @Update("update js set js_mm=#{newpwd} where js_gh=#{username} and js_mm=#{oldpwd}")
    int updatePwd(@Param("username") String username,@Param("oldpwd") String oldpwd,@Param("newpwd") String newpwd);


}