package cc.ddrpa.shrike.core.enums;

import java.util.stream.Stream;

public enum SymmetricEncryptionMethod {
    AES128GCM("aes128gcm"),
    SM4("sm4"),
    ;

    private final String name;

    SymmetricEncryptionMethod(String name) {
        this.name = name;
    }

    public static SymmetricEncryptionMethod of(String name) {
        return Stream.of(SymmetricEncryptionMethod.values())
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getName() {
        return name;
    }
}
