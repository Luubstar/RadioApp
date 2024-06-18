package net.radioapp.web.emisor;

public enum AudioExtension {
    WAV(1), MP3(2);

    final int bytevalue;
    AudioExtension(int bytevalue){
        this.bytevalue = bytevalue;
    }

    public byte getBytevalue() {
        return (byte) bytevalue;
    }
    public static AudioExtension obtenerTipoPorCodigo(int codigo) {
        for (AudioExtension tipo : AudioExtension.values()) {
            if (tipo.getBytevalue() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se encontró un formato de audio con el código especificado: " + codigo);
    }
}
