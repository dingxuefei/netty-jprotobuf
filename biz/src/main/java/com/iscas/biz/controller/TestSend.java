package com.iscas.biz.controller;

import com.iscas.biz.service.general.CommunicationService;
import com.iscas.protobuf.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/7 1:38
 * @since jdk1.8
 */
@RestController
public class TestSend {
    @Autowired
    private CommunicationService communicationService;
    @GetMapping("/send")
    public String send() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setCmd(MessageDTO.CommandType.MODEL_DATA);
        messageDTO.setData("wwgwgwee");
        messageDTO.setClientId("web-app");
        communicationService.pushDataToModel(messageDTO);
        return "success";
    }
}
