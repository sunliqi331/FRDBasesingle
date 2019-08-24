package com.its.frd.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.statistics.service.StatisticsService;
import com.its.statistics.vo.AnalyzeSearch;

/**
 * QDas导出映射控制
 * @author pactera
 *
 */
@Controller
@RequestMapping("/QdasStats")
public class QdasExportController {
	
	@Autowired
	private StatisticsService statisticsService;
	
	/**
	 * 进入Qdas导出页面
	 * @return
	 */
	@RequestMapping("/qdasExport")
	public ModelAndView enterQdasExportPage(){
		ModelAndView modelAndView = new ModelAndView("Qdas/exportQdas");
		modelAndView.addObject("products", statisticsService.getProductByCurrentCompanyId());
		return modelAndView;
	}
	
	/**
	 * 解析csv映射文件，创建qdas映射集合，生成qdas文件
	 * @param modelFile 映射文件，csv格式，第一位是要转换的K值域，第二位是转换前的变量
	 * @param productId 产品Id
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping(value="/generateQdasFile",method=RequestMethod.POST)
	public String generateQdasFile(MultipartFile modelFile,Long productId,Long chooseProcedure,
			String suffix,String begin,String end,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException, ParseException{
		InputStreamReader isr = new InputStreamReader(modelFile.getInputStream(), "gbk");
		BufferedReader reader = new BufferedReader(isr);
		String line;
		//解析映射文件，生成映射map集合
		Map<String, String> mapping = new HashMap<>();
		List<String> tempList = new ArrayList<>();
		while(StringUtils.isNotEmpty(line=reader.readLine())){
			//split[0]是转换后最终的K值域，split[1]是转换前的变量，
			String[] split = compatibleArray(line,tempList);
			//验证每一行的数据长度
			if(split.length!=2){
				continue;
			}
			if(StringUtils.isEmpty(split[0]) || StringUtils.isEmpty(split[1])){
				continue;
			}
			mapping.put(split[1], split[0]);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AnalyzeSearch analyzeSearch = new AnalyzeSearch();
		analyzeSearch.setProductId(productId);
		analyzeSearch.setBegin(sdf.parse(begin));
		analyzeSearch.setEnd(sdf.parse(end));
		analyzeSearch.setSuffix(suffix);
		String result = "";
		if(mapping.size()>0){
			result = statisticsService.ExportQdasData(analyzeSearch,mapping,chooseProcedure);
		}else{
			result = statisticsService.ExportQdasData(analyzeSearch,null,chooseProcedure);
		}
		String path = DynamicSpecifications.getRequest().getSession().getServletContext().getRealPath("/");
		File file = new File(path+"\\qdas."+analyzeSearch.getSuffix());
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		org.apache.commons.io.FileUtils.writeStringToFile(file, result);
		IOUtils.closeQuietly(out);
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition","attachment;fileName=qdas." + analyzeSearch.getSuffix());// 设置文件名
		byte[] buffer = new byte[1024];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			OutputStream os = response.getOutputStream();
			int i = bis.read(buffer);
			while (i != -1) {
				os.write(buffer, 0, i);
				i = bis.read(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 判断一个字符串是否包含字母或数字以外的字符
	 * @param res
	 * @return 如果包含，返回true
	 */
	/*public static boolean isContainLetterOrDigit(String res){
		boolean flag = false;
		if(StringUtils.isEmpty(res)){
			return flag;
		}
		for(int i=0 ; i<res.length() ; i++){
			if(!Character.isLetterOrDigit(res.charAt(i))){
				flag = true;
				break;
			}
		}
		return flag;
	}*/
	
	/**
	 * 兼容模板的空格间隔模式，和逗号分隔模式
	 * @param originalStr 原始的字符串
	 * @param tempList 临时数组
	 * @return
	 */
	public static String[] compatibleArray(String originalStr,List<String> tempList){
		String[] originalArray = originalStr.split(",");
		if(originalArray.length!=2){
			tempList.clear();
			String[] temp = originalStr.split(" ");
			for(String s : temp){
				if(StringUtils.isNotEmpty(s)){
					tempList.add(s);
				}
			}
			if(tempList.size()==2){
				originalArray = tempList.toArray(new String[tempList.size()]);
			}
		}
		//去除空格和csv产生的引号
		for(int i=0;i<originalArray.length;i++){
			originalArray[i] = originalArray[i].replaceAll(" ", "");
			originalArray[i] = originalArray[i].replace("\"", "");
		}
		
		return originalArray;
	}
	
}
