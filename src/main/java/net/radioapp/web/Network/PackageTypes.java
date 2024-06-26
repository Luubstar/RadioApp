package net.radioapp.web.Network;

public enum PackageTypes {
    HELO(0),
    LOG(1),
    ACTUALIZAR(2),
    MOVER(3),
    INICIOEMISION(4),
    FINEMISION(5),
    EMISION(6),
    LOCKVOLUMEN(7),
    LOCKFRECUENCIA(8),
    LOCKONOFF(9),
    PING(10),
    SOLICITAREMISION(11),
    CAMBIODEGRUPO(12);

    final int bytevalue;
    PackageTypes(int bytevalue){
        this.bytevalue = bytevalue;
    }

    public byte getBytevalue() {
        return (byte) bytevalue;
    }

    public static PackageTypes obtenerTipoPorCodigo(int codigo) {
        for (PackageTypes tipo : PackageTypes.values()) {
            if (tipo.getBytevalue() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se encontró un tipo de mensaje con el código especificado: " + codigo);
    }

}
