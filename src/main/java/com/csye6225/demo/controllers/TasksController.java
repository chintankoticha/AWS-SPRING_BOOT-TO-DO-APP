package com.csye6225.demo.controllers;

import com.csye6225.demo.pojo.TaskAttachments;
import com.csye6225.demo.pojo.Tasks;
import com.csye6225.demo.pojo.User;
import com.csye6225.demo.pojo.UserSession;
import com.csye6225.demo.repo.TaskAttachmentRepository;
import com.csye6225.demo.repo.TasksRepository;

import com.csye6225.demo.repo.UserRepository;
import org.json.simple.*;

import com.google.gson.reflect.TypeToken;
import javafx.concurrent.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class TasksController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TaskAttachmentRepository taskAttachmentRepo;

    @Autowired
    private TasksRepository taskRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String getTasks(HttpServletRequest request) {

        //Take user object from Basic Authentication
        String auth = request.getHeader("Authorization");

        List<Tasks> tasks = new ArrayList();
        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            final String[] values = credentials.split(":", 2);
            System.out.println("User is ::::: " + values[0]);
            System.out.println(" Password is ::::: " + values[1]);

            User user  = userRepo.findByUserName(values[0]);

            tasks = taskRepo.findTasksByUser(user);
        }

        JSONArray jsonArray = new JSONArray();


        for(Tasks task : tasks)
        {
            JSONObject json = new JSONObject();

            json.put("id", task.getId());
            json.put("url", task.getDescription());
            JSONArray attachmentArr = new JSONArray();
            for(TaskAttachments attachments: task.getTaskAttachments())
            {

                JSONObject attachmentObj = new JSONObject();
                attachmentObj.put("id", attachments.getId().toString());
                attachmentObj.put("url", attachments.getFileName());
                attachmentArr.add(attachmentObj);
            }

            json.put("attachments", attachmentArr);
            jsonArray.add(json);

        }
        return jsonArray.toString();
    }

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getAttachments(@PathVariable(value = "id") String taskId) {

        Tasks task = taskRepo.findTasksByTaskId(taskId);
        List<TaskAttachments> attachments = taskAttachmentRepo.findByTask(task);
        JSONArray jsonArray = new JSONArray();
        for (TaskAttachments attachment : attachments) {
            JSONObject json = new JSONObject();
            json.put("id", attachment.getId().toString());
            json.put("url", attachment.getFileName());
            jsonArray.add(json);
        }
        return jsonArray.toString();
    }
}