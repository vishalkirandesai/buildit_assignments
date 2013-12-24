package biz.buildit.web;
import biz.buildit.main.PlantHireRequest;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/planthirerequests")
@Controller
@RooWebScaffold(path = "planthirerequests", formBackingObject = PlantHireRequest.class)
public class PlantHireRequestController {
}
