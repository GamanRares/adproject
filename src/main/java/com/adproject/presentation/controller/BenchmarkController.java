package com.adproject.presentation.controller;

import com.adproject.service.dynamo.DynamoServiceImpl;
import com.adproject.service.mongo.MongoServiceImpl;
import com.adproject.service.mysql.SqlServiceImpl;
import com.adproject.utils.ExecutionTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.adproject.service.ServiceFactory.getService;

/**
 * @author Gaman Rares-Constantin on 5/18/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Controller
@RequestMapping("/benchmark")
public class BenchmarkController {

    @GetMapping("/{id}")
    public ModelAndView form(@PathVariable int id,
                             HttpServletResponse httpServletResponse,
                             Model model) {
        final List<ExecutionTime> executionTimes = new ArrayList<>();
        switch (id) {
            case 1: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo1());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo1());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo1());
                break;
            }
            case 2: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo2());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo2());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo2());
                break;
            }
            case 3: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo3());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo3());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo3());
                break;
            }
            case 4: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo4());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo4());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo4());
                break;
            }
            case 5: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo5());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo5());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo5());
                break;
            }
            case 6: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo6());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo6());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo6());
                break;
            }
            case 7: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo7());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo7());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo7());
                break;
            }
            case 8: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo8());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo8());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo8());
                break;
            }
            case 9: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo9());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo9());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo9());
                break;
            }
            case 10: {
                executionTimes.add(getService(MongoServiceImpl.class).executeQueryNo10());
                executionTimes.add(getService(DynamoServiceImpl.class).executeQueryNo10());
                executionTimes.add(getService(SqlServiceImpl.class).executeQueryNo10());
                break;
            }
            default: {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
        }

        model.addAttribute("executionTimes", executionTimes);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return new ModelAndView("content :: contentFragment");
    }
}
