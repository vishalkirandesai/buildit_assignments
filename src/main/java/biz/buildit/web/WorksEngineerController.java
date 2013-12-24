package biz.buildit.web;
import biz.buildit.main.WorksEngineer;
import biz.buildit.security.Assignments;
import biz.buildit.security.Authorities;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/worksengineers")
@Controller
@RooWebScaffold(path = "worksengineers", formBackingObject = WorksEngineer.class)
public class WorksEngineerController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid WorksEngineer worksEngineer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, worksEngineer);
            return "worksengineers/create";
        }
        Assignments assignments = new Assignments();
        uiModel.asMap().clear();
        worksEngineer.persist();
        assignments.setUserBuildIt(worksEngineer);
        assignments.setAuthority(Authorities.ROLE_WORKS_ENGINEER);
        assignments.persist();
        return "redirect:/worksengineers/" + encodeUrlPathSegment(worksEngineer.getId().toString(), httpServletRequest);
    }
}
