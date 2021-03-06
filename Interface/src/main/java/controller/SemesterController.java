package controller;

import dao.IClassDao;
import dao.ICourseDao;
import dao.ISemesterDao;
import domain.Clase;
import domain.Semester;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import util.APIResult;
import util.AccountUtil;

import java.util.List;

//控制器类
@Controller
@RequestMapping(path = "/semester")
public class SemesterController {
    /**
     * 添加学期
     * @param semester
     * @return
     */
    @RequestMapping(path = "/addsemester",method = {RequestMethod.POST},headers = {"Accept"})
    @ResponseBody
    public APIResult addClass(Semester semester,String adminuser,String adminpwd){
        if(!AccountUtil.isAdmin(adminuser,adminpwd))
            return APIResult.createNg("无操作权限");
        SqlSession session=util.MyBatis.getSession();
        ISemesterDao semesterDao=session.getMapper(ISemesterDao.class);
        try {
            semesterDao.addSemester(semester);
            session.commit();
            return APIResult.createOk("添加成功", semester.getSemestername());
        } catch (Exception e) {
            e.printStackTrace();
            return APIResult.createNg("添加失败");
        } finally {
            session.close();
        }
    }

    /**
     * 修改学期
     * @param semesterid
     * @param name
     * @return
     */
    @RequestMapping(path = "/updatesemester",method = {RequestMethod.POST},headers = {"Accept"})
    @ResponseBody
    public APIResult updateSemester(Integer semesterid,String name,String adminuser,String adminpwd){
        if(!AccountUtil.isAdmin(adminuser,adminpwd))
            return APIResult.createNg("无操作权限");
        SqlSession session=util.MyBatis.getSession();
        ISemesterDao semesterDao=session.getMapper(ISemesterDao.class);
        try {
            semesterDao.UpdateSemester(semesterid,name);
            session.commit();
            return APIResult.createOKMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return APIResult.createNg("修改失败");
        } finally {
            session.close();
        }
    }

    /**
     * 删除学期
     * @param semesterid
     * @return
     */
    @RequestMapping(path = "/delsemester", method = {RequestMethod.POST}, headers = {"Accept"})
    @ResponseBody
    public APIResult DeleteClase(int semesterid,String adminuser,String adminpwd) {
        if(!AccountUtil.isAdmin(adminuser,adminpwd))
            return APIResult.createNg("无操作权限");
        SqlSession session = util.MyBatis.getSession();
        int status=0;       //学生
        ISemesterDao semesterDao = session.getMapper(ISemesterDao.class);
        status=semesterDao.delSemester(semesterid);
        session.commit();
        session.close();
        if(status!=0){
            return APIResult.createOKMessage("删除成功");
        }else{
            return APIResult.createNg("删除失败");
        }
    }

    /**
     * 查询所有的学期信息
     * @return
     */
    @RequestMapping(path = "/allsemester",method = {RequestMethod.POST,RequestMethod.GET},headers = {"Accept"})
    @ResponseBody
    public APIResult AllTeacher(){
        SqlSession session=util.MyBatis.getSession();
        ISemesterDao semesterDao=session.getMapper(ISemesterDao.class);
        List<Semester> semesters=semesterDao.findAll();
        if(!semesters.isEmpty()){
            return APIResult.createOk("查询成功",semesters);
        }else{
            return APIResult.createNg("查询结果为空");
        }
    }
}
