package net.radioapp.web.Network;

import java.nio.ByteBuffer;

public class UDPDataArray {
    byte[] data;
    int editingPosition = 0;
    public static final int CHUNKSIZE = 1024;
    public static final int METADATASIZE = 5;
    public UDPDataArray(int size){
        data = new byte[size];
    }
    public UDPDataArray(byte[] data){
        this.data = data;
    }

    public void addData(byte v){addData(new byte[]{v});}

    public void addData(byte[] values){
        for (byte value : values) {
            data[editingPosition] = value;
            editingPosition++;
        }
        if (editingPosition > CHUNKSIZE){
            throw new RuntimeException("El tamaño de un paquete ha superado el esperado. Revisa el tamaño de los metadatos");
        }
    }

    public void addData(byte[] values, int start, int end){
        for (int i = start; i <= end; i++) {
            data[editingPosition] = values[i];
            editingPosition++;
        }
        if (editingPosition > CHUNKSIZE){
            throw new RuntimeException("El tamaño de un paquete ha superado el esperado. Revisa el tamaño de los metadatos");
        }
    }
    public byte[] getData(int start, int end){
        if(end < start){throw  new RuntimeException("Error al leer datos de un paquete: Fin antes que inicio");}
        byte[] res = new byte[end-start + 1];
        for(int i = 0; i < res.length ; i++) {res[i] = data[i + start]; }
        return res;
    }
    public PackageTypes getType(){return PackageTypes.obtenerTipoPorCodigo(data[0]);}
    public int getEndIndex(){return data.length-1;}
    public byte[] getData(){return data;}

    public static int byteToInt(byte[] c){
        return ByteBuffer.wrap(c).getInt();
    }

    public static byte[] intToByte(int c){
        return ByteBuffer.allocate(4).putInt(c).array();
    }

}
