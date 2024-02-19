package mysite.sbb.configuration.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class CommonUtil {
	public String markdown(String markDown){
		Parser parser = Parser.builder().build();
		Node parse = parser.parse(markDown);
		HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
		return htmlRenderer.render(parse);
	}
}
