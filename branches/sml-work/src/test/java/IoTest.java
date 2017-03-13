import org.hw.sml.core.resolver.JsEngine;


public class IoTest {
	public static void main(String[] args) {
		Object obj=JsEngine.evel("function hide(str){return str.substr(0,str.length-8)+'********'}; hide('23432423424234234')");
		System.out.println(obj);
	}
}
