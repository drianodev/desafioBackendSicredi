package br.com.drianodev.backendapi.model.enums;

public enum VotoOpcao {

    SIM(true),
    NAO(false),
    BRANCO(null);

    private final Boolean valor;

    VotoOpcao(Boolean valor) {
        this.valor = valor;
    }

    public Boolean getValor() {
        return valor;
    }

    public static VotoOpcao fromString(String str) {
        switch (str.toLowerCase()) {
            case "sim":
                return SIM;
            case "não":
                return NAO;
            case "branco":
                return BRANCO;
            default:
                throw new IllegalArgumentException("Opção de voto inválida: " + str);
        }
    }
}
