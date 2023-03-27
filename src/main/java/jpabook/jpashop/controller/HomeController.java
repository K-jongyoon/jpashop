package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j      // 로그를 사용하기 위해 사용
public class HomeController {


    @RequestMapping("/")
    public String home(){
        log.info("home controller");
        return "home";      // 이렇게하면 home.html로 찾아가서 thyleaf 를 찾아가게 된다.
    }
}
