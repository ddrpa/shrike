package cc.ddrpa.shrike.signature;

public class ShrikeConstant {
    private ShrikeConstant() {
        throw new IllegalStateException("Constant class");
    }
    public static final String SHRIKE_SIGNATURE_VERSION = "1.0";
    public static final String SHRIKE_SIGNATURE_DELIMITER = "\n";
    public static final String SHRIKE_SIGNATURE_ALGORITHM = "HMAC-SHA256";
}