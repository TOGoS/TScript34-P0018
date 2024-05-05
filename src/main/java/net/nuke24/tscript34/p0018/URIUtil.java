package net.nuke24.tscript34.p0018;

public class URIUtil {
	public static String fileUri(String path) {
		return "file:"+path.replace("%", "%25");
	}
}
