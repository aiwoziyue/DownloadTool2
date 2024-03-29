package team.redrock.downloadtool.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.redrock.downloadtool.entity.FileInf;
import team.redrock.downloadtool.entity.Picture;
import team.redrock.downloadtool.jpa.FileInfJPA;
import team.redrock.downloadtool.utils.Response;
import team.redrock.downloadtool.utils.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImp implements FileService {
    @Autowired
    FileInfJPA fileInfJPA;

   @Override
    public Response fileUpload(String filepath,HttpServletRequest request) {
        HttpSession session = request.getSession();
        String filename = Utility.getFileName(filepath);
        String suffix = Utility.getsuffix(filename);

        FileInf fileInf = new FileInf();
        fileInf.setFilename(filename);
        fileInf.setFilepath(filepath);
        fileInf.setSuffix(suffix);
        fileInf.setTime(new Date());
        fileInf.setUsername((String) session.getAttribute("user_session"));
        fileInfJPA.save(fileInf);


        return new Response("0", JSON.toJSONString(fileInf));
    }

    public Response fileUpload(String filename,String filepath,String suffix, String remark, String username){
        FileInf fileInf = new FileInf();
        fileInf.setFilename(filename);
        fileInf.setFilepath(filepath);
        fileInf.setSuffix(suffix);
        fileInf.setTime(new Date());
        fileInf.setUsername(username);
        fileInf.setFoldername("ROOT");
        fileInf.setRemark(remark);
        fileInfJPA.save(fileInf);
//        Cookie[] cookies = request.getCookies();
//        String token = "";
//        for (Cookie cookie : cookies) {
//            switch(cookie.getName()){
//                case "token":
//                    token = cookie.getValue();
//                    break;
//                default:
//                    break;
//            }
//        }
//        System.out.println("tiken-"+token);
        return new Response("0", JSON.toJSONString(fileInf));
    }

    @Override
    public Response fileDelete(String filename) {

        fileInfJPA.DeleteFile(filename);
        return new Response("0", JSON.toJSONString("文件删除成功"));
    }

    @Transactional
    @Override
    public Response deleteFileByFileid(Integer fileId){
        fileInfJPA.deleteFileByFileid(fileId);
        return new Response("0", JSON.toJSONString("文件删除成功"));
    }

    @Override
    public Response fileSelect(String filename, String username) {
        FileInf fileInf = fileInfJPA.findByFilenameAndUsername(filename, username);
        if(fileInf==null) {
            return new Response("-1","文件不存在");
        }
        return new Response("0",JSON.toJSONString(fileInf));
    }

    @Override
    public Response fileList() {
        List<FileInf> fileInfs = fileInfJPA.FindAllById();
        return new Response("0", JSON.toJSONString(fileInfs));

    }

    private static List<String> GetPastDaysList() {
        ArrayList<String> pastDaysList = new ArrayList<>();
        for (int i = 7; i >= 0; i--) {
            pastDaysList.add(getPastDate(i));
        }
        return pastDaysList;
    }

    @Override
    public JSONObject getListByUser(Integer current, Integer rowCount, HttpServletRequest request) {
        String username = request.getSession().getAttribute("user").toString();
        JSONObject jsonObject = new JSONObject();
        List<FileInf> fileInfs;
        if (rowCount == -1)
            fileInfs = fileInfJPA.getAllByUsername(username);
        else
            fileInfs = fileInfJPA.GetAllByUsernameAndStartRowAndSize(username, (current - 1) * rowCount, rowCount);
        jsonObject.put("current", current);
        jsonObject.put("rowCount", rowCount);
        jsonObject.put("rows", fileInfs);
        jsonObject.put("total", fileInfJPA.GetAllNumberByUsername(username));
        return jsonObject;
    }

    @Override
    public JSONObject getVideoListByUser(Integer current, Integer rowCount, HttpServletRequest request){
        String username = request.getSession().getAttribute("user").toString();
        JSONObject jsonObject = new JSONObject();
        List<FileInf> fileInfs;
        if (rowCount == -1)
            fileInfs = fileInfJPA.GetVideoListByUsername(username);
        else
            fileInfs = fileInfJPA.GetVideoListByUsernameAndAndStartRowAndSize(username, (current - 1) * rowCount, rowCount);
        jsonObject.put("current", current);
        jsonObject.put("rowCount", rowCount);
        jsonObject.put("rows", fileInfs);
        jsonObject.put("total", fileInfJPA.GetVideoNumberByUsername(username));
        return jsonObject;
    }

    @Override
    public JSONObject getMusicListByUser(Integer current, Integer rowCount, HttpServletRequest request){
        String username = request.getSession().getAttribute("user").toString();
        JSONObject jsonObject = new JSONObject();
        List<FileInf> fileInfs;
        if (rowCount == -1)
            fileInfs = fileInfJPA.GetMusicListByUsername(username);
        else
            fileInfs = fileInfJPA.GetMusicListByUsernameAndAndStartRowAndSize(username, (current - 1) * rowCount, rowCount);
        jsonObject.put("current", current);
        jsonObject.put("rowCount", rowCount);
        jsonObject.put("rows", fileInfs);
        jsonObject.put("total", fileInfJPA.GetMusicNumberByUsername(username));
        return jsonObject;
    }

    @Override
    public JSONObject getTextByUser(Integer current, Integer rowCount, HttpServletRequest request){
        String username = request.getSession().getAttribute("user").toString();
        JSONObject jsonObject = new JSONObject();
        List<FileInf> fileInfs;
        if (rowCount == -1)
            fileInfs = fileInfJPA.GetTextListByUsername(username);
        else
            fileInfs = fileInfJPA.GetTextListByUsernameAndStartRowAndSize(username, (current - 1) * rowCount, rowCount);
        jsonObject.put("current", current);
        jsonObject.put("rowCount", rowCount);
        jsonObject.put("rows", fileInfs);
        jsonObject.put("total", fileInfJPA.GetTextNumberByUsername(username));
        return jsonObject;
    }

    @Override
    public JSONObject getTorrentByUser(Integer current, Integer rowCount, HttpServletRequest request){
        String username = request.getSession().getAttribute("user").toString();
        JSONObject jsonObject = new JSONObject();
        List<FileInf> fileInfs;
        if (rowCount == -1)
            fileInfs = fileInfJPA.GetTorrentListByUsername(username);
        else
            fileInfs = fileInfJPA.GetTorrentListByUsernameAndStartRowAndSize(username, (current - 1) * rowCount, rowCount);
        jsonObject.put("current", current);
        jsonObject.put("rowCount", rowCount);
        jsonObject.put("rows", fileInfs);
        jsonObject.put("total", fileInfJPA.GetTorrentNumberByUsername(username));
        return jsonObject;
    }

    @Override
    public Response getFileByFileid(Integer fileid){
        FileInf fileInf = fileInfJPA.findByFileid(fileid);
        return new Response("0", JSON.toJSONString(fileInf));
    }

    @Override
    public JSONObject getFileTypeNumber(HttpServletRequest request){
        String username = request.getSession().getAttribute("user").toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("video", fileInfJPA.GetVideoNumberByUsername(username));
        jsonObject.put("music", fileInfJPA.GetMusicNumberByUsername(username));
        jsonObject.put("text", fileInfJPA.GetTextNumberByUsername(username));
        jsonObject.put("torrent", fileInfJPA.GetTorrentNumberByUsername(username));
        jsonObject.put("other", fileInfJPA.GetOtherNumberByUsername(username));
        return jsonObject;
    }

    @Override
    public JSONObject getOtherByUser(Integer current, Integer rowCount, HttpServletRequest request){
        String username = request.getSession().getAttribute("user").toString();
        JSONObject jsonObject = new JSONObject();
        List<FileInf> fileInfs;
        if (rowCount == -1)
            fileInfs = fileInfJPA.GetOtherListByUsername(username);
        else
            fileInfs = fileInfJPA.GetOtherListByUsernameAndStartRowAndSize(username, (current - 1) * rowCount, rowCount);
        jsonObject.put("current", current);
        jsonObject.put("rowCount", rowCount);
        jsonObject.put("rows", fileInfs);
        jsonObject.put("total", fileInfJPA.GetOtherNumberByUsername(username));
        return jsonObject;
    }

    public boolean isFileExist(String filename, String username){
        return fileInfJPA.findByFilenameAndUsername(filename, username) != null;
    }

    @Override
    public Response AudioList() {
        List<FileInf> fileInfs = fileInfJPA.SelectAudio();
        return new Response("0",JSON.toJSONString(fileInfs));
    }

    @Override
    public Response VedioList() {
        List<FileInf> fileInfs = fileInfJPA.SelectVideo();
        return new Response("0",JSON.toJSONString(fileInfs));
    }

    @Override
    public Response TextList() {
        List<FileInf> fileInfs = fileInfJPA.SelectText();
        return new Response("0",JSON.toJSONString(fileInfs));
    }

    @Override
    public Response TorrentList() {
        List<FileInf> fileInfs = fileInfJPA.SelectTorrent();
        return new Response("0",JSON.toJSONString(fileInfs));
    }

    public void savePic(String filename,String suffix){
       String path = "D:\\temp\\";
        Picture picture = new Picture();
        picture.setPicname(filename);
        if(Utility.isVedio(suffix).equals("vedio")){
            picture.setPicpath(path+filename+".jpg");
        }
        if(Utility.isVedio(suffix).equals("audio")){
            picture.setPicpath(path+"audio.jpg");
        }
        if(Utility.isVedio(suffix).equals("text")){
            picture.setPicpath(path+"text.jpg");
        }
        if(Utility.isVedio(suffix).equals("torrent")){
            picture.setPicpath(path+"torrent.jpg");
        }
        if(Utility.isVedio(suffix).equals("other")){
            picture.setPicpath(path+"other.jpg");
        }
    }

    @Override
    public JSONObject getLastSevenDaysFileNumber(HttpServletRequest request){
        String username = request.getSession().getAttribute("user").toString();
        List<String> daysList = GetPastDaysList();
        JSONObject jsonObject = new JSONObject();

        JSONArray upload = new JSONArray();
        for (String s : daysList) {
            Integer number = fileInfJPA.GetFileUpNumberByUsernameAndDate(s, username);
            upload.add(number);
        }
        JSONArray download = new JSONArray();
        for (String s : daysList) {
            Integer number = fileInfJPA.GetFileDownNumberByUsernameAndDate(s, username);
            download.add(number);
        }

        jsonObject.put("upload", upload);
        jsonObject.put("download", download);
        return jsonObject;
    }

    /**
     * 获取过去第几天的日期
     */
    private static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(today);
    }

}

