package com.its.statistics.controller;

import com.its.common.controller.BaseController;
import com.its.statistics.service.SpcAnalyseService;
import com.its.statistics.vo.SpcAnalyzeSearch;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/spcAnalyse")
public class SpcAnalyseController extends BaseController {

    @Resource
    private SpcAnalyseService spcAnalyseService;

    @RequestMapping("/saveAnalyseSPCData")
    public @ResponseBody
    String saveAnalyseSPCData(HttpServletRequest request, SpcAnalyzeSearch spcAnalyzeData) {
        //param为spc一次发的数据
        String datas = request.getParameter("param");
        String result = spcAnalyseService.saveAnalyseSPCData(spcAnalyzeData, datas);
        return result;
    }
}
