package com.wen.ojweb.controller;

import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.model.ResultJSON;
import com.wen.ojweb.service.OjuserService;
import com.wen.ojweb.serviceimpl.OjuserServiceimpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    OjuserService ojuserService;

    private static final Log log = LogFactory.getLog(LoginController.class);

    /**
     * 导出用户信息的Excel表格
     * @throws IOException
     */
    @GetMapping("/exportUserInfo")
    public void download(HttpServletResponse response, HttpSession session) throws IOException {

        Ojuser user = (Ojuser) session.getAttribute("user");

        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Excel表单,参数为sheet的名字
        HSSFSheet sheet = workbook.createSheet("用户信息");

        //创建表头
        setUserInfoTitle(workbook, sheet);
        List<Ojuser> list = ojuserService.selectAll();

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for (Ojuser ojuser:list) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(ojuser.getUsername());
            row.createCell(1).setCellValue(ojuser.getPassword());
            if(ojuser.getSex())
                row.createCell(2).setCellValue("男");
            else
                row.createCell(2).setCellValue("女");
            row.createCell(3).setCellValue(ojuser.getNickname());
            row.createCell(4).setCellValue(ojuser.getSchool());
            row.createCell(5).setCellValue(ojuser.getEmail());
            row.createCell(6).setCellValue(ojuser.getIntroduce());
            rowNum++;
        }
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");//设置日期格式
//        String fileName = df.format(new Date())+"用户信息 "+ user.getUsername()+ " 下载";
        String fileName = df.format(new Date())+" 用户信息.xls";
        //清空response
        response.reset();
        //设置response的Header
        fileName = new String(java.net.URLEncoder.encode(fileName, "UTF-8"));
        response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //将excel写入到输出流中
        workbook.write(os);
        os.flush();
        os.close();
    }
    /***
     * 设置用户信息表头
     * @param workbook
     * @param sheet
     */
    private void setUserInfoTitle(HSSFWorkbook workbook, HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 30*256);
        sheet.setColumnWidth(1, 20*256);
        sheet.setColumnWidth(2, 5*256);
        sheet.setColumnWidth(3, 30*256);
        sheet.setColumnWidth(4, 30*256);
        sheet.setColumnWidth(5, 30*256);
        sheet.setColumnWidth(6, 100*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("用户名");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("密码（6-20位）");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("性别（男或女）");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("昵称");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("学校");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("邮箱地址");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("自我介绍");
        cell.setCellStyle(style);
    }

    /**
     * 下载导入用户信息的模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/DownloadImportUserInfoDemo")
    public void DownloadImportUserInfoDemo(HttpServletResponse response) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Excel表单,参数为sheet的名字
        HSSFSheet sheet = workbook.createSheet("用户信息");

        //创建表头
        setUserInfoTitle(workbook, sheet);

        String fileName = "导入用户信息模板.xls";
        //清空response
        response.reset();
        //设置response的Header
        fileName = new String(java.net.URLEncoder.encode(fileName, "UTF-8"));
        response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //将excel写入到输出流中
        workbook.write(os);
        os.flush();
        os.close();
    }

    /**
     * 导入用户信息数据
     * @param file
     * @return
     */
    @PostMapping("/uploadUserInfo")
    @ResponseBody
    public ResultJSON upload(MultipartFile file) {
        if (file==null) {
            return ResultJSON.fail();
        }
        System.out.printf(file.toString());
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            //有多少个sheet
            int sheets = workbook.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                //获取多少行
                int rows = sheet.getPhysicalNumberOfRows();
                //遍历每一行，注意：第 0 行为标题
                for (int j = 1; j < rows; j++) {
                    Ojuser ojuser  = new Ojuser();
                    //获得第 j 行
                    HSSFRow row = sheet.getRow(j);
                    ojuser.setUsername(row.getCell(0).getStringCellValue());//用户名
                    ojuser.setPassword(row.getCell(1).getStringCellValue());//密码
                    if(row.getCell(2).getStringCellValue().equals("男"))//性别
                        ojuser.setSex(true);
                    else
                        ojuser.setSex(false);
                    ojuser.setNickname(row.getCell(3).getStringCellValue());//昵称
                    ojuser.setSchool(row.getCell(4).getStringCellValue());//学校
                    ojuser.setEmail(row.getCell(5).getStringCellValue());//邮箱
                    ojuser.setIntroduce(row.getCell(5).getStringCellValue());//自我介绍
                    if(ojuserService.selectByPrimaryKey(ojuser.getUsername())==null)
                        //如果数据用户名不重复插入数据到数据库
                        ojuserService.insertSelective(ojuser);
                }
            }

        } catch (IOException e) {
            if(log.isErrorEnabled()){
                log.trace("上传文件过程中出现错误");
            }
            return ResultJSON.fail();
        }
        return ResultJSON.success();
    }

}
