package biz.buildit.web;
import biz.buildit.main.SiteEngineer;
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

@RequestMapping("/siteengineers")
@Controller
@RooWebScaffold(path = "siteengineers", formBackingObject = SiteEngineer.class)
public class SiteEngineerController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid SiteEngineer siteEngineer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, siteEngineer);
            return "siteengineers/create";
        }
        uiModel.asMap().clear();
        Assignments assignments = new Assignments();
        siteEngineerRepository.save(siteEngineer);
        assignments.setUserBuildIt(siteEngineer);
        assignments.setAuthority(Authorities.ROLE_SITE_ENGINEER);
        assignments.persist();
        return "redirect:/siteengineers/" + encodeUrlPathSegment(siteEngineer.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new SiteEngineer());
        return "siteengineers/create";
    }
}
