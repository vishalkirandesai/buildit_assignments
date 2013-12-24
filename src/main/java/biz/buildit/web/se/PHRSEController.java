package biz.buildit.web.se;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import biz.buildit.exception.InvalidDatesException;
import biz.buildit.main.Plant;
import biz.buildit.main.PlantHireRequest;
import biz.buildit.main.SiteEngineer;
import biz.buildit.main.WorksEngineer;
import biz.buildit.repository.PlantHireRequestRepository;
import biz.buildit.repository.SiteEngineerRepository;
import biz.buildit.repository.WorksEngineerRepository;
import biz.buildit.util.Approval;

@RequestMapping("/se/phrs")
@Controller
@RooWebScaffold(path = "se/phrs", formBackingObject = PlantHireRequest.class, delete = false)
public class PHRSEController {

	@Autowired
    PlantHireRequestRepository plantHireRequestRepository;
	@Autowired
	SiteEngineerRepository siteEngineerRepository;
	@Autowired 
	WorksEngineerRepository worksEngineerRepository;
	
	Logger logger = LoggerFactory.getLogger(PHRSEController.class);
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid PlantHireRequest plantHireRequest, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws InvalidDatesException {
		if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, plantHireRequest);
            return "se/phrs/create";
        }
		if(plantHireRequest.getStartDate()!=null && plantHireRequest.getEndDate()!=null && 
				plantHireRequest.getStartDate().getTime() < plantHireRequest.getEndDate().getTime()){
			logger.debug("Create PHR : "+plantHireRequest.getId());
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        logger.debug("authentication :"+authentication);
	        String name = authentication.getName();
	        SiteEngineer siteEngineer = siteEngineerRepository.findSiteEngineerByName(name);
	        plantHireRequest.setSiteEng(siteEngineer);
	        plantHireRequest.setApproval(Approval.PENDINGAPPROVAL);
	        uiModel.asMap().clear();
	        plantHireRequestRepository.save(plantHireRequest);
	        logger.debug("Redirect path : /se/phrs/"+encodeUrlPathSegment(plantHireRequest.getId().toString(), httpServletRequest));
		}
		else{
			throw new InvalidDatesException("Invalid or no dates entered.");
		}
        return "redirect:/se/phrs/" + encodeUrlPathSegment(plantHireRequest.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new PlantHireRequest());
        return "se/phrs/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("planthirerequest", plantHireRequestRepository.findOne(id));
        uiModel.addAttribute("itemId", id);
        return "se/phrs/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("planthirerequests", plantHireRequestRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / sizeNo, sizeNo)).getContent());
            float nrOfPages = (float) plantHireRequestRepository.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("planthirerequests", plantHireRequestRepository.findAll());
        }
        addDateTimeFormatPatterns(uiModel);
        return "se/phrs/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid PlantHireRequest plantHireRequest, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, plantHireRequest);
            return "se/phrs/update";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("authentication :"+authentication);
        SiteEngineer siteEngineer = siteEngineerRepository.findSiteEngineerByName(authentication.getName());
        if(plantHireRequest.getSiteEng() == null)
        	plantHireRequest.setSiteEng(siteEngineer);
        else if(!siteEngineer.getUsername().equals(plantHireRequest.getSiteEng().getUsername())){
        	logger.debug("You're not allowed to update a PHR belonging to another SE. Redirect path : /se/phrs/"+encodeUrlPathSegment(plantHireRequest.getId().toString(), httpServletRequest));
            return "redirect:/se/phrs/" + encodeUrlPathSegment(plantHireRequest.getId().toString(), httpServletRequest);
        }
        PlantHireRequest plantHireRequestOriginal = PlantHireRequest.findPlantHireRequest(plantHireRequest.getId());
        if(plantHireRequestOriginal.getApproval() == Approval.APPROVED)
        	return "redirect:/se/phrs/" + encodeUrlPathSegment(plantHireRequest.getId().toString(), httpServletRequest);
        plantHireRequest.setApproval(Approval.PENDINGAPPROVAL);
        uiModel.asMap().clear();
        plantHireRequestRepository.save(plantHireRequest);
        return "redirect:/se/phrs/" + encodeUrlPathSegment(plantHireRequest.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, plantHireRequestRepository.findOne(id));
        return "se/phrs/update";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("plantHireRequest_startdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("plantHireRequest_enddate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("plantHireRequest_extensiondate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, PlantHireRequest plantHireRequest) {
        uiModel.addAttribute("plantHireRequest", plantHireRequest);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("plants", Plant.findAllPlants());
        uiModel.addAttribute("siteengineers", siteEngineerRepository.findAll());
        uiModel.addAttribute("worksengineers", worksEngineerRepository.findAll());
        uiModel.addAttribute("approvals", Arrays.asList(Approval.values()));
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
	
	@RequestMapping(value="/{id}/cancel", produces="text/html")
	String cancelPHR(@PathVariable Long id){
		PlantHireRequest plantHireRequest = PlantHireRequest.findPlantHireRequest(id);
		if(plantHireRequest.getApproval()!=Approval.APPROVED){
			plantHireRequest.setApproval(Approval.CANCEL);
			plantHireRequest.persist();
		}
		return "redirect:/se/phrs/"+id;
	}
}
