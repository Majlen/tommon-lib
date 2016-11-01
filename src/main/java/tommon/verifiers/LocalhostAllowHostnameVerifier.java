package tommon.verifiers;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Implementation of HostnameVerifier, which allows connection to localhost,
 * even if the certificate does not cover that hostname.
 *
 * @author Milan Ševčík
 */
public class LocalhostAllowHostnameVerifier implements HostnameVerifier {
	private HostnameVerifier verifier;

	public LocalhostAllowHostnameVerifier(HostnameVerifier verifier) {
		super();
		this.verifier = verifier;
	}

	@Override
	public boolean verify(String s, SSLSession sslSession) {
		if (s.equals("localhost")) {
			return true;
		} else {
			return verifier.verify(s, sslSession);
		}
	}
}
