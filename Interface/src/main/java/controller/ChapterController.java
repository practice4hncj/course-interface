package controller;

import dao.IChapterDao;
import domain.Chapter;
import domain.Problem;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import util.APIResult;

import java.util.List;

//控制器类
@Controller
@RequestMapping(path = "/chapter")
public class ChapterController {
    /**
     * 添加章节
     * @param chapter
     * @return
     */
    @RequestMapping(path = "/addchapter",method = {RequestMethod.POST,RequestMethod.GET},headers = {"Accept"})
    @ResponseBody
    public APIResult AddChapter(Chapter chapter){
        //查询数据库
        SqlSession session=util.MyBatis.getSession();
        IChapterDao iChapterDao=session.getMapper(IChapterDao.class);
        try {
            iChapterDao.addChapter(chapter);
            session.commit();
            return APIResult.createOk("添加成功", chapter);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResult.createNg("添加失败");
        } finally {
            session.close();
        }
    }

    /**
     * 删除章节
     * @param chapterid
     * @return
     */
    @RequestMapping(path = "/delchapter", method = {RequestMethod.POST, RequestMethod.GET}, headers = {"Accept"})
    @ResponseBody
    public APIResult DeleteClase(String chapterid) {
        //查询数据库
        SqlSession session = util.MyBatis.getSession();
        int status=0;       //学生
        IChapterDao chapterDao = session.getMapper(IChapterDao.class);
        status=chapterDao.delChapter(chapterid);
        session.commit();
        if(status!=0){
            return APIResult.createOKMessage("删除成功");
        }else{
            return APIResult.createNg("删除失败");
        }
    }


    /**
     * 查询某章节的所有题目
     * @param chapterid
     * @return
     */
    @RequestMapping(path = "/problem",method = {RequestMethod.POST,RequestMethod.GET},headers = {"Accept"})
    @ResponseBody
    public APIResult StudentLogin(String chapterid){
        //查询数据库
        SqlSession session=util.MyBatis.getSession();
        IChapterDao iChapterDao=session.getMapper(IChapterDao.class);
        List<Problem> problems =iChapterDao.findProblemByChapterId(chapterid);
        if(!problems.isEmpty()){
            for(Problem problem : problems)
                System.out.println(problem.toString());
            return APIResult.createOk("查询成功", problems);
        }else{
            return APIResult.createNg("此章节尚未添加题目");
        }
    }
    }