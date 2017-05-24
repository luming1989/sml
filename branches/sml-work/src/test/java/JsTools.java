import org.hw.sml.core.resolver.JsEngine;


public class JsTools {
	public static void main(String[] args) {
		System.out.println(JsEngine.evel("function hide(str){return str.substr(0,str.length-8)+'********'}"));
		System.out.println(JsEngine.evel("hide('21431243242134124324')"));
		System.out.println(JsEngine.evel("function hidephone(str,num){return str.substr(0,3+num)+'****'+str.substr(7+num,4)}"));
		System.out.println(JsEngine.evel("hidephone('8615721413207',2)"));
	}
}
