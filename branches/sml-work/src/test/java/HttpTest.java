import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpTest {
	public static String query(String url,String accessToken,String ifId,String requestBody) throws Exception{
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accessToken", accessToken);
            conn.setRequestProperty("IfId", ifId);
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("accept", "application/json;charset=UTF-8");
            conn.setRequestProperty("content-type","application/json;charset=UTF-8");
            conn.setRequestProperty("SOAPAction","");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            out = new PrintWriter(conn.getOutputStream());
            out.print(requestBody);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
        return result;
	}
	public static void main(String[] args) throws Exception {
		String content="{\"msisdns\":\"15721413207\",\"content\":\"test\",\"fnId\":\"1000\"}";
		String result=query("http://10.221.247.7:19080/ipmsDS/ipms","YWRtaW5obHdhc2QkMTIz","IF-3RD-SMS-XN-SHARE-001",content);
		System.out.println(result);
	}
}
