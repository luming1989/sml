package org.hw.sml.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

	public static String toString(InputStream is, String charset)
			throws IOException {
		Assert.notNull(is, "inputstream is null!");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bs = new byte[512];
		int temp = -1;
		while ((temp = is.read(bs)) != -1) {
			baos.write(bs, 0, temp);
		}
		return baos.toString(charset);
	}
}
