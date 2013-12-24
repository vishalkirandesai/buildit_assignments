package biz.buildit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.hateoas.Link;

@XmlTransient
public class ResourceSupport extends org.springframework.hateoas.ResourceSupport{
	@XmlElement(name = "_link", namespace = Link.ATOM_NAMESPACE)
	@org.codehaus.jackson.annotate.JsonProperty("_links")
	private final List<ExtendedLink> _links;
	
	public ResourceSupport(){
		super();
		this._links = new ArrayList<>();
	}
	
	public void add(Link link) {
		if(link instanceof ExtendedLink)
			this._links.add((ExtendedLink) link);
		else
			super.add(link);
	}
	
	public List<ExtendedLink> get_links() {
		return Collections.unmodifiableList(_links);
	}

	public void remove_links() {
		_links.clear();
	}

	public Link get_link(String rel) {

		for (Link link : _links) {
			if (link.getRel().equals(rel)) {
				return link;
			}
		}

		return null;
	}
}
